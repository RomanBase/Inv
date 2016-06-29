package com.dontpanic.base.animators;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

public class BaseAnim {

    public static final int DURATION = 250;
    public static final int SCALE_DURATION = 150;

    public static Animation scale(View view, float from, float to) {

        Animation animation = new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(SCALE_DURATION);
        animation.setFillAfter(true);
        view.startAnimation(animation);

        return animation;
    }

    public static Animation scale(View view, View to, float padding) {

        float fromX = 1.0f;
        float fromY = 1.0f;
        float toX = (float) to.getWidth() / ((float) view.getWidth() - padding * 2.0f);
        float toY = (float) to.getHeight() / ((float) view.getHeight() - padding * 2.0f);

        Animation animation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(SCALE_DURATION);
        animation.setFillAfter(true);
        view.startAnimation(animation);

        return animation;
    }

    public static Animation scaleReverse(View view, View to, float padding) {

        float fromX = (float) to.getWidth() / ((float) view.getWidth() - padding * 2.0f);
        float fromY = (float) to.getHeight() / ((float) view.getHeight() - padding * 2.0f);
        float toX = 1.0f;
        float toY = 1.0f;

        Animation animation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
