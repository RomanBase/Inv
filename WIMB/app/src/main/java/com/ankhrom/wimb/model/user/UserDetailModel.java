package com.ankhrom.wimb.model.user;


import android.databinding.ObservableBoolean;

import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.wimb.model.InvModel;

public class UserDetailModel extends InvModel {

    public final ObservableString sid = new ObservableString();
    public final ObservableBoolean sidIsLoading = new ObservableBoolean();

}
