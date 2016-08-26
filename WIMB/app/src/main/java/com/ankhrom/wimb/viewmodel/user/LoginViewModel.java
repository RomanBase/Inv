package com.ankhrom.wimb.viewmodel.user;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.fire.FacebookSignIn;
import com.ankhrom.fire.FireSignIn;
import com.ankhrom.fire.GoogleSignIn;
import com.ankhrom.wimb.FireFactory;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.LoginPageBinding;
import com.ankhrom.wimb.entity.AppUser;
import com.ankhrom.wimb.entity.AppUserCredentials;
import com.ankhrom.wimb.fire.FireArgCode;
import com.ankhrom.wimb.fire.FireUser;
import com.ankhrom.wimb.fire.FireValueListener;
import com.ankhrom.wimb.model.user.LoginModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.ankhrom.wimb.viewmodel.dashboard.DashboardViewModel;
import com.google.firebase.database.DatabaseError;

public class LoginViewModel extends InvViewModel<LoginPageBinding, LoginModel> {

    public final TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                onAccountPressed(null);
                return true;
            }

            return false;
        }
    };

    @Override
    public void onInit() {
        super.onInit();

        setModel(new LoginModel());
    }

    @Override
    public boolean onBackPressed() {

        if (model != null && model.showRegisterFields.get()) {
            model.showRegisterFields.set(false);
            return true;
        }

        if (model != null && model.showLoginFields.get()) {
            model.showLoginFields.set(false);
            return true;
        }

        return super.onBackPressed();
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

    public void onAccountPressed(View view) {

        boolean isFireLoginActive = model.showLoginFields.get();
        boolean isFireSignInActive = model.showRegisterFields.get();

        if (isFireLoginActive) {

            boolean isValid = true;
            if (StringHelper.isEmpty(model.email.get())) {
                isValid = false;
            }

            if (StringHelper.isEmpty(model.password.get())) {
                isValid = false;
            }

            if (isValid) {
                if (isFireSignInActive) {
                    registerWithFirebase();
                } else {
                    logInWithFirebase();
                }
            }

        } else {
            model.showLoginFields.set(true);
        }
    }

    public void onSignInPressed(View view) {

        model.showRegisterFields.set(true);
    }

    private void logInWithFirebase() {

        getFireFactory().signIn.withFirebase(model.email.get(), model.password.get());
    }

    private void registerWithFirebase() {

        getFireFactory().signIn.withFirebaseRegistration(model.email.get(), model.password.get());
    }

    private void checkCredinals(String uid) {

        final FireUser fireUser = getFireFactory().user;

        if (fireUser == null || isLoading.get()) {
            return;
        }

        isLoading.set(true);

        getFireData()
                .root(AppUser.KEY)
                .listener(new FireValueListener<AppUser>(AppUser.class) {

                    @Override
                    public void onDataChanged(@Nullable AppUser data) {

                        if (data == null || StringHelper.isEmpty(data.sid)) {
                            onUserCredentialsObtained(null, !StringHelper.isEmpty(getUid()));
                            isLoading.set(false);
                            return;
                        }

                        getFireData()
                                .root(AppUser.CREDENTIALS)
                                .listener(new FireValueListener<AppUserCredentials>(AppUserCredentials.class) {
                                    @Override
                                    public void onDataChanged(@Nullable AppUserCredentials data) {

                                        onUserCredentialsObtained(data, !StringHelper.isEmpty(getUid()));
                                        isLoading.set(false);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                        handleLoginError(FireSignIn.FireSignInVariant.unknown, databaseError.toException());
                                        isLoading.set(false);
                                    }
                                })
                                .get(data.sid);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        handleLoginError(FireSignIn.FireSignInVariant.unknown, databaseError.toException());
                        isLoading.set(false);
                    }
                })
                .get(uid);
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

    protected void onUserCredentialsObtained(@Nullable AppUserCredentials data, boolean isLoged) {

        if (data == null || StringHelper.isEmpty(data.nickname)) {
            ViewModel vm = getFactory().getViewModel(LoginCredinalsViewModel.class);
            getNavigation().setViewModel(vm, false);
        } else {
            setDefaultViewModel(DashboardViewModel.class);
        }
    }

    @Override
    public void onReceiveArgs(int requestCode, Object[] args) {

        InitArgs argsHelper = new InitArgs(this, args);

        if (requestCode == FireArgCode.USER_SIGNED_IN) {

            FireUser fireUser = argsHelper.getArg(FireUser.class);
            if (fireUser != null) {
                checkCredinals(getUid());
            }
        }

        if (requestCode == FireArgCode.USER_SIGNIN_ERROR) {

            FireSignIn.FireSignInVariant variant = argsHelper.getArg(FireSignIn.FireSignInVariant.class);
            Exception ex = argsHelper.getArg(Exception.class);

            if (variant == null) {
                variant = FireSignIn.FireSignInVariant.unknown;
            }

            handleLoginError(variant, ex);
        }
    }

    // TODO: 21/07/16 handle errors
    protected void handleLoginError(FireSignIn.FireSignInVariant variant, Exception ex) {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.login_page;
    }


    public void onMagicButtonPressed(View view) {

        /*Message message = new Message.Builder().addData(GcmMessageReceiver.KEY, BooGeo.KEY).build();

        new GcmMessage(getContext()).send(
                "AIzaSyB3cc3hSgwqxuBNNWSU3Ij0niObiyCOpyE",
                "fMTucfDuL3Q:APA91bFYMmVrrm7gnIPZwBZoCvPbzYsYHN25UEAA1NzIidsb4-ArKjrlJX2scZ8i2jo1Arrq5MTxZR1a9E3BbS3aIHEdLrj8pl4RJLxO-CbZxIe7kUdeVH6nTMGIvxCX_jz1WlBek74C",
                message
        );*/
    }
}
