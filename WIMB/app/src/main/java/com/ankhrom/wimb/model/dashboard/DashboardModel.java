package com.ankhrom.wimb.model.dashboard;


import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.wimb.BR;
import com.ankhrom.wimb.model.user.BooItemModel;

public class DashboardModel extends AdapterModel<BooItemModel> {

    public DashboardModel(Context context) {
        super(context);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
