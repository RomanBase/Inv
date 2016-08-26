package com.ankhrom.wimb.viewmodel.user;


import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.ankhrom.base.Base;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.base.model.Model;
import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.fire.FireData;
import com.ankhrom.gcm.GcmPrefs;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.LoginCredinalsPageBinding;
import com.ankhrom.wimb.entity.AppUser;
import com.ankhrom.wimb.entity.AppUserCredentials;
import com.ankhrom.wimb.entity.BooToken;
import com.ankhrom.wimb.fire.FirePosition;
import com.ankhrom.wimb.fire.FireValueListener;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.ankhrom.wimb.viewmodel.dashboard.DashboardViewModel;
import com.google.firebase.database.DatabaseError;

public class LoginCredinalsViewModel extends InvViewModel<LoginCredinalsPageBinding, Model> {

    public final EditTextObservable nickname = new EditTextObservable();

    public final TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                onSendPressed(null);
                return true;
            }

            return false;
        }
    };

    public void onSendPressed(View view) {

        String nick = nickname.get();

        if (StringHelper.isEmpty(nick)) {
            return;
        }

        createFireUser(nick);
    }

    private void createFireUser(String nick) {

        isLoading.set(true);

        String uid = getUid();
        String sid = FireData.uid();

        AppUser user = new AppUser();
        user.sid = sid;

        AppUserCredentials credentials = new AppUserCredentials();
        credentials.nickname = nick;
        credentials.isLocationEnabled = true;

        PreferenceManager.getDefaultSharedPreferences(getContext())
                .edit()
                .putString(AppUser.SID, sid)
                .apply();

        getFireData()
                .root(AppUser.KEY)
                .get(uid)
                .setValue(user);

        getFireData()
                .listener(fireUserListener)
                .root(AppUser.CREDENTIALS)
                .get(sid)
                .setValue(credentials);

        getFireData()
                .root(BooToken.KEY)
                .get(sid)
                .setValue(new GcmPrefs(getContext()).getToken());

        FirePosition.update(getContext());
    }

    private final FireValueListener<AppUser> fireUserListener = new FireValueListener<AppUser>(AppUser.class) {
        @Override
        public void onDataChanged(@Nullable AppUser data) {

            if (data == null) {
                return;
            }

            Base.logV("user created");

            BaseViewModelObserver observer = getObserver();
            ViewModel vm = getFactory().getViewModel(DashboardViewModel.class);

            if (vm != null) {
                observer.setDefaultViewModel(vm);
                observer.notifyViewModelChanged();
            }

            isLoading.set(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

            isLoading.set(false);
            Base.logE("user creating error");
        }
    };

    @Override
    public int getLayoutResource() {
        return R.layout.login_credinals_page;
    }
}
