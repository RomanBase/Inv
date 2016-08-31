package com.ankhrom.wimb.viewmodel.user;


import com.ankhrom.base.common.statics.FragmentHelper;
import com.ankhrom.base.custom.args.InitArgs;
import com.ankhrom.base.interfaces.viewmodel.CloseableViewModel;
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

public class BooDetailViewModel extends InvViewModel<UserBooDetailPageBinding, BooItemModel> implements CloseableViewModel, OnMapReadyCallback {

    @Override
    public void init(InitArgs args) {
        super.init(args);

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

    @Override
    public int getLayoutResource() {
        return R.layout.user_boo_detail_page;
    }

    @Override
    public boolean isCloseable() {
        return true;
    }
}
