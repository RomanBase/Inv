package com.ankhrom.wimb.viewmodel.sidemenu;

import android.content.Context;
import android.view.View;

import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.base.observable.ObservableUri;
import com.ankhrom.base.viewmodel.BaseMenuViewModel;
import com.ankhrom.wimb.FireFactory;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.common.ImageHelper;
import com.ankhrom.wimb.databinding.ActivityMenuBinding;
import com.ankhrom.wimb.entity.AppUserCredentials;
import com.ankhrom.wimb.fire.FireArgCode;
import com.ankhrom.wimb.viewmodel.user.UserDetailViewModel;

public class MenuModel extends BaseMenuViewModel<ActivityMenuBinding> {

    public final ObservableUri icon = new ObservableUri();
    public final ObservableString nickname = new ObservableString();

    public MenuModel(Context context, int container) {
        super(context, container);

        icon.set(ImageHelper.getUri(getContext(), null));
    }

    public void onUserDetailPressed(View view) {

        getNavigation().setViewModel(getFactory().getViewModel(UserDetailViewModel.class), false);
    }

    private FireFactory getFactory() {

        return getNavigation().getObserver().getFactory();
    }

    @Override
    protected void onBindingCreated(ActivityMenuBinding binding) {
        super.onBindingCreated(binding);

        binding.setMenu(this);
    }

    public void notifyUserData(int code, Object object) {

        if (code == FireArgCode.USER_CREDENTIALS_CHANGED) {
            AppUserCredentials credentials = (AppUserCredentials) object;

            icon.set(ImageHelper.getUri(getContext(), credentials.avatar));
            nickname.set(credentials.nickname);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_menu;
    }
}
