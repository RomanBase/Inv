package com.dontpanic.inv.viewmodel;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import com.dontpanic.base.interfaces.viewmodel.ViewModel;
import com.dontpanic.base.model.Model;
import com.dontpanic.base.viewmodel.BaseViewModel;
import com.dontpanic.base.viewmodel.BaseViewModelObserver;
import com.dontpanic.inv.FireFactory;
import com.dontpanicbase.inv.BR;
import com.dontpanicbase.inv.MainActivity;

public abstract class InvViewModel<S extends ViewDataBinding, T extends Model> extends BaseViewModel<S, T> {

    @Nullable
    public MainActivity getContextAsActivity() {

        Context context = getContext();

        if (context instanceof MainActivity) {
            return (MainActivity) getContext();
        }

        return null;
    }

    public FireFactory getFireFactory() {

        return getFactory();
    }

    protected void setDefaultViewModel(Class clazz) {

        BaseViewModelObserver observer = getObserver();
        ViewModel vm = getFactory().getViewModel(clazz);

        if (vm != null) {
            observer.setDefaultViewModel(vm);
            observer.notifyViewModelChanged();
        }
    }

    @Override
    public int getBindingResource() {
        return BR.VM;
    }
}
