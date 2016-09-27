package com.ankhrom.wimb.common;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.ankhrom.base.common.statics.FileHelper;
import com.ankhrom.base.common.statics.StringHelper;

public final class ImageHelper {

    public static Uri getUri(Context context, @Nullable String url) {

        if (StringHelper.isEmpty(url)) {
            return FileHelper.getResourceUri(context, "drawable", "placeholder");
        }

        return Uri.parse(url);
    }
}
