package com.ankhrom.wimb.viewmodel.user;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.ankhrom.base.Base;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.viewmodel.CloseableViewModel;
import com.ankhrom.base.interfaces.viewmodel.MenuItemableViewModel;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.base.model.ToolbarItemModel;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.fire.FireData;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.UserDetailPageBinding;
import com.ankhrom.wimb.entity.AppUser;
import com.ankhrom.wimb.entity.BooGeo;
import com.ankhrom.wimb.model.user.UserDetailModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;


public class UserDetailViewModel extends InvViewModel<UserDetailPageBinding, UserDetailModel> implements MenuItemableViewModel, CloseableViewModel {

    @Override
    public void onInit() {
        super.onInit();

        setTitle("AppUser");
        setModel(new UserDetailModel());
    }

    // TODO: 23/08/16
    public void onResetSidPressed(View view) {

        if (Base.debug) {
            return;
        }

        AppUser user = getAppUser();

        if (!StringHelper.isEmpty(user.sid)) {

            getFireData()
                    .root(BooGeo.KEY)
                    .get(user.sid)
                    .removeValue();
        }

        user.sid = FireData.uid();

        getFireData()
                .root(AppUser.KEY)
                .root(getUid())
                .get(AppUser.SID)
                .setValue(user.sid);
    }

    public void onChangePhotoPressed(View view) {

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setCustomMetadata("time", String.valueOf(System.currentTimeMillis()))
                .build();

        getFireStorage()
                .folder(AppUser.KEY)
                .folder(getAppUser().sid)
                .file(AppUser.AVATAR + ".jpg")
                .putBytes(new byte[]{0, 0, 0, 0}, metadata)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri url = taskSnapshot.getDownloadUrl();
                        if (url != null) {
                            onPhotoUploaded(url.toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void onGalleryPressed(View view) {

    }

    public void onCameraPressed(View view) {

    }

    private void onPhotoUploaded(String url) {

        getFireData()
                .root(AppUser.KEY)
                .root(getUid())
                .get(AppUser.AVATAR)
                .setValue(FireData.asString(url));
    }

    @Override
    public int getLayoutResource() {
        return R.layout.user_detail_page;
    }

    @Override
    public ToolbarItemModel[] getMenuItems() {

        return new ToolbarItemModel[]{
                new ToolbarItemModel("logout") {
                    @Override
                    public void onClick(MenuItem item) {

                        getFireFactory().auth.signOut();

                        BaseViewModelObserver observer = getObserver();
                        ViewModel vm = getFactory().getViewModel(LoginViewModel.class);

                        if (vm != null) {
                            observer.setDefaultViewModel(vm);
                            observer.notifyViewModelChanged();
                        }
                    }
                }.setImageResourceId(R.drawable.placeholder)
                        .setShowAsAction(true)
        };
    }

    @Override
    public boolean isCloseable() {
        return true;
    }
}
