package com.ankhrom.wimb.model;

import com.ankhrom.base.model.ItemModel;
import com.ankhrom.wimb.BR;


public abstract class InvItemModel extends ItemModel {

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
