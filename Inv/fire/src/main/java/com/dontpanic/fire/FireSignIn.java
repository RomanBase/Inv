package com.dontpanic.fire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FireSignIn {

    final FirebaseAuth auth;

    private boolean isRegistered;

    private OnUserStateChangedListener userStatusListener;

    public FireSignIn(FirebaseAuth auth) {
        this.auth = auth;
    }

    private final FirebaseAuth.AuthStateListener fireAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            if (userStatusListener == null) {
                return;
            }

            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                userStatusListener.onFireUserSignedIn(user);
            } else {
                userStatusListener.onFireUserSignedOut();
            }
        }
    };

    public void setOnUserStateChangedListener(OnUserStateChangedListener listener) {
        this.userStatusListener = listener;
    }

    public void register() {

        if (isRegistered) {
            return;
        }

        isRegistered = true;
        auth.addAuthStateListener(fireAuthListener);
    }

    public void unregister() {

        isRegistered = false;
        auth.removeAuthStateListener(fireAuthListener);
    }

    public void checkGoogleSignIn(@NonNull GoogleSignIn googleSignIn) {

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleSignIn.getGoogleApiClient());
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            onGoogleSignIn(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    onGoogleSignIn(googleSignInResult);
                }
            });
        }
    }

    public void onGoogleSignIn(Intent data) {

        onGoogleSignIn(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
    }

    public void onGoogleSignIn(GoogleSignInResult result) {

        GoogleSignInAccount account = result.getSignInAccount();

        if (account == null) {
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.v("Fire", "on log complete " + task.isSuccessful());
                    }
                });
    }

    public void onFacebookSignIn(AccessToken access) {

        AuthCredential credential = FacebookAuthProvider.getCredential(access.getToken());

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.v("Fire", "on log complete " + task.isSuccessful());
                    }
                });
    }

    public interface OnUserStateChangedListener {

        void onFireUserSignedIn(FirebaseUser user);

        void onFireUserSignedOut();
    }
}
