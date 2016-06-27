package com.dontpanic.inv.model.category;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.dontpanic.base.animators.BaseAnim;
import com.dontpanic.base.custom.listener.OnTouchActionListener;
import com.dontpanic.inv.model.InvSelectableItemModel;
import com.dontpanicbase.inv.R;


public class CategoryItemModel extends InvSelectableItemModel {

    private static final float ACTION_SCALE = 1.35f;

    public static final View.OnTouchListener onItemTouch = new OnTouchActionListener() {
        @Override
        public void onTouchActionDown(View view) {

            BaseAnim.scale(((ViewGroup) view).getChildAt(0), 1.0f, ACTION_SCALE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setElevation(24.0f);
            }
        }

        @Override
        public void onTouchActionUp(View view) {

            BaseAnim.scale(((ViewGroup) view).getChildAt(0), ACTION_SCALE, 1.0f);

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
