package com.ankhrom.wimb.entity;


import android.content.Context;
import android.support.annotation.NonNull;

import com.ankhrom.wimb.preferences.UserPrefs;

public class User {

    public static final String KEY = "users";

    public String sid;
    public String nickname;
    public String avatar;
    public boolean isLocationEnabled;

    public static User init(@NonNull String nickname) {

        User user = new User();
        user.nickname = nickname;

        return user;
    }

    public static UserPrefs prefs(@NonNull Context context) {

        return new UserPrefs(context);
    }
}
