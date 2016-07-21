package com.dontpanic.inv;

import com.dontpanic.base.BaseFactory;
import com.dontpanic.fire.FacebookSignIn;
import com.dontpanic.fire.FireSignIn;
import com.dontpanic.fire.GoogleSignIn;
import com.dontpanic.inv.fire.FireUser;
import com.google.firebase.auth.FirebaseAuth;

public class FireFactory extends BaseFactory {

    public final FirebaseAuth auth;
    public final FireSignIn signIn;

    public GoogleSignIn googleSignIn; // TODO: 21/07/16
    public FacebookSignIn facebookSignIn; // TODO: 21/07/16  

    public FireUser user;

    public FireFactory(FirebaseAuth auth, FireSignIn signIn) {
        this.auth = auth;
        this.signIn = signIn;
    }
}
