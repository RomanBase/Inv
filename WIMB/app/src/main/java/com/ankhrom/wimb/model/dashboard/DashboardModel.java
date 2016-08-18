package com.ankhrom.wimb.model.dashboard;


import android.content.Context;

import com.ankhrom.base.model.AdapterModel;
import com.ankhrom.wimb.BR;
import com.ankhrom.wimb.model.user.BooItemModel;

import java.util.Collection;

public class DashboardModel extends AdapterModel<BooItemModel> {

    public DashboardModel(Context context) {
        super(context);
    }

    public DashboardModel(Context context, Collection<BooItemModel> collection) {
        super(context, collection);
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }
}
