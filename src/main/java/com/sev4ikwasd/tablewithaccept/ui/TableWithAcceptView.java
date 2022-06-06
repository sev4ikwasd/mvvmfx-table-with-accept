package com.sev4ikwasd.tablewithaccept.ui;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstract View implementing accept and cancel functionality,
 * requires {@link javafx.scene.control.TableView} with id {@code tableView} to be present in FXML.
 *
 * @param <T> Model class
 * @param <U> wrapped Model class
 * @param <V> ViewModel for View
 */
public abstract class TableWithAcceptView<T, U extends TableItemViewModel<T>, V extends RepositoryTableWithAcceptViewModel<T, U>> implements FxmlView<V>, Initializable {
    /**
     * Table containing objects of wrapped Model class.
     */
    @FXML
    protected TableView<U> tableView;

    /**
     * ViewModel object.
     */
    @InjectViewModel
    protected V viewModel;

    /**
     * @see javafx.fxml.Initializable
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.setItems(viewModel.getList());

        viewModel.selectedTableRowProperty().bind(tableView.getSelectionModel().selectedItemProperty());
        viewModel.setOnSelect(vm -> tableView.getSelectionModel().select(vm));
    }

    /**
     * Add item to ViewModel.
     */
    @FXML
    public void add(ActionEvent event) {
        viewModel.add();
    }

    /**
     * Remove item from ViewModel.
     */
    @FXML
    public void remove(ActionEvent event) {
        viewModel.remove();
    }

    /**
     * Accept changes in ViewModel.
     */
    @FXML
    public void accept(ActionEvent event) {
        viewModel.accept();
    }

    /**
     * Cancel changes in ViewModel.
     */
    @FXML
    public void cancel(ActionEvent event) {
        viewModel.cancel();
    }
}
