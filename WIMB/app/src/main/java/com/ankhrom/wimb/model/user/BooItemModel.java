package com.ankhrom.wimb.model.user;


import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.model.InvSelectableItemModel;

public class BooItemModel extends InvSelectableItemModel {

    public final ObservableString avatar = new ObservableString();
    public final ObservableString nickname = new ObservableString();

    @Override
    public int getLayoutResource() {
        return R.layout.user_boo_item;
    }
}
