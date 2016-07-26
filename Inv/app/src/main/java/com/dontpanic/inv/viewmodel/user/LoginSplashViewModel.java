package com.dontpanic.inv.viewmodel.user;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dontpanic.base.common.statics.StringHelper;
import com.dontpanic.base.interfaces.viewmodel.ViewModel;
import com.dontpanic.fire.FireSignIn;
import com.dontpanic.inv.entity.User;
import com.dontpanic.inv.fire.FireUser;
import com.dontpanic.inv.interfaces.ToolbarToggler;
import com.dontpanic.inv.viewmodel.categories.CategoriesViewModel;
import com.dontpanicbase.inv.R;

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
