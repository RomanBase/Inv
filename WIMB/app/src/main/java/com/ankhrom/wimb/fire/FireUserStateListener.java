package com.ankhrom.wimb.fire;

import com.ankhrom.base.Base;
import com.ankhrom.base.interfaces.viewmodel.ViewModelObserver;
import com.ankhrom.fire.FireSignIn;
import com.ankhrom.wimb.FireFactory;
import com.ankhrom.wimb.MainActivity;
import com.google.firebase.auth.FirebaseUser;

public class FireUserStateListener implements FireSignIn.OnUserStateChangedListener {

    private final MainActivity activity;
    private final FireFactory factory;

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

    @Override
    public void onFireUserError(FireSignIn.FireSignInVariant variant, Exception ex) {

        Base.logE("user log error - " + variant);

        ViewModelObserver observer = activity.getViewModelObserver();

        if (observer != null) {
            observer.postArgsToViewModel(FireArgCode.USER_SIGNIN_ERROR, variant, ex);
        }
    }

    private void notifyViewModel(int state, FireUser user) {

        ViewModelObserver observer = activity.getViewModelObserver();

        if (observer != null) {
            observer.postArgsToViewModel(state, user);
        }
    }
}
