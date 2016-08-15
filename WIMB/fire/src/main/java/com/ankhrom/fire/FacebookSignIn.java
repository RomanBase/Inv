package com.ankhrom.fire;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class FacebookSignIn {

    private final Activity activity;
    private final CallbackManager callbackManager;

    private FacebookSignIn(Activity activity, CallbackManager callbackManager) {
        this.activity = activity;
        this.callbackManager = callbackManager;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static FacebookSignIn with(Activity activity) {

        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(activity);
        }

        return new FacebookSignIn(activity, CallbackManager.Factory.create());
    }

    public FacebookSignIn log(final FireSignIn fireSignIn) {

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.v(FireSignIn.TAG, "facebook log in");
                fireSignIn.onFacebookSignIn(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

                Log.v(FireSignIn.TAG, "facebook log cancel");
            }

            @Override
            public void onError(FacebookException error) {

                Log.v(FireSignIn.TAG, "facebook log error");
                fireSignIn.notifyErrorStatusListener(FireSignIn.FireSignInVariant.facebook, error);
            }
        });

        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"));

        return this;
    }
}
