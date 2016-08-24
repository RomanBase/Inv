package com.ankhrom.wimb.entity;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Keep;

import com.ankhrom.base.common.statics.FileHelper;
import com.ankhrom.base.common.statics.StringHelper;

@Keep
public class AppUserCredentials {

    public String nickname;
    public String avatar;
    public boolean isLocationEnabled;

    public Uri getAvatar(Context context) {

        if (StringHelper.isEmpty(avatar)) {
            return FileHelper.getResourceUri(context, "drawable", "placeholder");
        }

        return Uri.parse(avatar);
    }
}
