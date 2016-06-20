package com.dontpanic.base.interfaces.viewmodel;

public interface SearchableViewModel {

    /**
     * populate search query from toolbars searchview
     */
    boolean performSearch(String query, boolean submit);
}
