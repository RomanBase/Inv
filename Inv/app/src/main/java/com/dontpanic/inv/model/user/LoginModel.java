package com.dontpanic.inv.model.user;


import android.databinding.ObservableBoolean;

import com.dontpanic.base.observable.EditTextObservable;
import com.dontpanic.inv.model.InvModel;

public class LoginModel extends InvModel {

    public final ObservableBoolean showLoginFields = new ObservableBoolean(false);
    public final EditTextObservable email = new EditTextObservable();
    public final EditTextObservable password = new EditTextObservable();
}
