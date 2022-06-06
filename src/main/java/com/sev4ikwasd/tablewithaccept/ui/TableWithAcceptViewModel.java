package com.sev4ikwasd.tablewithaccept.ui;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Abstract ViewModel implementing accept and cancel functionality.
 *
 * @param <T> Model class
 * @param <V> wrapped Model class
 */
public abstract class TableWithAcceptViewModel<T, V extends TableItemViewModel<T>> implements ViewModel {

    /**
     * Current selected table row.
     */
    protected final ObjectProperty<V> selectedTableRow = new SimpleObjectProperty<>();
    private final ObservableList<V> list = FXCollections.observableArrayList();
    private final List<T> markedToAddList = new ArrayList<>();
    private final List<T> markedToRemoveList = new ArrayList<>();
    private final Class<T> typeClass;
    private final Class<V> viewModelClass;
    private Consumer<V> onSelect;

    /**
     * @param typeClass      class object of Model class
     * @param viewModelClass class object of wrapped Model class
     */
    public TableWithAcceptViewModel(Class<T> typeClass, Class<V> viewModelClass) {
        this.typeClass = typeClass;
        this.viewModelClass = viewModelClass;
    }

    /**
     * @return list of items to be displayed
     */
    protected abstract List<T> getItems();

    /**
     * Updates visible table of items.
     */
    protected void updateList() {
        // Clear changes
        markedToAddList.clear();
        markedToRemoveList.clear();

        List<T> items = getItems();

        list.clear();
        items.forEach(item -> {
            try {
                list.add(viewModelClass.getDeclaredConstructor(typeClass).newInstance(item));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        if (selectedTableRow.get() != null) {
            Optional<V> selectedRow = list.stream()
                    .filter(row -> row.getItem().equals(selectedTableRow.get().getItem()))
                    .findFirst();

            Optional.of(onSelect).ifPresent(consumer -> consumer.accept(selectedRow.orElse(null)));
        }
    }

    /**
     * @return observable list of items in table
     */
    public ObservableList<V> getList() {
        return list;
    }

    /**
     * @param consumer for updating selected item of table in view
     */
    public void setOnSelect(Consumer<V> consumer) {
        onSelect = consumer;
    }

    public ObjectProperty<V> selectedTableRowProperty() {
        return selectedTableRow;
    }

    /**
     * Adds new row to table.
     */
    public void add() {
        try {
            T newItem = typeClass.getDeclaredConstructor().newInstance();
            markedToAddList.add(newItem);
            V newTableViewModel = viewModelClass.getDeclaredConstructor(typeClass).newInstance(newItem);
            list.add(newTableViewModel);
            Optional.of(onSelect).ifPresent(consumer -> consumer.accept(newTableViewModel));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes selected row from table.
     */
    public void remove() {
        T selectedItem = selectedTableRow.get().getItem();
        list.remove(selectedTableRow.get());
        if (!markedToAddList.remove(selectedItem))
            markedToRemoveList.add(selectedItem);
    }

    /**
     * Cancels all changes of table.
     */
    public void cancel() {
        updateList();
    }

    /**
     * Accepts all changes of table.
     */
    public void accept() {
        // Remove all items marked for removal
        for (T item : markedToRemoveList) {
            if (!removeItem(item)) {
                removeItemError(item);
                return;
            }
        }

        for (V tableViewModel : list) {
            // Add all items marked for addition
            if (markedToAddList.contains(tableViewModel.getItem())) {
                if (tableViewModel.accept() && addItem(tableViewModel.getItem())) continue;
                else {
                    addItemError(tableViewModel.getItem());
                    return;
                }
            }
            // Update all changed items
            if (tableViewModel.isChanged()) {
                if (tableViewModel.accept() && updateItem(tableViewModel.getItem())) continue;
                else {
                    updateItemError(tableViewModel.getItem());
                    return;
                }
            }
        }
        updateList();
    }

    /**
     * Removes item from Model.
     *
     * @param item to be removed from Model
     * @return {@code true} if removal was successful
     */
    protected abstract boolean removeItem(T item);

    /**
     * Adds item to Model
     *
     * @param item to be added to Model
     * @return {@code true} if addition was successful
     */
    protected abstract boolean addItem(T item);

    /**
     * Updates item in Model
     *
     * @param item updated item
     * @return {@code true} if update was successful
     */
    protected abstract boolean updateItem(T item);

    /**
     * Handler of error in item removal
     *
     * @param item which was not successfully removed
     */
    protected abstract void removeItemError(T item);

    /**
     * Handler of error in item addition
     *
     * @param item which was not successfully added
     */
    protected abstract void addItemError(T item);

    /**
     * Handler of error in item update
     *
     * @param item which was not successfully updated
     */
    protected abstract void updateItemError(T item);
}
