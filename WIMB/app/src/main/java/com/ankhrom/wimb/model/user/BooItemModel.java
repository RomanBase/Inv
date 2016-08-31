package com.ankhrom.wimb.model.user;


import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.base.observable.ObservableUri;
import com.ankhrom.fire.FireData;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.entity.AppUserCredentials;
import com.ankhrom.wimb.fire.FireTimestamp;
import com.ankhrom.wimb.fire.FireValueListener;
import com.ankhrom.wimb.model.InvSelectableItemModel;
import com.google.firebase.database.DatabaseReference;

public abstract class BooItemModel extends InvSelectableItemModel {

    public final ObservableUri avatar = new ObservableUri();
    public final ObservableString nickname = new ObservableString();
    public final ObservableString location = new ObservableString();
    public final ObservableString time = new ObservableString();
    public final ObservableDouble lat = new ObservableDouble();
    public final ObservableDouble lng = new ObservableDouble();

    public final ObservableBoolean isLocationAvailable = new ObservableBoolean();
    public final ObservableBoolean isExpanded = new ObservableBoolean();

    private final String sid;
    private final DatabaseReference ref;

    public BooItemModel(FireData fireData, String sid, OnItemSelectedListener<BooItemModel> itemSelectedListener) {
        super(itemSelectedListener);

        this.sid = sid;

        ref = fireData
                .subscribe(credentialsListener)
                .get(sid);
    }

    private final FireValueListener<AppUserCredentials> credentialsListener = new FireValueListener<AppUserCredentials>(AppUserCredentials.class) {
        @Override
        public void onDataChanged(@Nullable AppUserCredentials data) {

            if (data == null) {
                return;
            }

            if (!StringHelper.isEmpty(data.avatar)) {
                avatar.set(Uri.parse(data.avatar));
            }

            nickname.set(data.nickname);
            location.set(data.location);
            time.set(data.lastUpdate > 0 ? FireTimestamp.getReadable(data.lastUpdate) : null);

            if (data.geo != null) {
                lat.set(data.geo.lat);
                lng.set(data.geo.lng);
            }
        }
    };

    protected abstract void onNotify(String sid, BooItemModel model);

    protected abstract void onGpsRequest(String sid, BooItemModel model);

    public void onNotifyPressed(View view) {

        onNotify(sid, this);
    }

    public void onGpsLocatePressed(View view) {

        onGpsRequest(sid, this);
    }

    public void unsubscribe() {

        ref.removeEventListener(credentialsListener);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.user_boo_item;
    }
}
