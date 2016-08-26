package com.ankhrom.wimb.model.dashboard;


import android.databinding.ObservableFloat;
import android.view.View;

import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.UserBooNotifyPopupBinding;
import com.ankhrom.wimb.model.InvPopupModel;

public class NotifyBooPopupModel extends InvPopupModel<UserBooNotifyPopupBinding> {

    public final ObservableFloat position = new ObservableFloat();

    public void onClosePressed(View view) {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.user_boo_notify_popup;
    }
}
