package com.dontpanic.inv.viewmodel.user;

import android.content.Intent;
import android.view.View;

import com.dontpanic.base.interfaces.viewmodel.ViewModel;
import com.dontpanic.base.model.Model;
import com.dontpanic.base.viewmodel.BaseViewModelObserver;
import com.dontpanic.fire.FacebookSignIn;
import com.dontpanic.fire.GoogleSignIn;
import com.dontpanic.inv.FireFactory;
import com.dontpanic.inv.fire.FireArgCode;
import com.dontpanic.inv.viewmodel.InvViewModel;
import com.dontpanic.inv.viewmodel.categories.CategoriesViewModel;
import com.dontpanicbase.inv.R;
import com.dontpanicbase.inv.databinding.LoginPageBinding;

public class LoginViewModel extends InvViewModel<LoginPageBinding, Model> {

    @Override
    public void onInit() {
        super.onInit();

        setTitle("login");
    }

    public void onFacebookPressed(View view) {

        getFireFactory().facebookSignIn = FacebookSignIn.with(getContextAsActivity())
                .log(getFireFactory().signIn);
    }

    public void onGooglePressed(View view) {

        FireFactory factory = getFireFactory();

        if (factory.googleSignIn != null) {
            factory.googleSignIn.log(FireArgCode.FIRE_GOOGLE_SIGN_IN);
        } else {
            factory.googleSignIn = GoogleSignIn.with(getContextAsActivity())
                    .client(R.string.gplus_client_id)
                    .log(FireArgCode.FIRE_GOOGLE_SIGN_IN);
        }
    }

    public void onLogoutPressed(View view) {

        getFireFactory().auth.signOut();
    }

    @Override
    public boolean onBaseActivityResult(int requestCode, int resultCode, Intent data) {

        FireFactory factory = getFireFactory();

        if (factory.facebookSignIn != null) {
            factory.facebookSignIn.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == FireArgCode.FIRE_GOOGLE_SIGN_IN) {

            factory.signIn.onGoogleSignIn(data);
        }

        return false;
    }

    @Override
    public void onReceiveArgs(int requestCode, Object[] args) {

        if (requestCode == FireArgCode.USER_SIGNED_IN) {

            BaseViewModelObserver observer = getObserver();
            ViewModel vm = getFactory().getViewModel(CategoriesViewModel.class);

            if (vm != null) {
                observer.setDefaultViewModel(vm);
                observer.notifyViewModelChanged();
            }
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.login_page;
    }

}
