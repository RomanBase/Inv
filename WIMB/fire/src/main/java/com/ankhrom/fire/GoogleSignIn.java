package com.ankhrom.fire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleSignIn {

    private final FragmentActivity activity;

    GoogleApiClient apiClient;

    private GoogleSignIn(FragmentActivity activity) {
        this.activity = activity;
    }

    public GoogleApiClient getGoogleApiClient() {

        return apiClient;
    }

    public void log(int requestCode) {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        activity.startActivityForResult(signInIntent, requestCode);
    }

    public static GoogleSignInClient with(FragmentActivity activity) {

        return with(activity, null);
    }

    public static GoogleSignInClient with(FragmentActivity activity, GoogleApiClient apiClient) {

        GoogleSignIn signIn = new GoogleSignIn(activity);
        signIn.apiClient = apiClient;

        return new GoogleSignInClient(signIn);
    }

    public static class GoogleSignInClient {

        final GoogleSignIn signIn;

        GoogleSignInClient(GoogleSignIn signIn) {
            this.signIn = signIn;
        }

        public GoogleSignInRequest client(int resId) {

            return client(signIn.activity.getResources().getString(resId));
        }

        public GoogleSignInRequest client(String id) {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(id)
                    .requestEmail()
                    .build();

            return log(gso);
        }

        private GoogleSignInRequest log(GoogleSignInOptions gso) {

            if (signIn.apiClient == null) {

                signIn.apiClient = new GoogleApiClient.Builder(signIn.activity)
                        .enableAutoManage(signIn.activity, new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Log.e(FireSignIn.TAG, "Google plus connection error");
                            }
                        })
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            }

            return new GoogleSignInRequest(this);
        }
    }

    public static class GoogleSignInRequest {

        final GoogleSignInClient client;

        GoogleSignInRequest(GoogleSignInClient client) {
            this.client = client;
        }

        public GoogleSignIn log(int requestCode) {

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(client.signIn.apiClient);
            client.signIn.activity.startActivityForResult(signInIntent, requestCode);

            return client.signIn;
        }

        public GoogleSignIn get() {

            return client.signIn;
        }
    }
}
