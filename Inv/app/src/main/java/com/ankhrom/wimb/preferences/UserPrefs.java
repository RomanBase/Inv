package com.ankhrom.wimb.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.wimb.entity.User;
import com.google.gson.Gson;

public class UserPrefs {

    public static final String KEY = "user";

    private static final String CREDINALS = "credinals";

    private final SharedPreferences prefs;

    public UserPrefs(@NonNull Context context) {

        prefs = context.getSharedPreferences(KEY, 0);
    }

    public void set(User user) {

        prefs.edit().putString(KEY, new Gson().toJson(user)).apply();
    }

    @Nullable
    public User get() {

        User user = null;

        try {
            String data = prefs.getString(CREDINALS, null);

            if (!StringHelper.isEmpty(data)) {
                user = new Gson().fromJson(data, User.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null) {
            if (StringHelper.isEmpty(user.uid)) {
                return null;
            }
        }

        return user;
    }
}
