package com.ankhrom.wimb.viewmodel.dashboard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import com.ankhrom.base.Base;
import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.interfaces.viewmodel.MenuItemableViewModel;
import com.ankhrom.base.model.ToolbarItemModel;
import com.ankhrom.fire.FireData;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.common.ImageHelper;
import com.ankhrom.wimb.databinding.DashboardPageBinding;
import com.ankhrom.wimb.entity.AppUser;
import com.ankhrom.wimb.entity.AppUserCredentials;
import com.ankhrom.wimb.entity.BooRequest;
import com.ankhrom.wimb.fire.FireValueListener;
import com.ankhrom.wimb.model.dashboard.AddBooPopupModel;
import com.ankhrom.wimb.model.dashboard.DashboardModel;
import com.ankhrom.wimb.model.dashboard.NotifyBooPopupModel;
import com.ankhrom.wimb.model.user.BooItemModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends InvViewModel<DashboardPageBinding, DashboardModel> implements MenuItemableViewModel {

    private AppUserCredentials requestedUser;
    private String requestedSid;

    @Override
    public void onInit() {
        super.onInit();

        setTitle("dashboard");

        setModel(new DashboardModel(getContext()));
        loadBooItems();
    }

    private final AddBooPopupModel addBooPopup = new AddBooPopupModel() {
        @Override
        protected void onFindRequested(@NonNull String sid) {
            findBoo(sid);
        }

        @Override
        protected void onClaimRequested() {
            claimBoo();
        }
    };

    private final NotifyBooPopupModel notifyBooPopup = new NotifyBooPopupModel();

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

    private final OnItemSelectedListener<BooItemModel> booSelectedListener = new OnItemSelectedListener<BooItemModel>() {
        @Override
        public void onItemSelected(View view, BooItemModel model) {

            notifyBooPopup.position.set(72 * 3);
            getObserver().getPopupAdapter().show(notifyBooPopup);
        }
    };

    private final FireValueListener<AppUserCredentials> userBooListener = new FireValueListener<AppUserCredentials>(AppUserCredentials.class) {
        @Override
        public void onDataChanged(@Nullable AppUserCredentials data) {

            if (data == null) {
                return;
            }

            BooItemModel item = new BooItemModel(booSelectedListener);
            item.avatar.set(ImageHelper.getUri(getContext(), data.avatar));
            item.nickname.set(data.nickname);

            model.adapter.add(item);
        }
    };

    void findBoo(String sid) {

        requestedSid = sid;

        addBooPopup.isFound.set(false);
        addBooPopup.isLoading.set(true);
        addBooPopup.nickname.set(null);
        addBooPopup.avatar.set(null);

        getFireData()
                .listener(findUserListener)
                .root(AppUser.CREDENTIALS)
                .get(sid);
    }

    void claimBoo() {

        if (requestedUser == null) {
            return;
        }

        addBooPopup.hide();
        clearPopup();

        AppUser activeUser = getAppUser();
        activeUser.addBoo(requestedUser, requestedSid);

        BooRequest request = new BooRequest();
        request.nickname = getAppUserCredentials().nickname;

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

        BooItemModel item = new BooItemModel(booSelectedListener);
        item.nickname.set(requestedUser.nickname);
        item.avatar.set(ImageHelper.getUri(getContext(), requestedUser.avatar));
        model.adapter.add(item);
    }

    void onUserFound(AppUserCredentials user) {

        requestedUser = user;

        addBooPopup.nickname.set(user.nickname);
        addBooPopup.avatar.set(ImageHelper.getUri(getContext(), user.avatar));
        addBooPopup.isLoading.set(false);

        AppUser activeUser = getFireFactory().appUser;
        if (activeUser.boo != null) {
            for (String bSid : activeUser.boo) {
                if (ObjectHelper.equals(bSid, requestedSid)) {
                    addBooPopup.isFound.set(false);
                    return;
                }
            }
        }

        addBooPopup.isFound.set(true);
    }

    void onUserNotFound() {

        requestedUser = null;
        addBooPopup.isLoading.set(false);

        Base.logE("user not found");
    }

    void clearPopup() {

        addBooPopup.isFound.set(false);
        addBooPopup.isLoading.set(false);
        addBooPopup.nickname.set(null);
        addBooPopup.avatar.set(null);
        addBooPopup.sid.set(null);
    }

    void loadBooItems() {

        if (model.adapter.getItemCount() > 0) {
            return;
        }

        FireData dbCredentials = getFireData()
                .root(AppUser.CREDENTIALS);

        List<String> bTest = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            bTest.add("Ahoy");
        }

        AppUser activeUser = getAppUser();
        if (activeUser != null && activeUser.boo != null) {
            for (String bSid : activeUser.boo) {
                dbCredentials
                        .listener(userBooListener)
                        .get(bSid);
            }
        }

        model.adapter.notifyDataSetChanged();
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
                        getObserver().getPopupAdapter().show(addBooPopup);
                    }
                }.setShowAsAction(true).setImageResourceId(R.drawable.ic_person_add)
        };
    }
}
