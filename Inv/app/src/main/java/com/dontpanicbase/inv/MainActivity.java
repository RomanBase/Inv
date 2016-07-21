package com.dontpanicbase.inv;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.dontpanic.base.Base;
import com.dontpanic.base.BaseActivityDrawer;
import com.dontpanic.base.interfaces.viewmodel.ViewModelObserver;
import com.dontpanic.base.viewmodel.BaseViewModelObserver;
import com.dontpanic.fire.FireSignIn;
import com.dontpanic.inv.FireFactory;
import com.dontpanic.inv.fire.FireUserStateListener;
import com.dontpanic.inv.viewmodel.sidemenu.MenuModel;
import com.dontpanic.inv.viewmodel.user.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;

public class MainActivity extends BaseActivityDrawer {

    private FireFactory factory;

    @Override
    protected boolean onPreInit() {

        Base.debug = true;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FireSignIn fireSignIn = new FireSignIn(auth);

        factory = new FireFactory(auth, fireSignIn);
        factory.signIn.setOnUserStateChangedListener(new FireUserStateListener(this, factory));
        factory.signIn.register();

        return super.onPreInit();
    }

    @Override
    protected ViewModelObserver init() {

        return BaseViewModelObserver.with(this, R.id.root_container)
                .setScreenRootContainerId(R.id.screen_root_container)
                .setFactory(factory)
                .setViewModel(factory.getViewModel(LoginViewModel.class))
                .setMenuModel(new MenuModel(this, R.id.root_menu_container))
                .build();
    }

    BottomBar bar;

    @Override
    protected void onPostInit(Bundle state, ViewDataBinding binding) {
        super.onPostInit(state, binding);

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (bar != null) {
            bar.onSaveInstanceState(outState);
        }
    }

    @Override
    protected int getMainLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (factory == null) {
            return;
        }

        factory.signIn.register();
        if (factory.googleSignIn != null) {
            factory.signIn.checkGoogleSignIn(factory.googleSignIn);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (factory == null) {
            return;
        }

        factory.signIn.unregister();
    }
}
