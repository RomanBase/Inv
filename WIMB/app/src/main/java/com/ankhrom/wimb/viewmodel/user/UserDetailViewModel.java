package com.ankhrom.wimb.viewmodel.user;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.ankhrom.wimb.fire.FireValueListener;
import com.ankhrom.wimb.model.user.UserDetailModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;


public class UserDetailViewModel extends InvViewModel<UserDetailPageBinding, UserDetailModel> implements MenuItemableViewModel, CloseableViewModel {

    private AppUser activeUser;

    @Override
    public void onInit() {
        super.onInit();

        setTitle("AppUser");
        setModel(new UserDetailModel());
        loadUserData();
    }

    private final FireValueListener<AppUser> userFireListener = new FireValueListener<AppUser>(AppUser.class) {
        @Override
        public void onDataChanged(@Nullable AppUser data) {

            activeUser = data;

            // TODO: 23/08/16
            if (data == null) {
                return;
            }

            model.sid.set(data.sid);

            isLoading.set(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

            isLoading.set(false);
        }
    };

    private void loadUserData() {

        activeUser = getFireFactory().activeUser;

        isLoading.set(true);
        getFireData()
                .listener(userFireListener)
                .root(AppUser.KEY)
                .get(getUid());
    }

    // TODO: 23/08/16
    public void onResetSidPressed(View view) {

        if (Base.debug) {
            return;
        }

        if (activeUser == null) {
            return;
        }

        if (!StringHelper.isEmpty(activeUser.sid)) {

            getFireData()
                    .root(BooGeo.KEY)
                    .get(activeUser.sid)
                    .removeValue();
        }

        activeUser.sid = FireData.uid();

        getFireData()
                .listener(userFireListener)
                .root(AppUser.KEY)
                .root(getUid())
                .get(AppUser.SID)
                .setValue(activeUser.sid);
    }

    public void onChangePhotoPressed(View view) {

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setCustomMetadata("time", String.valueOf(System.currentTimeMillis()))
                .build();

        getFireStorage()
                .folder(AppUser.KEY)
                .folder(activeUser.sid)
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
