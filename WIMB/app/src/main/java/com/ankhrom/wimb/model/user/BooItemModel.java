package com.ankhrom.wimb.model.user;


import android.databinding.ObservableBoolean;
import android.view.View;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.base.observable.ObservableUri;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.model.InvSelectableItemModel;

public abstract class BooItemModel extends InvSelectableItemModel {

    public final ObservableUri avatar = new ObservableUri();
    public final ObservableString nickname = new ObservableString();
    public final ObservableString location = new ObservableString();
    public final ObservableString time = new ObservableString();
    public final ObservableBoolean isLocationAvailable = new ObservableBoolean();

    public final ObservableBoolean isExpanded = new ObservableBoolean();

    private final String sid;

    public BooItemModel(String sid, OnItemSelectedListener<BooItemModel> itemSelectedListener) {
        super(itemSelectedListener);

        this.sid = sid;
    }

    protected abstract void onNotify(String sid, BooItemModel model);

    protected abstract void onGpsRequest(String sid, BooItemModel model);

    public void onNotifyPressed(View view) {

        onNotify(sid, this);
    }

    public void onGpsLocatePressed(View view) {

        onGpsRequest(sid, this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.user_boo_item;
    }
}
