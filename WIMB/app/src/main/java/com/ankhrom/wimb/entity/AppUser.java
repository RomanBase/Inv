package com.ankhrom.wimb.entity;


import android.support.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class AppUser {

    public String sid;
    public List<String> boo;

    public void addBoo(AppUserCredentials user, String sid) {

        if (boo == null) {
            boo = new ArrayList<>();
        }

        boo.add(sid);
    }
}

