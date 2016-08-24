package com.ankhrom.wimb.viewmodel.user;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.fire.FireSignIn;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.entity.AppUserCredentials;
import com.ankhrom.wimb.fire.FireUser;
import com.ankhrom.wimb.interfaces.ToolbarToggler;
import com.ankhrom.wimb.viewmodel.dashboard.DashboardViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class LoginSplashViewModel extends LoginViewModel implements ToolbarToggler {

    @Override
    public void onInit() {
        super.onInit();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            handleLoginError(FireSignIn.FireSignInVariant.unknown, new ClassNotFoundException("no user"));
        }
    }

    @Override
    protected void handleLoginError(FireSignIn.FireSignInVariant variant, Exception ex) {

        setDefaultViewModel(LoginViewModel.class);
    }

    @Override
    protected void onUserCredentialsObtained(@Nullable AppUserCredentials data, @NonNull FireUser fireUser) {

        if (data == null || StringHelper.isEmpty(data.nickname)) {
            ViewModel vm = getFactory().getViewModel(LoginViewModel.class, fireUser);
            getNavigation().setViewModel(vm, false);
        } else {
            setDefaultViewModel(DashboardViewModel.class);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.login_splash_page;
    }

    @Override
    public State getToolbarState() {
        return State.COLLAPSED;
    }

    @Override
    public boolean animateStateChange(State state) {
        return false;
    }
}
