package com.ankhrom.wimb.entity;


import android.content.Context;
import android.support.annotation.NonNull;

import com.ankhrom.wimb.preferences.UserPrefs;

public class User {

    public static final String KEY = "users";

    public String uid;
    public String nickname;
    public String avatar;

    public static User init(@NonNull String uid, @NonNull String nickname) {

        User user = new User();
        user.uid = uid;
        user.nickname = nickname;

        return user;
    }

    public static UserPrefs prefs(@NonNull Context context) {

        return new UserPrefs(context);
    }
}
