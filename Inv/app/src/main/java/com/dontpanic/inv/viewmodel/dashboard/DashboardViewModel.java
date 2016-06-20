package com.dontpanic.inv.viewmodel.dashboard;

import com.dontpanic.inv.viewmodel.InvViewModel;
import com.dontpanicbase.inv.R;

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
