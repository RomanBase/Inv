package com.ankhrom.wimb.viewmodel;

import android.databinding.ViewDataBinding;

import com.ankhrom.base.interfaces.viewmodel.NetworkingViewModel;
import com.ankhrom.base.model.Model;

public abstract class InvNetworkingViewModel<S extends ViewDataBinding, T extends Model, U> extends InvViewModel<S, T> implements NetworkingViewModel<T, U> {

}
