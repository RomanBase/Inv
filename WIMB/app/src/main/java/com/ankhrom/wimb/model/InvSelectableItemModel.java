package com.ankhrom.wimb.model;


import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.wimb.BR;

public abstract class InvSelectableItemModel extends SelectableItemModel {

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
