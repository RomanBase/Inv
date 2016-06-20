package com.dontpanic.base.animators;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

import com.dontpanic.base.common.statics.MathHelper;

public class BaseAnim {

    public static final int DURATION = 250;
    public static final int SCALE_DURATION = 150;

    public static Animation scale(View view, float from, float to) {

        Animation animation = new ScaleAnimation(from, to, from, to,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(SCALE_DURATION);
        animation.setFillAfter(true);
        view.startAnimation(animation);

        return animation;
    }

    public static Animation fade(View view) {

        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(DURATION);
        animation.setFillAfter(true);
        view.startAnimation(animation);

        return animation;
    }

    public static Animation show(View view) {

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(DURATION);
        animation.setFillAfter(true);
        view.startAnimation(animation);

        return animation;
    }

    public static Animation transit(View view, final Rect from, final Rect to) {

        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        final float sx = (float) from.width() / (float) to.width();
        final float sy = (float) from.height() / (float) to.height();

        Animation animation = new ViewAnimation(view) {
            @Override
            protected void onInit(View view) {

                view.setScaleX(sx);
                view.setScaleY(sy);
                view.setPivotX(0);
                view.setPivotY(0);

                view.setX(from.left);
                view.setY(from.top);
            }

            @Override
            protected void onTransformationStep(View view, float delta) {

                view.setScaleX(MathHelper.interpolate(sx, 1.0f, delta));
                view.setScaleY(MathHelper.interpolate(sy, 1.0f, delta));

                view.setX(MathHelper.interpolate(from.left, to.left, delta));
                view.setY(MathHelper.interpolate(from.top, to.top, delta));
            }

            @Override
            protected void onTransformationDone(View view) {

                view.setX(to.left);
                view.setY(to.top);

                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                view.setPivotX((float) view.getWidth() * 0.5f);
                view.setPivotY((float) view.getHeight() * 0.5f);
            }
        };

        animation.setDuration(DURATION);
        animation.start();

        return animation;
    }

    public static Animation expand(final View v, final int targetHeight) {

        if (v.getLayoutParams() == null) {
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(DURATION);
        v.startAnimation(a);

        return a;
    }

    public static Animation collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(DURATION);
        v.startAnimation(a);

        return a;
    }
}
