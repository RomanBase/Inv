package com.ankhrom.wimb.model.user;


import android.databinding.ObservableBoolean;

import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.wimb.model.InvModel;

public class LoginModel extends InvModel {

    public final ObservableBoolean showLoginFields = new ObservableBoolean(false);
    public final EditTextObservable email = new EditTextObservable();
    public final EditTextObservable password = new EditTextObservable();
}
