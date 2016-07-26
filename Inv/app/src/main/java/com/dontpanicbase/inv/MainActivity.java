package com.dontpanicbase.inv;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;

import com.dontpanic.base.Base;
import com.dontpanic.base.BaseActivityDrawer;
import com.dontpanic.base.common.statics.ViewHelper;
import com.dontpanic.base.interfaces.viewmodel.ViewModel;
import com.dontpanic.base.interfaces.viewmodel.ViewModelObserver;
import com.dontpanic.base.viewmodel.BaseViewModelObserver;
import com.dontpanic.fire.FireSignIn;
import com.dontpanic.inv.FireFactory;
import com.dontpanic.inv.fire.FireUserStateListener;
import com.dontpanic.inv.interfaces.ToolbarToggler;
import com.dontpanic.inv.viewmodel.sidemenu.MenuModel;
import com.dontpanic.inv.viewmodel.user.LoginSplashViewModel;
import com.dontpanicbase.inv.databinding.ActivityCollapsingToolbarBinding;
import com.dontpanicbase.inv.databinding.ActivityMainBinding;
import com.roughike.bottombar.BottomBar;

public class MainActivity extends BaseActivityDrawer {

    private final FireFactory factory = new FireFactory(FireSignIn.init());
    private final MainLayoutObserver layoutObserver = new MainLayoutObserver(this);

    @Override
    protected boolean onPreInit() {

        Base.debug = true;

        factory.signIn.setOnUserStateChangedListener(new FireUserStateListener(this, factory));
        factory.signIn.register();

        return super.onPreInit();
    }

    @Override
    protected ViewModelObserver init() {

        return BaseViewModelObserver.with(this, R.id.root_container)
                .setScreenRootContainerId(R.id.screen_root_container)
                .setFactory(factory)
                .setViewModel(factory.getViewModel(LoginSplashViewModel.class))
                .setMenuModel(new MenuModel(this, R.id.root_menu_container))
                .build();
    }

    BottomBar bar;

    @Override
    protected void onPostInit(Bundle state, ViewDataBinding binding) {
        super.onPostInit(state, binding);

        ActivityCollapsingToolbarBinding toolbarBinding = DataBindingUtil.findBinding(ViewHelper.findChildView(AppBarLayout.class, binding.getRoot()));

        layoutObserver.bind((ActivityMainBinding) binding, toolbarBinding);

        binding.getRoot().post(new Runnable() {
            @Override
            public void run() {
                getViewModelObserver().notifyViewModelChanged();
            }
        });

        /*bar = BottomBar.attach((CoordinatorLayout) findViewById(R.id.root_coordinator), state);
        bar.noTopOffset();
        bar.noNavBarGoodness();

        bar.setItems(R.menu.menu_bottom);
        bar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {

            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        bar.mapColorForTab(0, "#AAFF0000");
        bar.mapColorForTab(1, "#AAFFFF00");
        bar.mapColorForTab(2, "#AAFF00FF");
        bar.mapColorForTab(3, "#AA00FF00");
        bar.mapColorForTab(4, "#AA00FFFF");*/
    }

    @Override
    public void onViewModelChanged(ViewModel viewModel) {
        super.onViewModelChanged(viewModel);

        if (viewModel instanceof ToolbarToggler) {
            layoutObserver.toggleToolbar((ToolbarToggler) viewModel);
        } else {
            layoutObserver.setDefaultToolbarState();
        }
    }

    @Override
    protected void setTitle(String title) {

        layoutObserver.title.set(title);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (bar != null) {
            bar.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        factory.signIn.register();
        if (factory.googleSignIn != null) {
            factory.signIn.checkGoogleSignIn(factory.googleSignIn);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        factory.signIn.unregister();
    }

    @Override
    protected int getMainLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }
}
