package com.dontpanic.base.viewmodel;

import android.databinding.ViewDataBinding;

import com.dontpanic.base.interfaces.viewmodel.NetworkingViewModel;
import com.dontpanic.base.model.Model;

public abstract class BaseNetworkingViewModel<S extends ViewDataBinding, T extends Model, U> extends BaseViewModel<S, T> implements NetworkingViewModel<T, U> {

}
