package com.ankhrom.wimb.model.dashboard;


import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.base.observable.ObservableUri;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.UserBooAddPopupBinding;
import com.ankhrom.wimb.model.InvPopupModel;

public abstract class AddBooPopupModel extends InvPopupModel<UserBooAddPopupBinding> {

    public final EditTextObservable sid = new EditTextObservable();
    public final ObservableString nickname = new ObservableString();
    public final ObservableUri avatar = new ObservableUri();
    public final ObservableBoolean isFound = new ObservableBoolean();
    public final ObservableBoolean isLoading = new ObservableBoolean();

    public final TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onFindPressed(null);
                return true;
            }

            return false;
        }
    };

    protected abstract void onFindRequested(@NonNull String sid);

    protected abstract void onClaimRequested();

    public void onFindPressed(View view) {

        String sid = this.sid.get();

        if (StringHelper.isEmpty(sid)) {
            return;
        }

        onFindRequested(sid);
    }

    public void onClaimPressed(View view) {

        if (isFound.get()) {
            onClaimRequested();
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.user_boo_add_popup;
    }
}
