package com.ankhrom.wimb;

import com.ankhrom.base.BaseFactory;
import com.ankhrom.fire.FacebookSignIn;
import com.ankhrom.fire.FireSignIn;
import com.ankhrom.fire.GoogleSignIn;
import com.ankhrom.wimb.entity.AppUser;
import com.ankhrom.wimb.entity.AppUserCredentials;
import com.ankhrom.wimb.fire.FireUser;
import com.google.firebase.auth.FirebaseAuth;

public class FireFactory extends BaseFactory {

    public final FirebaseAuth auth;
    public final FireSignIn signIn;

    public GoogleSignIn googleSignIn; // TODO: 21/07/16
    public FacebookSignIn facebookSignIn; // TODO: 21/07/16  

    public FireUser user;
    public AppUser appUser;
    public AppUserCredentials appUserCredentials;

    public FireFactory(FireSignIn signIn) {
        this.signIn = signIn;
        this.auth = signIn.auth;
    }
}
