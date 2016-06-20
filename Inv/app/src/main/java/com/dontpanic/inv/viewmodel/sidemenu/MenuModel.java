package com.dontpanic.inv.viewmodel.sidemenu;

import android.content.Context;
import android.view.View;

import com.dontpanic.base.viewmodel.BaseMenuViewModel;
import com.dontpanic.inv.FireFactory;
import com.dontpanic.inv.viewmodel.user.UserDetailViewModel;
import com.dontpanicbase.inv.R;
import com.dontpanicbase.inv.databinding.ActivityMenuBinding;

public class MenuModel extends BaseMenuViewModel<ActivityMenuBinding> {

    public MenuModel(Context context, int container) {
        super(context, container);
    }

    public void onUserDetailPressed(View view) {

        getNavigation().setViewModel(getFactory().getViewModel(UserDetailViewModel.class), false);
    }

    private FireFactory getFactory() {

        return getNavigation().getObserver().getFactory();
    }

    @Override
    protected void onBindingCreated(ActivityMenuBinding binding) {
        super.onBindingCreated(binding);

        binding.setMenu(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_menu;
    }
}
