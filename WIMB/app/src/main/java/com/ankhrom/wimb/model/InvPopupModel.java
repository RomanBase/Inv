package com.ankhrom.wimb.model;


import android.databinding.ViewDataBinding;

import com.ankhrom.base.model.PopupModel;
import com.ankhrom.wimb.BR;

public abstract class InvPopupModel<T extends ViewDataBinding> extends PopupModel<T> {

    @Override
    public int getVariableBindingResource() {
        return BR.PM;
    }
}
