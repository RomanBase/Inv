package com.ankhrom.wimb.fire;

import android.support.annotation.Nullable;

import com.ankhrom.base.Base;
import com.ankhrom.base.interfaces.viewmodel.ViewModelObserver;
import com.ankhrom.fire.FireData;
import com.ankhrom.fire.FireSignIn;
import com.ankhrom.wimb.FireFactory;
import com.ankhrom.wimb.MainActivity;
import com.ankhrom.wimb.entity.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class FireUserStateListener implements FireSignIn.OnUserStateChangedListener {

    private final MainActivity activity;
    private final FireFactory factory;

    private DatabaseReference userReference;

    public FireUserStateListener(MainActivity activity, FireFactory factory) {
        this.activity = activity;
        this.factory = factory;
    }

    private final FireValueListener<User> userDataListener = new FireValueListener<User>(User.class) {
        @Override
        public void onDataChanged(@Nullable User data) {
            factory.activeUser = data;
            notifyViewModelUser(data);
        }
    };

    @Override
    public void onFireUserSignedIn(FirebaseUser user) {

        factory.user = new FireUser(user);

        if (userReference != null) {
            userReference.removeEventListener(userDataListener);
            userReference = null;
        }

        userReference = FireData.init().root(User.KEY).get(user.getUid());
        userReference.addValueEventListener(userDataListener);

        Base.logV("user log in");

        notifyViewModelFireUser(FireArgCode.USER_SIGNED_IN, factory.user);
    }

    @Override
    public void onFireUserSignedOut() {

        FireUser user = factory.user;
        factory.user = null;
        factory.activeUser = null;

        if (userReference != null) {
            userReference.removeEventListener(userDataListener);
            userReference = null;
        }

        Base.logV("user log out");

        notifyViewModelFireUser(FireArgCode.USER_SIGNED_OUT, user);
    }

    @Override
    public void onFireUserError(FireSignIn.FireSignInVariant variant, Exception ex) {

        Base.logE("user log error - " + variant);

        ViewModelObserver observer = activity.getViewModelObserver();

        if (observer != null) {
            observer.postArgsToViewModel(FireArgCode.USER_SIGNIN_ERROR, variant, ex);
        }
    }

    private void notifyViewModelFireUser(int state, FireUser user) {

        ViewModelObserver observer = activity.getViewModelObserver();

        if (observer != null) {
            observer.postArgsToViewModel(state, user);
        }
    }

    private void notifyViewModelUser(User user) {

        ViewModelObserver observer = activity.getViewModelObserver();

        if (observer != null) {
            observer.postArgsToViewModel(FireArgCode.USER_DATA_CHANGED, user);
        }
    }
}
