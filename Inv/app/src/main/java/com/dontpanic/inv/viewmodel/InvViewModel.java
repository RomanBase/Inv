package com.dontpanic.inv.viewmodel;

import android.databinding.ViewDataBinding;

import com.dontpanic.base.model.Model;
import com.dontpanic.base.viewmodel.BaseViewModel;
import com.dontpanic.inv.FireFactory;
import com.dontpanicbase.inv.BR;
import com.dontpanicbase.inv.MainActivity;

public abstract class InvViewModel<S extends ViewDataBinding, T extends Model> extends BaseViewModel<S, T> {

    public MainActivity getContextAsActivity() {

        return (MainActivity) getContext();
    }

    public FireFactory getFireFactory() {

        return getFactory();
    }

    @Override
    public int getBindingResource() {
        return BR.VM;
    }
}
