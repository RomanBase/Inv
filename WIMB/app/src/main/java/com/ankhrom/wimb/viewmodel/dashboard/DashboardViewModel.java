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
import com.ankhrom.wimb.entity.BooRequest;
import com.ankhrom.wimb.entity.User;
import com.ankhrom.wimb.fire.FireArgCode;
import com.ankhrom.wimb.fire.FireQuerySingleListener;
import com.ankhrom.wimb.model.dashboard.AddBooPopupModel;
import com.ankhrom.wimb.model.dashboard.DashboardModel;
import com.ankhrom.wimb.model.user.BooItemModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class DashboardViewModel extends InvViewModel<DashboardPageBinding, DashboardModel> implements MenuItemableViewModel {

    private User requestedUser;

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

    private final FireQuerySingleListener<User> findUserListener = new FireQuerySingleListener<User>(User.class) {
        @Override
        public void onDataChanged(@Nullable User data) {
            if (data == null) {
                notFound();
            } else {
                userFound(data);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            notFound();
        }
    };

    void findBoo(String sid) {

        popup.isFound.set(false);
        popup.isLoading.set(true);
        popup.nickname.set(null);
        popup.avatar.set(null);

        getFireData()
                .listener(findUserListener)
                .root(User.KEY)
                .search(User.SID)
                .find(sid);
    }

    void claimBoo() {

        if (requestedUser == null) {
            return;
        }

        popup.hide();
        clearPopup();

        DatabaseReference ref = getFireData() // TODO: 19/08/16 listener
                .root(BooRequest.KEY)
                .get(requestedUser.sid)
                .push();

        ref.setValue(getUid());

        User activeUser = getFireFactory().activeUser;
        activeUser.addBoo(requestedUser, ref.getKey());

        getFireData() // TODO: 19/08/16 listener
                .root(User.KEY)
                .get(getUid())
                .child(User.BOO)
                .setValue(activeUser.boo);


        BooItemModel item = new BooItemModel();
        item.nickname.set(requestedUser.nickname);
        model.adapter.add(item);
    }

    void userFound(User user) {

        requestedUser = user;

        popup.nickname.set(user.nickname);
        popup.avatar.set(user.avatar);
        popup.isLoading.set(false);

        User activeUser = getFireFactory().activeUser;
        if (activeUser.boo != null) {
            for (User.BooUser bu : activeUser.boo) {
                if (ObjectHelper.equals(bu.sid, requestedUser.sid)) {
                    popup.isFound.set(false);
                    return;
                }
            }
        }

        popup.isFound.set(true);
    }

    void notFound() {

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

        User activeUser = getFireFactory().activeUser;
        if (activeUser != null && activeUser.boo != null) {

            for (User.BooUser user : activeUser.boo) {
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
