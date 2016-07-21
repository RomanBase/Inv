package com.dontpanic.inv.viewmodel.user;


import android.view.View;

import com.dontpanic.inv.viewmodel.InvViewModel;
import com.dontpanicbase.inv.R;

public class LoginFireViewModel extends InvViewModel {

    public void onLoginPressed(View view) {

        getFireFactory().signIn.withFirebase("ahoj@ahoj.ahoj", "123456");
    }

    public void onCreatePressed(View view) {

        getFireFactory().signIn.withFirebaseRegistration("ahoj@ahoj.ahoj", "123456");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.login_fire_page;
    }
}
