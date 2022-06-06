package com.sev4ikwasd.tablewithaccept.ui;

import de.saxsys.mvvmfx.utils.mapping.ModelWrapper;

/**
 * Abstract class wrapping item of table.
 *
 * @param <T> Model class
 */
public abstract class TableItemViewModel<T> {
    /**
     * Wrapper of the Model item, must be used to wrap all fields that should be in the table,
     * see {@link de.saxsys.mvvmfx.utils.mapping.ModelWrapper} for details.
     */
    protected final ModelWrapper<T> wrapper = new ModelWrapper<>();

    /**
     * @param item initial item
     */
    public TableItemViewModel(T item) {
        wrapper.set(item);
        wrapper.reload();
    }

    /**
     * @return item
     */
    public T getItem() {
        return wrapper.get();
    }

    /**
     * @return {@code true} if value was changed but not accepted
     */
    public boolean isChanged() {
        return wrapper.isDifferent();
    }

    /**
     * Cancels all changes that were made to item before accept.
     */
    public void cancel() {
        wrapper.reload();
    }

    /**
     * Validates changes of the item and accepts them if changes are valid.
     *
     * @return {@code true} if changes were accepted
     */
    public boolean accept() {
        if (validate()) {
            wrapper.commit();
            return true;
        }
        return false;
    }

    /**
     * Validates changes in item.
     *
     * @return {@code true} if changes to value are valid
     */
    protected abstract boolean validate();
}
