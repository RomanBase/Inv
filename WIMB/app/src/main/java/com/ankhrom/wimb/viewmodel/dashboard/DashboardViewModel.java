package com.ankhrom.wimb.viewmodel.dashboard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import com.ankhrom.base.Base;
import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.common.statics.StringHelper;
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
import com.ankhrom.wimb.fire.FireEntity;
import com.ankhrom.wimb.fire.FireTimestamp;
import com.ankhrom.wimb.fire.FireValueListener;
import com.ankhrom.wimb.gcm.WimbMessage;
import com.ankhrom.wimb.model.dashboard.AddBooPopupModel;
import com.ankhrom.wimb.model.dashboard.DashboardModel;
import com.ankhrom.wimb.model.dashboard.NotifyBooPopupModel;
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

            initBoo(data, snapshot.getKey());
        }
    };

    private final FireValueListener<String> userBooLocalizationTokenListener = new FireValueListener<String>(String.class) {
        @Override
        public void onDataChanged(@Nullable String token) {

            if (StringHelper.isEmpty(token)) {
                return;
            }

            WimbMessage.with(getContext(), FireEntity.GEO).sendTo(token);
        }
    };

    private final FireValueListener<String> userBooMessageTokenListener = new FireValueListener<String>(String.class) {
        @Override
        public void onDataChanged(@Nullable String token) {

            if (StringHelper.isEmpty(token)) {
                return;
            }

            WimbMessage.with(getContext(), FireEntity.NOTIFY)
                    .data(FireEntity.CREDENTIALS, "WIMB")
                    .data(FireEntity.MESSAGE, getAppUserCredentials().nickname)
                    .sendTo(token);
        }
    };

    void initBoo(AppUserCredentials data, String sid) {

        BooItemModel item = new BooItemModel(sid, booSelectedListener) {

            @Override
            protected void onNotify(String sid, BooItemModel model) {

                getFireData()
                        .listener(userBooMessageTokenListener)
                        .root(FireEntity.TOKEN)
                        .get(sid);
            }

            @Override
            protected void onGpsRequest(String sid, BooItemModel model) {

                getFireData()
                        .listener(userBooLocalizationTokenListener)
                        .root(FireEntity.TOKEN)
                        .get(sid);
            }
        };

        item.avatar.set(ImageHelper.getUri(getContext(), data.avatar));
        item.nickname.set(data.nickname);
        item.location.set(data.location);
        item.time.set(data.lastUpdate > 0 ? FireTimestamp.getReadable(data.lastUpdate) : null); // TODO: 29/08/16

        model.adapter.add(item);
    }

    void findBoo(String sid) {

        requestedSid = sid;

        addBooPopup.isFound.set(false);
        addBooPopup.isLoading.set(true);
        addBooPopup.nickname.set(null);
        addBooPopup.avatar.set(null);

        getFireData()
                .listener(findUserListener)
                .root(FireEntity.CREDENTIALS)
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
                .root(FireEntity.REQUEST)
                .root(requestedSid)
                .get(activeUser.sid)
                .setValue(request);

        getFireData() // TODO: 19/08/16 listener ?
                .root(FireEntity.USER)
                .get(getUid())
                .child(FireEntity.BOO)
                .setValue(activeUser.boo);

        initBoo(requestedUser, requestedSid);
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
                .root(FireEntity.CREDENTIALS);

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
