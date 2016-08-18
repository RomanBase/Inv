package com.ankhrom.wimb.viewmodel.dashboard;

import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.DashboardPageBinding;
import com.ankhrom.wimb.model.dashboard.DashboardModel;
import com.ankhrom.wimb.model.user.BooItemModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class DashboardViewModel extends InvViewModel<DashboardPageBinding, DashboardModel> {

    @Override
    public void onInit() {
        super.onInit();

        setTitle("dashboard");

        BooItemModel[] items = new BooItemModel[]{
                new BooItemModel(), new BooItemModel(), new BooItemModel(), new BooItemModel(), new BooItemModel()
        };

        setModel(new DashboardModel(getContext(), new ArrayList<BooItemModel>(Arrays.asList(items))));
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dashboard_page;
    }
}
