package com.dontpanic.inv.viewmodel.user;


import android.view.View;

import com.dontpanic.base.Base;
import com.dontpanic.base.common.statics.ObjectHelper;
import com.dontpanic.base.common.statics.StringHelper;
import com.dontpanic.base.custom.args.InitArgs;
import com.dontpanic.base.interfaces.viewmodel.ViewModel;
import com.dontpanic.base.model.Model;
import com.dontpanic.base.observable.EditTextObservable;
import com.dontpanic.base.viewmodel.BaseViewModelObserver;
import com.dontpanic.fire.FireData;
import com.dontpanic.inv.entity.User;
import com.dontpanic.inv.fire.FireUser;
import com.dontpanic.inv.viewmodel.InvViewModel;
import com.dontpanic.inv.viewmodel.categories.CategoriesViewModel;
import com.dontpanicbase.inv.R;
import com.dontpanicbase.inv.databinding.LoginCredinalsPageBinding;
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
