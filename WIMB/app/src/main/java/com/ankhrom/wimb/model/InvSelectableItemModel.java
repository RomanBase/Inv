package com.ankhrom.wimb.model;


import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.wimb.BR;

public abstract class InvSelectableItemModel extends SelectableItemModel {

    public InvSelectableItemModel() {
    }

    public InvSelectableItemModel(OnItemSelectedListener itemSelectedListener) {
        super(itemSelectedListener);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
