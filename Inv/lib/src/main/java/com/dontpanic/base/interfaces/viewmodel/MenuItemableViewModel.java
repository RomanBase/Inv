package com.dontpanic.base.interfaces.viewmodel;

import com.dontpanic.base.model.ToolbarItemModel;

public interface MenuItemableViewModel {

    /**
     * @return list of menu items (toolbar menu icons)
     */
    ToolbarItemModel[] getMenuItems();
}
