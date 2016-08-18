package com.ankhrom.wimb.viewmodel.user;


import android.support.annotation.Nullable;
import android.view.View;

import com.ankhrom.base.Base;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.base.model.Model;
import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.fire.FireData;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.LoginCredinalsPageBinding;
import com.ankhrom.wimb.entity.User;
import com.ankhrom.wimb.fire.FireUser;
import com.ankhrom.wimb.fire.FireValueListener;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.ankhrom.wimb.viewmodel.dashboard.DashboardViewModel;
import com.google.firebase.database.DatabaseError;

public class LoginCredinalsViewModel extends InvViewModel<LoginCredinalsPageBinding, Model> {

    public final EditTextObservable nickname = new EditTextObservable();
    private FireUser fireUser;
    private User activeUser;

    @Override
    public void init(InitArgs args) {
        super.init(args);

        fireUser = args.getArg(FireUser.class);
    }

    public void onSendPressed(View view) {

        String nick = nickname.get();

        if (StringHelper.isEmpty(nick)) {
            return;
        }

        createFireUser(User.init(nick), getUid());
    }

    private void createFireUser(User user, String uid) {

        isLoading.set(true);

        activeUser = user;
        activeUser.sid = FireData.uid();
        activeUser.isLocationEnabled = true;

        getFireData()
                .listener(fireUserListener)
                .root(User.KEY)
                .get(uid)
                .setValue(activeUser);
    }

    private final FireValueListener<User> fireUserListener = new FireValueListener<User>(User.class) {
        @Override
        public void onDataChanged(@Nullable User data) {

            if (data == null) {
                return;
            }

            Base.logV("user created");
            User.prefs(getContext()).set(activeUser);

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

            activeUser = null;
            isLoading.set(false);
            Base.logE("user creating error");
        }
    };

    @Override
    public int getLayoutResource() {
        return R.layout.login_credinals_page;
    }
}
