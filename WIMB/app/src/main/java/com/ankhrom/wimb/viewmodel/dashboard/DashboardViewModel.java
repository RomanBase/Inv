package com.ankhrom.wimb.viewmodel.dashboard;

import com.ankhrom.base.model.Model;
import com.ankhrom.wimb.databinding.DashboardPageBinding;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.ankhrom.wimb.R;

public class DashboardViewModel extends InvViewModel<DashboardPageBinding, Model> {

    @Override
    public void onInit() {
        super.onInit();

        setTitle("dashboard");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dashboard_page;
    }
}
