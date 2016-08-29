package com.ankhrom.wimb;

import android.Manifest;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ankhrom.base.Base;
import com.ankhrom.base.BaseActivityDrawer;
import com.ankhrom.base.common.BasePermission;
import com.ankhrom.base.interfaces.viewmodel.ViewModelObserver;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.fire.FireSignIn;
import com.ankhrom.gcm.GcmPrefs;
import com.ankhrom.gcm.GcmRegistration;
import com.ankhrom.gcm.GcmRegistrationReceiver;
import com.ankhrom.gcm.PlayService;
import com.ankhrom.wimb.fire.FireUserStateListener;
import com.ankhrom.wimb.fire.TokenChangedListener;
import com.ankhrom.wimb.gcm.GeoBooResultReceiver;
import com.ankhrom.wimb.viewmodel.sidemenu.MenuModel;
import com.ankhrom.wimb.viewmodel.user.LoginSplashViewModel;

public class MainActivity extends BaseActivityDrawer {

    private final FireFactory factory = new FireFactory(FireSignIn.init());
    private final GeoBooResultReceiver geoResultReceiver = new GeoBooResultReceiver(this);
    //private final MainLayoutObserver layoutObserver = new MainLayoutObserver(this);

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

    @Override
    protected void onPostInit(Bundle state, ViewDataBinding binding) {
        super.onPostInit(state, binding);

        GcmPrefs prefs = new GcmPrefs(this);
        prefs.shared.registerOnSharedPreferenceChangeListener(new TokenChangedListener(factory));

        if (!prefs.getTokenStatus()) {
            new GcmRegistrationReceiver().register(this);
            if (PlayService.isAvailable(this)) {
                startService(new Intent(this, GcmRegistration.class));
            }
        }

        BasePermission.with(this)
                .require(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                        //"android.permission.READ_EXTERNAL_STORAGE"
                );
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
    protected void onResume() {
        super.onResume();

        geoResultReceiver.register();
    }

    @Override
    protected void onPause() {
        super.onPause();

        geoResultReceiver.unregister();
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
