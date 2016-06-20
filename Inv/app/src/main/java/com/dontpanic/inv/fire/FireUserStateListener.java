package com.dontpanic.inv.fire;

import com.dontpanic.base.Base;
import com.dontpanic.base.interfaces.viewmodel.ViewModelObserver;
import com.dontpanic.fire.FireSignIn;
import com.dontpanic.inv.FireFactory;
import com.dontpanicbase.inv.MainActivity;
import com.google.firebase.auth.FirebaseUser;

public class FireUserStateListener implements FireSignIn.OnUserStateChangedListener {

    final MainActivity activity;
    final FireFactory factory;

    public FireUserStateListener(MainActivity activity, FireFactory factory) {
        this.activity = activity;
        this.factory = factory;
    }

    @Override
    public void onFireUserSignedIn(FirebaseUser user) {

        factory.user = new FireUser(user);

        Base.logV("user log in");

        notifyViewModel(FireArgCode.USER_SIGNED_IN, factory.user);
    }

    @Override
    public void onFireUserSignedOut() {

        FireUser user = factory.user;
        factory.user = null;

        Base.logV("user log out");

        notifyViewModel(FireArgCode.USER_SIGNED_OUT, user);
    }

    private void notifyViewModel(int state, FireUser user) {

        ViewModelObserver observer = activity.getViewModelObserver();

        if (observer != null) {
            observer.postArgsToViewModel(state, user);
        }
    }
}
