package com.ankhrom.wimb.viewmodel.user;

import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import com.ankhrom.base.Base;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.viewmodel.CloseableViewModel;
import com.ankhrom.base.interfaces.viewmodel.MenuItemableViewModel;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.base.model.ToolbarItemModel;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.fire.FireData;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.UserDetailPageBinding;
import com.ankhrom.wimb.entity.Geo;
import com.ankhrom.wimb.entity.User;
import com.ankhrom.wimb.fire.FireQuerySingleListener;
import com.ankhrom.wimb.fire.FireValueListener;
import com.ankhrom.wimb.model.user.UserDetailModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;


public class UserDetailViewModel extends InvViewModel<UserDetailPageBinding, UserDetailModel> implements MenuItemableViewModel, CloseableViewModel {

    private User activeUser;

    @Override
    public void onInit() {
        super.onInit();

        setTitle("User");
        setModel(new UserDetailModel());
        loadUserData();
    }

    private final FireValueListener<User> userFireListener = new FireValueListener<User>(User.class) {
        @Override
        public void onDataChanged(@Nullable User data) {

            activeUser = data;

            if (data == null) {
                return;
            }

            model.sid.set(data.sid);

            isLoading.set(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

            isLoading.set(false);
        }
    };

    private void loadUserData() {

        getFireData()
                .listener(new FireQuerySingleListener<User>(User.class) {
                    @Override
                    public void onDataChanged(@Nullable User data) {
                        if (data == null) {
                            Base.logE("fire no entry");
                        } else {
                            Base.log(data.nickname, data.sid);
                        }
                    }
                })
                .root(User.KEY)
                .search("sid")
                .find("-KPRpY60oAUHjH_KaPxb");

        isLoading.set(true);
        getFireData()
                .listener(userFireListener)
                .root(User.KEY)
                .get(getUid());
    }

    public void onResetSidPressed(View view) {

        if (activeUser == null) {
            return;
        }

        if (!StringHelper.isEmpty(activeUser.sid)) {

            getFireData()
                    .root(Geo.KEY)
                    .get(activeUser.sid)
                    .removeValue();
        }

        activeUser.sid = FireData.uid();

        getFireData()
                .root(Geo.KEY)
                .geo()
                .set(activeUser.sid, new GeoLocation(5, 5));

        getFireData()
                .listener(userFireListener)
                .root(User.KEY)
                .get(getUid())
                .setValue(activeUser);
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

    @Override
    public boolean isCloseable() {
        return true;
    }
}
