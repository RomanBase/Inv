package com.ankhrom.wimb.viewmodel.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.ankhrom.base.Base;
import com.ankhrom.base.BaseActivity;
import com.ankhrom.base.GlobalCode;
import com.ankhrom.base.common.BaseCamera;
import com.ankhrom.base.common.BaseGallery;
import com.ankhrom.base.common.BasePermission;
import com.ankhrom.base.common.statics.BitmapHelper;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.base.interfaces.viewmodel.CloseableViewModel;
import com.ankhrom.base.interfaces.viewmodel.MenuItemableViewModel;
import com.ankhrom.base.interfaces.viewmodel.ViewModel;
import com.ankhrom.base.model.ToolbarItemModel;
import com.ankhrom.base.viewmodel.BaseViewModelObserver;
import com.ankhrom.fire.FireData;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.common.ImageHelper;
import com.ankhrom.wimb.databinding.UserDetailPageBinding;
import com.ankhrom.wimb.entity.AppUser;
import com.ankhrom.wimb.entity.AppUserCredentials;
import com.ankhrom.wimb.fire.FireEntity;
import com.ankhrom.wimb.model.user.UserDetailModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;


public class UserDetailViewModel extends InvViewModel<UserDetailPageBinding, UserDetailModel> implements MenuItemableViewModel, CloseableViewModel {

    private BaseCamera camera;
    private BaseGallery gallery;

    @Override
    public void onInit() {
        super.onInit();

        setTitle(R.string.app_name);
        setModel(new UserDetailModel());
        setModelData();
    }

    private void setModelData() {

        AppUserCredentials credentials = getAppUserCredentials();

        model.avatar.set(ImageHelper.getUri(getContext(), credentials.avatar));
        model.nickname.set(credentials.nickname);
        model.sid.set(getAppUser().sid);
    }

    // TODO: 23/08/16
    public void onResetSidPressed(View view) {

        if (Base.debug) {
            return;
        }

        AppUser user = getAppUser();

        if (!StringHelper.isEmpty(user.sid)) {

            getFireData()
                    .root(FireEntity.GEO)
                    .get(user.sid)
                    .removeValue();
        }

        user.sid = FireData.uid();

        getFireData()
                .root(FireEntity.USER)
                .root(getUid())
                .get(FireEntity.SID)
                .setValue(user.sid);
    }

    public void uploadPhoto(byte[] data) {

        model.imageIsLoading.set(true);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        getFireStorage()
                .folder(FireEntity.USER)
                .folder(getAppUser().sid)
                .file(FireEntity.AVATAR + ".jpg")
                .putBytes(data, metadata)
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

    private void onPhotoUploaded(String url) {

        model.avatar.set(Uri.parse(url));

        getFireData()
                .root(FireEntity.CREDENTIALS)
                .root(getSid())
                .get(FireEntity.AVATAR)
                .setValue(FireData.asString(url));

        model.imageIsLoading.set(false);
    }

    public void onGalleryPressed(View view) {

        if (!BasePermission.isAvailable(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            BasePermission.requestDialog(getContext(), GlobalCode.PERMMISSION_REQUEST, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }

        gallery = BaseGallery.with((BaseActivity) getObserver().getContext()).open();
    }

    public void onCameraPressed(View view) {

        if (!BaseCamera.isCameraAvailable(getContext())) {
            return;
        }

        camera = BaseCamera.with((BaseActivity) getObserver().getContext()).open();
    }

    @Override
    public boolean onBaseActivityResult(int requestCode, int resultCode, Intent data) {

        if (camera != null) {
            Bitmap bitmap = camera.getOnActivityResult(requestCode, resultCode, data);
            camera = null;
            if (bitmap != null) {
                uploadPhoto(BitmapHelper.getBitmapJPG(bitmap, 100));
                return true;
            }
        }

        if (gallery != null) {
            Bitmap bitmap = gallery.getOnActivityResult(requestCode, resultCode, data);
            gallery = null;
            if (bitmap != null) {
                uploadPhoto(BitmapHelper.getBitmapJPG(bitmap, 100));
                return true;
            }
        }

        return super.onBaseActivityResult(requestCode, resultCode, data);
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
                }.setImageResourceId(R.drawable.ic_remove)
                        .setShowAsAction(true)
        };
    }

    @Override
    public boolean isCloseable() {
        return true;
    }
}
