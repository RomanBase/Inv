package com.ankhrom.wimb.model.user;


import android.databinding.ObservableBoolean;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.ankhrom.base.custom.listener.OnTouchActionListener;
import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.wimb.model.InvModel;

public class LoginModel extends InvModel {

    public final ObservableBoolean showLoginFields = new ObservableBoolean();
    public final ObservableBoolean showRegisterFields = new ObservableBoolean();

    public final EditTextObservable email = new EditTextObservable();
    public final EditTextObservable password = new EditTextObservable();

    public final OnTouchActionListener onTouch = new OnTouchActionListener() {
        @Override
        public void onTouchActionDown(View view) {
            view.startAnimation(new AlphaAnimation(1.0f, 0.5f));
        }

        @Override
        public void onTouchActionUp(View view) {
            view.startAnimation(new AlphaAnimation(0.0f, 1.0f));
        }
    };
}
