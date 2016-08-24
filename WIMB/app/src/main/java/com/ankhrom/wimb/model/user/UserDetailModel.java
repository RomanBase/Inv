package com.ankhrom.wimb.model.user;


import android.databinding.ObservableBoolean;
import android.view.View;

import com.ankhrom.base.animators.BaseAnim;
import com.ankhrom.base.custom.listener.OnTouchActionListener;
import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.wimb.model.InvModel;

public class UserDetailModel extends InvModel {

    public final EditTextObservable nickname = new EditTextObservable();
    public final ObservableString sid = new ObservableString();
    public final ObservableBoolean sidIsLoading = new ObservableBoolean();

    public final View.OnTouchListener onTouchAnim = new OnTouchActionListener() {
        @Override
        public void onTouchActionDown(View view) {

            BaseAnim.scale(view, 1.0f, 1.25f);
        }

        @Override
        public void onTouchActionUp(View view) {

            BaseAnim.scale(view, 1.25f, 1.0f);
        }
    };
}
