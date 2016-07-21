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

    public static final String TAG = "Fire";

    public static enum FireSignInVariant {
        facebook,
        gplus,
        twitter,
        github,
        firebase_login,
        firebase_registration,
        custom
    }

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
            GoogleSignInResult result = opr.get();
            onGoogleSignIn(result);
        } else {
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

    public void onGoogleSignIn(@NonNull GoogleSignInResult result) {

        GoogleSignInAccount account = result.getSignInAccount();

        if (account == null) {
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.v(TAG, "on google log complete " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            notifyErrorStatusListener(FireSignInVariant.gplus, task.getException());
                        }
                    }
                });
    }

    public void onFacebookSignIn(@NonNull AccessToken access) {

        AuthCredential credential = FacebookAuthProvider.getCredential(access.getToken());

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.v(TAG, "on facebook log complete " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            notifyErrorStatusListener(FireSignInVariant.facebook, task.getException());
                        }
                    }
                });
    }

    public void withFirebase(@NonNull String email, @NonNull String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.v(TAG, "on firebase log complete " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            notifyErrorStatusListener(FireSignInVariant.firebase_login, task.getException());
                        }
                    }
                });
    }

    public void withFirebaseRegistration(@NonNull String email, @NonNull String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.v(TAG, "on register complete " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            notifyErrorStatusListener(FireSignInVariant.firebase_registration, task.getException());
                        }
                    }
                });
    }

    public void notifyErrorStatusListener(FireSignInVariant variant, Exception ex) {

        if (userStatusListener == null) {
            return;
        }

        userStatusListener.onFireUserError(variant, ex);
    }

    public interface OnUserStateChangedListener {

        void onFireUserSignedIn(FirebaseUser user);

        void onFireUserSignedOut();

        void onFireUserError(FireSignInVariant variant, Exception ex);
    }
}
