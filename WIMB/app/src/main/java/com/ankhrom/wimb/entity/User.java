package com.ankhrom.wimb.entity;


import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@Keep
public class User {

    public static final String KEY = "users";
    public static final String SID = "sid";
    public static final String BOO = "boo";

    public String sid;
    public String nickname;
    public String avatar;
    public boolean isLocationEnabled;
    public List<BooUser> boo;

    public static User init(@NonNull String nickname) {

        User user = new User();
        user.nickname = nickname;

        return user;
    }

    public void addBoo(User user, String request) {

        if (boo == null) {
            boo = new ArrayList<>();
        }

        BooUser bu = new BooUser();
        bu.sid = user.sid;
        bu.nickname = user.nickname;
        bu.request = request;

        boo.add(bu);
    }

    public static class BooUser {

        public String sid;
        public String nickname;
        public String request;
    }
}

