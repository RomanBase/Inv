package com.ankhrom.wimb.viewmodel;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.base.model.Model;
import com.ankhrom.base.viewmodel.BaseViewModel;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.fire.FireData;
import com.ankhrom.fire.FireFile;
import com.ankhrom.wimb.BR;
import com.ankhrom.wimb.FireFactory;
import com.ankhrom.wimb.MainActivity;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.entity.AppUser;
import com.ankhrom.wimb.entity.AppUserCredentials;

public abstract class InvViewModel<S extends ViewDataBinding, T extends Model> extends BaseViewModel<S, T> {

    @Nullable
    public MainActivity getContextAsActivity() {

        Context context = getContext();

        if (context instanceof MainActivity) {
            return (MainActivity) getContext();
        }

        return null;
    }

    protected String getUid() {

        return getFireFactory().user.data.getUid();
    }

    protected String getSid(){

        return getAppUser().sid;
    }

    protected FireData getFireData() {

        return FireData.init();
    }

    protected FireFile getFireStorage() {

        return FireFile.with(getString(R.string.fire_storage_bucket));
    }

    protected AppUser getAppUser() {

        return getFireFactory().appUser;
    }

    protected AppUserCredentials getAppUserCredentials() {

        return getFireFactory().appUserCredentials;
    }

    protected FireFactory getFireFactory() {

        return getFactory();
    }

    protected void setDefaultViewModel(Class clazz) {

        BaseViewModelObserver observer = getObserver();
        ViewModel vm = getFactory().getViewModel(clazz);

        if (vm != null) {
            observer.setDefaultViewModel(vm);
            observer.notifyViewModelChanged();
        }
    }

    @Override
    public int getBindingResource() {
        return BR.VM;
    }
}
