package com.dontpanic.inv.viewmodel;

import android.databinding.ViewDataBinding;

import com.dontpanic.base.interfaces.viewmodel.NetworkingViewModel;
import com.dontpanic.base.model.Model;

public abstract class InvNetworkingViewModel<S extends ViewDataBinding, T extends Model, U> extends InvViewModel<S, T> implements NetworkingViewModel<T, U> {

}
