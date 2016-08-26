package com.ankhrom.wimb.model.user;


import android.view.View;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.base.observable.ObservableUri;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.model.InvSelectableItemModel;

public class BooItemModel extends InvSelectableItemModel {

    public final ObservableUri avatar = new ObservableUri();
    public final ObservableString nickname = new ObservableString();
    public final ObservableString location = new ObservableString();
    public final ObservableString time = new ObservableString();

    public BooItemModel(OnItemSelectedListener<BooItemModel> itemSelectedListener) {
        super(itemSelectedListener);
    }

    public void onNotifyPressed(View view) {

    }

    public void onGpsLocatePressed(View view) {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.user_boo_item;
    }
}
