package com.dontpanic.inv.viewmodel.user;

import android.view.MenuItem;

import com.dontpanic.base.interfaces.viewmodel.MenuItemableViewModel;
import com.dontpanic.base.interfaces.viewmodel.ViewModel;
import com.dontpanic.base.model.Model;
import com.dontpanic.base.model.ToolbarItemModel;
import com.dontpanic.base.viewmodel.BaseViewModelObserver;
import com.dontpanic.inv.viewmodel.InvViewModel;
import com.dontpanicbase.inv.R;
import com.dontpanicbase.inv.databinding.UserDetailPageBinding;


public class UserDetailViewModel extends InvViewModel<UserDetailPageBinding, Model> implements MenuItemableViewModel {

    @Override
    public void onInit() {
        super.onInit();

        setTitle("User");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.user_detail_page;
    }

    @Override
    public ToolbarItemModel[] getMenuItems() {
        return new ToolbarItemModel[]{
                new ToolbarItemModel("logout") {
                    @Override
                    public void onClick(MenuItem item) {

                        getFireFactory().auth.signOut();

                        BaseViewModelObserver observer = getObserver();
                        ViewModel vm = getFactory().getViewModel(LoginViewModel.class);

                        if (vm != null) {
                            observer.setDefaultViewModel(vm);
                            observer.notifyViewModelChanged();
                        }
                    }
                }.setImageResourceId(R.drawable.placeholder)
                        .setShowAsAction(true)
        };
    }
}
