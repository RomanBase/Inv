package com.dontpanic.base.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.dontpanic.base.common.statics.FragmentHelper;
import com.dontpanic.base.custom.builder.FragmentTransactioner;
import com.dontpanic.base.interfaces.ActivityEventListener;
import com.dontpanic.base.interfaces.ObjectFactory;
import com.dontpanic.base.interfaces.OnActivityStateChangedListener;
import com.dontpanic.base.interfaces.PopupModelAdapter;
import com.dontpanic.base.interfaces.viewmodel.AnimableTransitionViewModel;
import com.dontpanic.base.interfaces.viewmodel.AnimableViewModel;
import com.dontpanic.base.interfaces.viewmodel.MenuViewModel;
import com.dontpanic.base.interfaces.viewmodel.OnBaseChangedListener;
import com.dontpanic.base.interfaces.viewmodel.ViewModel;
import com.dontpanic.base.interfaces.viewmodel.ViewModelNavigation;
import com.dontpanic.base.interfaces.viewmodel.ViewModelObserver;
import com.dontpanic.base.networking.volley.VolleyBuilder;

public class BaseViewModelObserver implements ViewModelObserver, ViewModelNavigation, ActivityEventListener {

    private ViewModel currentViewModel;
    private MenuViewModel menuViewModel;
    private ObjectFactory objectFactory;
    private PopupModelAdapter popupAdapter;
    private RequestQueue requestQueue;

    private Context context;
    private ViewGroup rootView;
    private ViewModel defaultViewModel;

    private int rootContainerId;

    private OnBaseChangedListener baseChangeListener;

    private BaseViewModelObserver() {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDefaultViewModel(@NonNull ViewModel viewModel) {

        defaultViewModel = viewModel;
        currentViewModel = viewModel;

        currentViewModel.setNavigation(this);
        currentViewModel.onInit();
        currentViewModel.onViewStackChanged(false, true);
        currentViewModel.loadModel();

        FragmentTransactioner.with(context)
                .replaceView(currentViewModel.getFragment())
                .into(rootContainerId)
                .clearStack()
                .commit();
    }

    public void setDefaultMenuModel(MenuViewModel menuModel) {

        if (menuModel != null) {
            menuModel.setNavigation(this);
            menuModel.onInit();
            menuModel.onCreate();
        }

        menuViewModel = menuModel;
    }

    public void setFactory(ObjectFactory factory) {
        this.objectFactory = factory;
    }

    public void setPopupAdapter(PopupModelAdapter popupAdapter) {
        this.popupAdapter = popupAdapter;
    }

    public void setRootViewContainer(ViewGroup rootView) {

        this.rootView = rootView;

        if (popupAdapter != null) {
            popupAdapter.init(context, rootView);
        }
    }

    public void setRootContainerId(@IdRes int viewId) {
        this.rootContainerId = viewId;
    }

    public void setBaseChangedListener(OnBaseChangedListener modelChangeListener) {
        this.baseChangeListener = modelChangeListener;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    private void initViewModel(ViewModel viewModel, boolean visibleStack) {

        if (currentViewModel != null) {
            currentViewModel.onViewStackChanged(true, visibleStack);
        }

        currentViewModel = viewModel;
        currentViewModel.setNavigation(this);
        currentViewModel.onInit();
        currentViewModel.loadModel();
        notifyViewModelChanged();
    }

    private void commitViewModel(FragmentTransactioner.FragmentCommitAnim fragment, ViewModel viewModel) {

        if (viewModel instanceof AnimableViewModel) {
            fragment.enterAndExit((AnimableViewModel) viewModel)
                    .commit();
        } else if (viewModel instanceof AnimableTransitionViewModel) {
            AnimableTransitionViewModel avm = (AnimableTransitionViewModel) viewModel;
            fragment.transition(avm.getTransition())
                    .commit();
        } else {
            fragment.commit();
        }
    }

    @Override
    public void setViewModel(@NonNull ViewModel viewModel, Boolean clearBackStack) {

        initViewModel(viewModel, false);

        FragmentTransactioner.FragmentCommitAnim fragment = FragmentTransactioner.with(context)
                .replacePage(currentViewModel.getFragment())
                .into(rootContainerId);

        commitViewModel(fragment, viewModel);
    }

    @Override
    public void addViewModel(@NonNull ViewModel viewModel) {

        initViewModel(viewModel, true);

        FragmentTransactioner.FragmentCommitAnim fragment = FragmentTransactioner.with(context)
                .addPage(currentViewModel.getFragment())
                .into(rootContainerId);

        commitViewModel(fragment, viewModel);
    }

    @Override
    public boolean onBaseBackPressed() {

        if (popupAdapter != null && popupAdapter.isActive()) {
            popupAdapter.hide(popupAdapter.getCurrentPopupModel());
            return true;
        }

        if (currentViewModel != null) {
            if (currentViewModel.onBackPressed()) {
                return true;
            }

            if (defaultViewModel.getClass().isInstance(currentViewModel)) {
                return false;
            }
        }

        if (!defaultViewModel.getClass().isInstance(currentViewModel)) {
            Fragment fragment = currentViewModel.getFragment();
            setPreviousViewModel();
        }

        return false;
    }

    @Override
    public void setPreviousViewModel() {

        int count = FragmentHelper.getBackStackLength(context);

        if (count <= 1) {
            currentViewModel = defaultViewModel;
        } else {
            Fragment fragment = FragmentHelper.getPreviousStackFragment(context);
            if (fragment != null && fragment instanceof ViewModel) {
                currentViewModel = (ViewModel) fragment;
            } else {
                return;
            }
        }

        currentViewModel.onViewStackChanged(false, true);

        if (!currentViewModel.isModelLoaded()) {
            currentViewModel.loadModel();
        }

        notifyViewModelChanged();
    }

    @Override
    public void notifyViewModelChanged() {

        if (baseChangeListener != null) {
            baseChangeListener.onViewModelChanged(currentViewModel);
        }
    }

    @Override
    public void navigateBack() {

        ((Activity) context).onBackPressed();
    }

    @Override
    public void onResume() {

        if (currentViewModel != null && currentViewModel instanceof OnActivityStateChangedListener) {
            ((OnActivityStateChangedListener) currentViewModel).onActivityResume();
        }
    }

    @Override
    public void onPause() {

        if (currentViewModel != null && currentViewModel instanceof OnActivityStateChangedListener) {
            ((OnActivityStateChangedListener) currentViewModel).onActivityPause();
        }
    }

    @Override
    public boolean onBaseActivityResult(int requestCode, int resultCode, Intent data) {

        return currentViewModel != null && currentViewModel.onBaseActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void postArgsToViewModel(int requestCode, Object... args) {

        if (currentViewModel != null) {
            currentViewModel.onReceiveArgs(requestCode, args);
        }
    }

    @Override
    public void postArgsToBase(int requestCode, Object... args) {

        if (baseChangeListener != null) {
            baseChangeListener.onReceiveArgs(requestCode, args);
        }
    }

    @Override
    public void notifyScreenOptions(int options) {

        if (baseChangeListener != null) {
            baseChangeListener.onScreenOptionsChanged(options);
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends MenuViewModel> T getMenuViewModel() {
        return (T) menuViewModel;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ObjectFactory> T getFactory() {
        return (T) objectFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PopupModelAdapter> T getPopupAdapter() {
        return (T) popupAdapter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModelObserver> T getObserver() {
        return (T) this;
    }

    @Override
    public RequestQueue getRequestQueue() {

        return requestQueue;
    }

    @Override
    public ViewModelNavigation getNavigation() {
        return this;
    }

    @Override
    public ActivityEventListener getEventListener() {
        return this;
    }

    @Override
    public ViewModel getCurrentViewModel() {
        return currentViewModel;
    }

    public ViewGroup getRootLayout() {
        return rootView;
    }

    public ViewModel getDefaultViewModel() {
        return defaultViewModel;
    }

    public View getCurrentView() {

        if (rootView == null || rootView.getChildCount() == 0) {
            return null;
        }

        return rootView.getChildAt(0);
    }

    public int getRootContainerId() {
        return rootContainerId;
    }

    public OnBaseChangedListener getBaseChangeListener() {
        return baseChangeListener;
    }


    public static class Builder {

        private final Context bContext;
        private final int bRootContainerId;
        private ViewModel bViewModel;
        private MenuViewModel bMenuModel;
        private ObjectFactory bFactory;
        private PopupModelAdapter bPopupAdapter;
        private RequestQueue bRequestQueue;

        public Builder(@NonNull Context context, @IdRes int rootContainerId) {
            this.bContext = context;
            this.bRootContainerId = rootContainerId;
        }

        public Builder setViewModel(ViewModel viewModel) {

            this.bViewModel = viewModel;
            return this;
        }

        public Builder setMenuModel(MenuViewModel menuModel) {

            this.bMenuModel = menuModel;
            return this;
        }

        public Builder setFactory(ObjectFactory factory) {

            this.bFactory = factory;
            return this;
        }

        public Builder setPopupAdapter(PopupModelAdapter popupAdapter) {

            this.bPopupAdapter = popupAdapter;
            return this;
        }

        public Builder setRequestQueue(RequestQueue requestQueue) {

            this.bRequestQueue = requestQueue;
            return this;
        }

        public ViewModelObserver build() {

            if (bRequestQueue == null) {
                bRequestQueue = new VolleyBuilder(bContext)
                        .cacheFile("base_cache")
                        .cacheSize(30)
                        .build();
            }

            if (bPopupAdapter == null) {
                bPopupAdapter = new BasePopupAdapter();
            }

            BaseViewModelObserver mvm = new BaseViewModelObserver();
            mvm.setContext(bContext);
            mvm.setRootContainerId(bRootContainerId);
            mvm.setFactory(bFactory);
            mvm.setPopupAdapter(bPopupAdapter);
            mvm.setRequestQueue(bRequestQueue);
            mvm.setDefaultMenuModel(bMenuModel);
            mvm.setDefaultViewModel(bViewModel);

            return mvm;
        }
    }
}
