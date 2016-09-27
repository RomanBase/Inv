package com.ankhrom.wimb.viewmodel.user;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;

import com.ankhrom.base.Base;
import com.ankhrom.base.animators.LayerEnablingAnimatorListener;
import com.ankhrom.base.animators.PageAnimation;
import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.common.statics.ScreenHelper;
import com.ankhrom.base.common.statics.ViewHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.viewmodel.AnimableTransitionViewModel;
import com.ankhrom.base.interfaces.viewmodel.CloseableViewModel;
import com.ankhrom.base.interfaces.viewmodel.MenuItemableViewModel;
import com.ankhrom.base.model.ToolbarItemModel;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.UserBooDetailPageBinding;
import com.ankhrom.wimb.model.user.BooItemModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BooDetailViewModel extends InvViewModel<UserBooDetailPageBinding, BooItemModel> implements OnMapReadyCallback, CloseableViewModel, MenuItemableViewModel, AnimableTransitionViewModel {

    private final PageAnimation animation = new PageAnimation();
    private View parentItemView;

    @Override
    public boolean onBackPressed() {

        if (parentItemView == null) {
            return false;
        }

        if (animation.inProgress) {
            return true;
        }

        getNavigation().setPreviousViewModel();
        animation.startHideAnimation(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                parentItemView.setVisibility(View.VISIBLE);
                parentItemView = null;
                FragmentHelper.removePage(getContext(), getFragment());
            }
        });

        return true;
    }

    @Override
    public void init(InitArgs args) {
        super.init(args);

        parentItemView = args.getArg(View.class);

        initModel(args.getArg(BooItemModel.class));
    }

    private void initModel(BooItemModel model) {

        if (model == null) {
            onModelError();
            return;
        }

        setTitle(model.nickname.get());

        setModel(model);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (parentItemView == null) {
            return;
        }

        parentItemView.setVisibility(View.INVISIBLE);

        View image = view.findViewById(R.id.image);
        View background = view.findViewById(R.id.background);
        final View content = view.findViewById(R.id.content);

        content.setVisibility(View.GONE);

        float padding = getResources().getDimension(R.dimen.base_padding);
        float size = padding + getResources().getDimension(R.dimen.icon_size_big);

        RectF rect = ViewHelper.getRect(parentItemView, ScreenHelper.getPx(getContext(), 80.0f));

        animation.addItem(new PageAnimation.Item(image, rect, new RectF(padding, padding, size, size)));
        animation.addItem(new PageAnimation.Item(background).setBackground(ContextCompat.getColor(getContext(), R.color.transparent), ContextCompat.getColor(getContext(), R.color.colorPrimary)));

        animation.startShowAnimation(new LayerEnablingAnimatorListener(parentItemView));

        Base.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                content.setVisibility(View.VISIBLE);
            }
        }, 75);
    }

    @Override
    protected void onCreateViewBinding(UserBooDetailPageBinding binding) {
        super.onCreateViewBinding(binding);

        MapFragment mapFragment = MapFragment.newInstance();
        mapFragment.getMapAsync(this);

        FragmentHelper.getManager(getContext())
                .beginTransaction()
                .add(R.id.map_container, mapFragment)
                .commit();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        LatLng location = new LatLng(model.lat.get(), model.lng.get());

        map.getUiSettings().setCompassEnabled(false);

        //noinspection MissingPermission
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        map.addMarker(new MarkerOptions()
                .position(location)
                .title(model.nickname.get()));
    }

    private void removeBoo() {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.user_boo_detail_page;
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public ToolbarItemModel[] getMenuItems() {

        return new ToolbarItemModel[]{
                new ToolbarItemModel("remove") {
                    @Override
                    public void onClick(MenuItem item) {
                        removeBoo();
                    }
                }.setImageResourceId(R.drawable.ic_remove).setShowAsAction(true)
        };
    }

    @Override
    public int getTransition() {
        return FragmentTransaction.TRANSIT_NONE;
    }
}
