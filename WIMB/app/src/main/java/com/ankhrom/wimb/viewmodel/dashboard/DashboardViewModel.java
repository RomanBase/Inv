package com.ankhrom.wimb.viewmodel.dashboard;

import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.ankhrom.wimb.R;

public class DashboardViewModel extends InvViewModel {

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
