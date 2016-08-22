package com.ankhrom.wimb.entity;


import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@Keep
public class AppUser {

    public static final String KEY = "users";
    public static final String SID = "sid";
    public static final String BOO = "boo";

    public String sid;
    public String nickname;
    public String avatar;
    public boolean isLocationEnabled;
    public List<BooUser> boo;

    public static AppUser init(@NonNull String nickname) {

        AppUser user = new AppUser();
        user.nickname = nickname;

        return user;
    }

    public void addBoo(AppUser user) {

        if (boo == null) {
            boo = new ArrayList<>();
        }

        BooUser bu = new BooUser();
        bu.sid = user.sid;
        bu.nickname = user.nickname;

        boo.add(bu);
    }
}

