package com.dontpanic.inv.model;


import com.dontpanic.base.model.SelectableItemModel;
import com.dontpanicbase.inv.BR;

public abstract class InvSelectableItemModel extends SelectableItemModel {

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
