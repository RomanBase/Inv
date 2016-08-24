package com.ankhrom.wimb.entity;


import android.support.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class AppUser {

    public static final String KEY = "users";
    public static final String SID = "sid";
    public static final String BOO = "boo";
    public static final String AVATAR = "avatar";
    public static final String CREDENTIALS = "credentials";

    public String sid;
    public List<BooUser> boo;

    public void addBoo(AppUserCredentials user, String sid) {

        if (boo == null) {
            boo = new ArrayList<>();
        }

        BooUser bu = new BooUser();
        bu.sid = sid;
        bu.nickname = user.nickname;
        bu.avatar = user.avatar;

        boo.add(bu);
    }
}

