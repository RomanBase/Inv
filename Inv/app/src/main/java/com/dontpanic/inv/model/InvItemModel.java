package com.dontpanic.inv.model;

import com.dontpanic.base.model.ItemModel;
import com.dontpanicbase.inv.BR;


public abstract class InvItemModel extends ItemModel {

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
