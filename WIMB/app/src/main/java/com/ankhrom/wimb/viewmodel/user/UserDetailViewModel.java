package com.ankhrom.wimb.viewmodel.user;

import android.view.MenuItem;

import com.ankhrom.base.interfaces.viewmodel.MenuItemableViewModel;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.base.model.Model;
import com.ankhrom.base.model.ToolbarItemModel;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.UserDetailPageBinding;


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
