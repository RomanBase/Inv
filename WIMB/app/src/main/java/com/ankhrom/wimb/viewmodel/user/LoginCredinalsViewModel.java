package com.ankhrom.wimb.viewmodel.user;


import android.view.View;

import com.ankhrom.base.Base;
import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.base.model.Model;
import com.ankhrom.base.observable.EditTextObservable;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.fire.FireData;
import com.ankhrom.wimb.entity.User;
import com.ankhrom.wimb.fire.FireUser;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.ankhrom.wimb.viewmodel.categories.CategoriesViewModel;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.LoginCredinalsPageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginCredinalsViewModel extends InvViewModel<LoginCredinalsPageBinding, Model> implements ValueEventListener {

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

        createFireUser(User.init(fireUser.data.getUid(), nick));
    }

    private void createFireUser(User user) {

        isLoading.set(true);
        activeUser = user;

        FireData.init()
                .listener(this)
                .root(User.KEY)
                .get(user.uid)
                .setValue(user);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        isLoading.set(false);

        if (activeUser != null && ObjectHelper.equals(activeUser.uid, dataSnapshot.getKey())) {

            Base.logV("user created");
            User.prefs(getContext()).set(activeUser);

            BaseViewModelObserver observer = getObserver();
            ViewModel vm = getFactory().getViewModel(CategoriesViewModel.class);

            if (vm != null) {
                observer.setDefaultViewModel(vm);
                observer.notifyViewModelChanged();
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

        activeUser = null;
        isLoading.set(false);
        Base.logE("user creating error");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.login_credinals_page;
    }
}