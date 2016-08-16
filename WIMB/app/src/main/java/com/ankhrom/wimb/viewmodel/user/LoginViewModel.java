package com.ankhrom.wimb.viewmodel.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.fire.FacebookSignIn;
import com.ankhrom.fire.FireData;
import com.ankhrom.fire.FireSignIn;
import com.ankhrom.fire.GoogleSignIn;
import com.ankhrom.wimb.FireFactory;
import com.ankhrom.wimb.entity.User;
import com.ankhrom.wimb.fire.FireArgCode;
import com.ankhrom.wimb.fire.FireUser;
import com.ankhrom.wimb.fire.FireValueListener;
import com.ankhrom.wimb.model.user.LoginModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.ankhrom.wimb.viewmodel.categories.CategoriesViewModel;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.LoginPageBinding;
import com.google.firebase.database.DatabaseError;

public class LoginViewModel extends InvViewModel<LoginPageBinding, LoginModel> {

    private FireUser fireUser;

    @Override
    public void onInit() {
        super.onInit();

        setTitle("login");
        setModel(new LoginModel());
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

        if (isFireLoginActive) {

            boolean isValid = true;
            if (StringHelper.isEmpty(model.email.get())) {
                isValid = false;
            }

            if (StringHelper.isEmpty(model.password.get())) {
                isValid = false;
            }

            if (isValid) {
                logInWithFirebase();
            }

        } else {
            model.showLoginFields.set(true);
        }
    }

    public void onCloseFireLoginPressed(View view) {

        model.showLoginFields.set(false);
    }

    private void logInWithFirebase() {

        getFireFactory().signIn.withFirebase(model.email.get(), model.password.get());
    }

    private void registerWithFirebase() {

        getFireFactory().signIn.withFirebaseRegistration(model.email.get(), model.password.get());
    }

    private void checkCredinals(String uid) {

        if (fireUser == null || isLoading.get()) {
            return;
        }

        isLoading.set(true);

        FireData.init()
                .root(User.KEY)
                .listener(new FireValueListener<User>(User.class) {

                    @Override
                    public void onDataChanged(@Nullable User data) {

                        onUserCredinals(data, fireUser);
                        isLoading.set(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        handleLoginError(FireSignIn.FireSignInVariant.unknown, null);
                        isLoading.set(false);
                    }
                })
                .get(fireUser.data.getUid());
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

    protected void onUserCredinals(@Nullable User data, @NonNull FireUser fireUser) {

        if (data == null || StringHelper.isEmpty(data.nickname)) {
            ViewModel vm = getFactory().getViewModel(LoginCredinalsViewModel.class, fireUser);
            getNavigation().setViewModel(vm, false);
        } else {
            setDefaultViewModel(CategoriesViewModel.class);
        }
    }

    @Override
    public void onReceiveArgs(int requestCode, Object[] args) {

        //magic button test
        /*if (requestCode == FireArgCode.USER_SIGNED_IN) {
            new ToastBuilder(getContext()).text("log in").buildAndShow();
            return;
        }*/

        InitArgs argsHelper = new InitArgs(this, args);

        if (requestCode == FireArgCode.USER_SIGNED_IN) {

            fireUser = argsHelper.getArg(FireUser.class);
            checkCredinals(fireUser.data.getUid());
        }

        if (requestCode == FireArgCode.USER_SIGNIN_ERROR) {

            FireSignIn.FireSignInVariant variant = argsHelper.getArg(FireSignIn.FireSignInVariant.class);
            Exception ex = argsHelper.getArg(Exception.class);

            if (variant != null) {
                variant = FireSignIn.FireSignInVariant.unknown;
            }

            handleLoginError(variant, ex);
        }
    }

    // TODO: 21/07/16 handle errors
    protected void handleLoginError(FireSignIn.FireSignInVariant variant, Exception ex) {

        switch (variant) {
            case firebase_login:
                registerWithFirebase();
                break;
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.login_page;
    }


    public void onMagicButtonPressed(View view) {


    }
}