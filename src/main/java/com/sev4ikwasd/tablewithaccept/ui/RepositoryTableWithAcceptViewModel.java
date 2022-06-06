package com.sev4ikwasd.tablewithaccept.ui;

import com.sev4ikwasd.tablewithaccept.model.Repository;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

/**
 * A ViewModel for a typical repository.
 *
 * @param <T> Model class
 * @param <V> wrapped Model class
 */
public abstract class RepositoryTableWithAcceptViewModel<T, V extends TableItemViewModel<T>> extends TableWithAcceptViewModel<T, V> {
    /**
     * Property of error alert, {@code true} when error occurs, must be changed to {@code false} after error was handled.
     */
    protected final BooleanProperty errorAlertTriggered = new SimpleBooleanProperty(false);
    /**
     * Property of text of error alert
     */
    protected final StringProperty errorAlertText = new SimpleStringProperty();
    private final Repository<T> repository;

    /**
     * @param typeClass      class object of Model class
     * @param viewModelClass class object of wrapped Model class
     * @param repository     for Model of given type
     */
    public RepositoryTableWithAcceptViewModel(Class<T> typeClass, Class<V> viewModelClass, Repository<T> repository) {
        super(typeClass, viewModelClass);
        this.repository = repository;
    }

    /**
     * Initialize method as in MvvmFx documentation.
     */
    public void initialize() {
        updateList();
    }

    public BooleanProperty errorAlertTriggeredProperty() {
        return errorAlertTriggered;
    }

    public StringProperty errorAlertTextProperty() {
        return errorAlertText;
    }

    @Override
    protected List<T> getItems() {
        return repository.getAll();
    }

    @Override
    protected boolean removeItem(T item) {
        return repository.remove(item);
    }

    @Override
    protected boolean addItem(T item) {
        return repository.add(item);
    }

    @Override
    protected boolean updateItem(T item) {
        return repository.update(item);
    }

    @Override
    protected void removeItemError(T item) {
        errorAlertText.set("Error occurred while removing item: " + item.toString());
        errorAlertTriggered.set(true);
    }

    @Override
    protected void addItemError(T item) {
        errorAlertText.set("Error occurred while adding item: " + item.toString());
        errorAlertTriggered.set(true);
    }

    @Override
    protected void updateItemError(T item) {
        errorAlertText.set("Error occurred while updating item: " + item.toString());
        errorAlertTriggered.set(true);
    }
}
