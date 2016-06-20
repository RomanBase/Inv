package com.dontpanic.base.interfaces.viewmodel;

import android.content.Context;

import com.android.volley.RequestQueue;

import com.dontpanic.base.interfaces.ActivityEventListener;
import com.dontpanic.base.interfaces.ObjectFactory;
import com.dontpanic.base.interfaces.PopupModelAdapter;

public interface ViewModelObserver {

    void postArgsToViewModel(int requestCode, Object... args);

    void postArgsToBase(int requestCode, Object... args);

    void notifyScreenOptions(int options);

    void notifyViewModelChanged();

    <T extends MenuViewModel> T getMenuViewModel();

    <T extends ObjectFactory> T getFactory();

    <T extends PopupModelAdapter> T getPopupAdapter();

    RequestQueue getRequestQueue();

    ViewModel getCurrentViewModel();

    Context getContext();

    ViewModelNavigation getNavigation();

    ActivityEventListener getEventListener();
}
