package com.ankhrom.wimb.viewmodel.dashboard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.ankhrom.base.Base;
import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.interfaces.viewmodel.MenuItemableViewModel;
import com.ankhrom.base.model.ToolbarItemModel;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.DashboardPageBinding;
import com.ankhrom.wimb.entity.AppUser;
import com.ankhrom.wimb.entity.AppUserCredentials;
import com.ankhrom.wimb.entity.BooRequest;
import com.ankhrom.wimb.entity.BooUser;
import com.ankhrom.wimb.fire.FireArgCode;
import com.ankhrom.wimb.fire.FireValueListener;
import com.ankhrom.wimb.model.dashboard.AddBooPopupModel;
import com.ankhrom.wimb.model.dashboard.DashboardModel;
import com.ankhrom.wimb.model.user.BooItemModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.google.firebase.database.DatabaseError;

public class DashboardViewModel extends InvViewModel<DashboardPageBinding, DashboardModel> implements MenuItemableViewModel {

    private AppUserCredentials requestedUser;
    private String requestedSid;

    @Override
    public void onInit() {
        super.onInit();

        setTitle("dashboard");

        setModel(new DashboardModel(getContext()));
        onUserDataChanged();
    }

    private final AddBooPopupModel popup = new AddBooPopupModel() {
        @Override
        protected void onFindRequested(@NonNull String sid) {
            findBoo(sid);
        }

        @Override
        protected void onClaimRequested() {
            claimBoo();
        }
    };

    private final FireValueListener<AppUserCredentials> findUserListener = new FireValueListener<AppUserCredentials>(AppUserCredentials.class) {
        @Override
        public void onDataChanged(@Nullable AppUserCredentials data) {
            if (data == null) {
                onUserNotFound();
            } else {
                onUserFound(data);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            onUserNotFound();
        }
    };

    void findBoo(String sid) {

        requestedSid = sid;

        popup.isFound.set(false);
        popup.isLoading.set(true);
        popup.nickname.set(null);
        popup.avatar.set(null);

        getFireData()
                .listener(findUserListener)
                .root(AppUser.CREDENTIALS)
                .get(sid);
    }

    void claimBoo() {

        if (requestedUser == null) {
            return;
        }

        popup.hide();
        clearPopup();

        AppUser activeUser = getAppUser();
        activeUser.addBoo(requestedUser, requestedSid);

        BooRequest request = new BooRequest();
        request.nickname = getUserCredentials().nickname;

        getFireData() // TODO: 19/08/16 listener ?
                .root(BooRequest.KEY)
                .root(requestedSid)
                .get(activeUser.sid)
                .setValue(request);

        getFireData() // TODO: 19/08/16 listener ?
                .root(AppUser.KEY)
                .get(getUid())
                .child(AppUser.BOO)
                .setValue(activeUser.boo);

        BooItemModel item = new BooItemModel();
        item.nickname.set(requestedUser.nickname);
        model.adapter.add(item);
    }

    void onUserFound(AppUserCredentials user) {

        requestedUser = user;

        popup.nickname.set(user.nickname);
        popup.avatar.set(user.avatar);
        popup.isLoading.set(false);

        AppUser activeUser = getFireFactory().appUser;
        if (activeUser.boo != null) {
            for (BooUser bu : activeUser.boo) {
                if (ObjectHelper.equals(bu.sid, requestedSid)) {
                    popup.isFound.set(false);
                    return;
                }
            }
        }

        popup.isFound.set(true);
    }

    void onUserNotFound() {

        requestedUser = null;
        popup.isLoading.set(false);

        Base.logE("user not found");
    }

    void clearPopup() {

        popup.isFound.set(false);
        popup.isLoading.set(false);
        popup.nickname.set(null);
        popup.avatar.set(null);
        popup.sid.set(null);
    }

    void onUserDataChanged() {

        if (model.adapter.getItemCount() > 0) {
            return;
        }

        AppUser activeUser = getFireFactory().appUser;
        if (activeUser != null && activeUser.boo != null) {

            for (BooUser user : activeUser.boo) {
                BooItemModel item = new BooItemModel();
                item.nickname.set(user.nickname);
                model.adapter.add(item);
            }
        }

        model.adapter.notifyDataSetChanged();
    }

    @Override
    public void onReceiveArgs(int requestCode, Object[] args) {
        super.onReceiveArgs(requestCode, args);

        if (requestCode == FireArgCode.USER_DATA_CHANGED) {
            onUserDataChanged();
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dashboard_page;
    }

    @Override
    public ToolbarItemModel[] getMenuItems() {

        return new ToolbarItemModel[]{
                new ToolbarItemModel("Add") {
                    @Override
                    public void onClick(MenuItem item) {
                        clearPopup();
                        getObserver().getPopupAdapter().show(popup);
                    }
                }.setShowAsAction(true).setImageResourceId(R.drawable.placeholder)
        };
    }
}
