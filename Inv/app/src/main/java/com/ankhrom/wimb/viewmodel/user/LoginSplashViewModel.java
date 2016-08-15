package com.ankhrom.wimb.viewmodel.user;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.fire.FireSignIn;
import com.ankhrom.wimb.entity.User;
import com.ankhrom.wimb.fire.FireUser;
import com.ankhrom.wimb.interfaces.ToolbarToggler;
import com.ankhrom.wimb.viewmodel.categories.CategoriesViewModel;
import com.ankhrom.wimb.R;

public class LoginSplashViewModel extends LoginViewModel implements ToolbarToggler {

    @Override
    protected void handleLoginError(FireSignIn.FireSignInVariant variant, Exception ex) {

        setDefaultViewModel(LoginViewModel.class);
    }

    @Override
    protected void onUserCredinals(@Nullable User data, @NonNull FireUser fireUser) {

        if (data == null || StringHelper.isEmpty(data.nickname)) {
            ViewModel vm = getFactory().getViewModel(LoginViewModel.class, fireUser);
            getNavigation().setViewModel(vm, false);
        } else {
            setDefaultViewModel(CategoriesViewModel.class);
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
