package com.ankhrom.wimb.model.category;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.ankhrom.base.animators.BaseAnim;
import com.ankhrom.base.custom.listener.OnTouchActionListener;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.model.InvSelectableItemModel;


public class CategoryItemModel extends InvSelectableItemModel {

    private static final float ACTION_SCALE = 1.35f;

    public final View.OnTouchListener onItemTouch = new OnTouchActionListener() {
        @Override
        public void onTouchActionDown(View view) {

            View image = ((ViewGroup) view).getChildAt(0);
            BaseAnim.scale(image, view, image.getPaddingTop());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setElevation(view.getResources().getDimension(R.dimen.base_padding));
            }
        }

        @Override
        public void onTouchActionUp(View view) {

            View image = ((ViewGroup) view).getChildAt(0);
            BaseAnim.scaleReverse(image, view, image.getPaddingTop());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setElevation(0.0f);
            }
        }
    };

    @Override
    public int getLayoutResource() {
        return R.layout.category_item;
    }
}
