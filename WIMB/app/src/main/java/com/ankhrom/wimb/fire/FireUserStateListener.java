package com.ankhrom.wimb.fire;

import android.support.annotation.Nullable;

import com.ankhrom.base.Base;
import com.ankhrom.base.interfaces.viewmodel.ViewModelObserver;
import com.ankhrom.fire.FireData;
import com.ankhrom.fire.FireSignIn;
import com.ankhrom.wimb.FireFactory;
import com.ankhrom.wimb.MainActivity;
import com.ankhrom.wimb.entity.AppUser;
import com.ankhrom.wimb.entity.AppUserCredentials;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class FireUserStateListener implements FireSignIn.OnUserStateChangedListener {

    private final MainActivity activity;
    private final FireFactory factory;

    private DatabaseReference userReference;
    private DatabaseReference credentialsReference;

    public FireUserStateListener(MainActivity activity, FireFactory factory) {
        this.activity = activity;
        this.factory = factory;
    }

    private final FireValueListener<AppUser> userDataListener = new FireValueListener<AppUser>(AppUser.class) {
        @Override
        public void onDataChanged(@Nullable AppUser data) {

            if (credentialsReference != null) {
                credentialsReference.removeEventListener(userCredentialsListener);
                credentialsReference = null;
            }

            factory.appUser = data;
            notifyViewModel(FireArgCode.USER_DATA_CHANGED, data);

            if (data != null) {
                credentialsReference = FireData.init().root(AppUser.CREDENTIALS).get(data.sid);
                credentialsReference.addValueEventListener(userCredentialsListener);
            }
        }
    };

    private final FireValueListener<AppUserCredentials> userCredentialsListener = new FireValueListener<AppUserCredentials>(AppUserCredentials.class) {
        @Override
        public void onDataChanged(@Nullable AppUserCredentials data) {
            factory.appUserCredentials = data;
            notifyViewModel(FireArgCode.USER_CREDENTIALS_CHANGED, data);
        }
    };

    @Override
    public void onFireUserSignedIn(FirebaseUser user) {

        factory.user = new FireUser(user);

        if (userReference != null) {
            userReference.removeEventListener(userDataListener);
            userReference = null;
        }

        userReference = FireData.init().root(AppUser.KEY).get(user.getUid());
        userReference.addValueEventListener(userDataListener);

        Base.logV("user log in");

        notifyViewModel(FireArgCode.USER_SIGNED_IN, factory.user);
    }

    @Override
    public void onFireUserSignedOut() {

        FireUser user = factory.user;
        factory.user = null;
        factory.appUser = null;

        if (userReference != null) {
            userReference.removeEventListener(userDataListener);
            userReference = null;
        }

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

    private void notifyViewModel(int state, Object object) {

        ViewModelObserver observer = activity.getViewModelObserver();

        if (observer != null) {
            observer.postArgsToViewModel(state, object);
        }
    }
}
