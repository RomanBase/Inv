package com.dontpanic.inv.viewmodel.user;

import android.content.Intent;
import android.view.View;

import com.dontpanic.base.Base;
import com.dontpanic.base.common.statics.StringHelper;
import com.dontpanic.base.custom.args.InitArgs;
import com.dontpanic.base.custom.builder.ToastBuilder;
import com.dontpanic.base.interfaces.viewmodel.ViewModel;
import com.dontpanic.base.viewmodel.BaseViewModelObserver;
import com.dontpanic.fire.FacebookSignIn;
import com.dontpanic.fire.FireData;
import com.dontpanic.fire.FireSignIn;
import com.dontpanic.fire.GoogleSignIn;
import com.dontpanic.inv.FireFactory;
import com.dontpanic.inv.fire.FireArgCode;
import com.dontpanic.inv.model.user.LoginModel;
import com.dontpanic.inv.viewmodel.InvViewModel;
import com.dontpanic.inv.viewmodel.categories.CategoriesViewModel;
import com.dontpanicbase.inv.R;
import com.dontpanicbase.inv.databinding.LoginPageBinding;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginViewModel extends InvViewModel<LoginPageBinding, LoginModel> {

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

    @Override
    public void onReceiveArgs(int requestCode, Object[] args) {

        if (Base.debug) {
            new ToastBuilder(getContext()).text("log").buildAndShow();
            return;
        }

        if (requestCode == FireArgCode.USER_SIGNED_IN) {

            BaseViewModelObserver observer = getObserver();
            ViewModel vm = getFactory().getViewModel(CategoriesViewModel.class);

            if (vm != null) {
                observer.setDefaultViewModel(vm);
                observer.notifyViewModelChanged();
            }
        }

        if (requestCode == FireArgCode.USER_SIGNIN_ERROR) {

            InitArgs argsHelper = new InitArgs(this, args);
            FireSignIn.FireSignInVariant variant = argsHelper.getArg(FireSignIn.FireSignInVariant.class);
            Exception ex = argsHelper.getArg(Exception.class);

            if (variant != null) {
                variant = FireSignIn.FireSignInVariant.unknown;
            }

            handleLoginError(variant, ex);
        }
    }

    // TODO: 21/07/16 handle errors
    private void handleLoginError(FireSignIn.FireSignInVariant variant, Exception ex) {

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

        FireData data = FireData.init();

        data.listener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Base.log(dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Base.logE(databaseError.getMessage());
            }
        });

        DatabaseReference ref = data.root("base").get("kuk");
        ref.setValue(new Ahoj());

        ref.push();

        data.geo(ref).set("loc", new GeoLocation(10.0, 10.0));
    }

    public static class Ahoj {

        public String a1 = "1";
        public String a2 = "2";
        public int i3 = 3;
    }
}
