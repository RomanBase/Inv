package com.dontpanic.inv.common;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.dontpanic.base.animators.BaseAnim;
import com.dontpanic.base.common.statics.ColorHelper;
import com.dontpanic.base.custom.builder.ScrollFlagsBuilder;
import com.dontpanic.base.custom.listener.AppBarStateChangedListener;
import com.dontpanic.base.custom.view.ImageGlideView;

public class ToolbarObserver {

    private final Context context;

    private int defaultFlags;

    private final CollapsingToolbarLayout toolbarCollapsing;
    private final AppBarLayout toolbarAppbar;
    private final Toolbar toolbar;
    private final ImageGlideView toolbarImage;

    public ToolbarObserver(@NonNull Context context, @NonNull CollapsingToolbarLayout toolbarCollapsing, @NonNull AppBarLayout toolbarAppbar, @NonNull Toolbar toolbar, @NonNull ImageGlideView toolbarImage) {

        this.context = context;
        this.toolbarCollapsing = toolbarCollapsing;
        this.toolbarAppbar = toolbarAppbar;
        this.toolbar = toolbar;
        this.toolbarImage = toolbarImage;

        defaultFlags = ((AppBarLayout.LayoutParams) this.toolbarCollapsing.getLayoutParams()).getScrollFlags();
    }

    public void postLock(final boolean keepToolbarVisible, final boolean animate) {

        toolbarAppbar.post(new Runnable() {
            @Override
            public void run() {
                lock(keepToolbarVisible, animate);
            }
        });
    }

    public void postUnlock(final boolean animate) {

        toolbarAppbar.post(new Runnable() {
            @Override
            public void run() {
                unlock(animate);
            }
        });
    }

    public void collapse(boolean animate) {

        toolbarAppbar.setExpanded(false, animate);
    }

    public void expand(boolean animate) {

        toolbarAppbar.setExpanded(true, animate);
    }

    public void lock(boolean toolbarVisible, boolean animate) {

        if (toolbarVisible) {
            setFlagsIdle(new ScrollFlagsBuilder().scroll().exitUntilCollapsed().snap().build());
        } else {
            setFlagsIdle(new ScrollFlagsBuilder().scroll().snap().build());
        }

        new AppBarStateChangedListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarState state) {
                if (state == AppBarState.COLLAPSED) {
                    unregister(toolbarAppbar);
                    setFlagsIdle(new ScrollFlagsBuilder().snap().build());
                }
            }
        }.register(toolbarAppbar);

        collapse(animate);
    }

    public void unlock() {

        setFlagsIdle(defaultFlags);
    }

    public void unlock(boolean animate) {

        setFlagsIdle(defaultFlags);
        expand(animate);
    }

    private void setFlagsIdle(int flags) {

        setFlags(toolbarCollapsing, flags);
    }

    public void setToolbarColor(final int color, boolean animate) {

        Drawable background = toolbarAppbar.getBackground();
        final int prevColor = background instanceof ColorDrawable ? ((ColorDrawable) background).getColor() : Color.TRANSPARENT;

        if (animate) {
            ValueAnimator animator = new ValueAnimator();
            animator.setFloatValues(0.0f, 1.0f);
            animator.setDuration(BaseAnim.DURATION);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float t = (float) animation.getAnimatedValue();
                    int iColor = ColorHelper.interpolate(prevColor, color, t);

                    toolbarAppbar.setBackgroundColor(iColor);
                    toolbarCollapsing.setContentScrimColor(iColor);
                }
            });
            animator.start();
        } else {
            toolbarAppbar.setBackgroundColor(color);
            toolbarCollapsing.setContentScrimColor(color);
        }
    }

    public void setStatusBarColor(final int color, boolean animate) {

        Drawable background = toolbarCollapsing.getStatusBarScrim();
        final int prevColor = background instanceof ColorDrawable ? ((ColorDrawable) background).getColor() : Color.TRANSPARENT;

        if (animate) {
            ValueAnimator animator = new ValueAnimator();
            animator.setFloatValues(0.0f, 1.0f);
            animator.setDuration(BaseAnim.DURATION);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float t = (float) animation.getAnimatedValue();
                    int iColor = ColorHelper.interpolate(prevColor, color, t);

                    toolbarCollapsing.setStatusBarScrimColor(iColor);
                }
            });
            animator.start();
        } else {
            toolbarCollapsing.setStatusBarScrimColor(color);
        }
    }

    public void setBackgroundColor(final int statusColor, final int toolbarColor, boolean animate) {

        Drawable statusBackground = toolbarCollapsing.getStatusBarScrim();
        final int prevStatusColor = statusBackground instanceof ColorDrawable ? ((ColorDrawable) statusBackground).getColor() : Color.TRANSPARENT;

        Drawable toolbarBackground = toolbarAppbar.getBackground();
        final int prevToolbarColor = toolbarBackground instanceof ColorDrawable ? ((ColorDrawable) toolbarBackground).getColor() : Color.TRANSPARENT;

        if (animate) {
            ValueAnimator animator = new ValueAnimator();
            animator.setFloatValues(0.0f, 1.0f);
            animator.setDuration(BaseAnim.DURATION);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float t = (float) animation.getAnimatedValue();

                    int iStatusColor = ColorHelper.interpolate(prevStatusColor, statusColor, t);
                    int iToolbarColor = ColorHelper.interpolate(prevToolbarColor, toolbarColor, t);

                    toolbarCollapsing.setStatusBarScrimColor(iStatusColor);
                    toolbarAppbar.setBackgroundColor(iToolbarColor);
                    toolbarCollapsing.setContentScrimColor(iToolbarColor);
                }
            });
            animator.start();
        } else {
            toolbarCollapsing.setStatusBarScrimColor(statusColor);
            toolbarAppbar.setBackgroundColor(toolbarColor);
            toolbarCollapsing.setContentScrimColor(toolbarColor);
        }
    }

    public void setCollapsingHeight(int height) {

        ViewGroup.LayoutParams params = toolbarCollapsing.getLayoutParams();
        params.height = height;
    }

    public void setCollapsingHeightDimen(@DimenRes int resId) {

        ViewGroup.LayoutParams params = toolbarCollapsing.getLayoutParams();
        params.height = (int) context.getResources().getDimension(resId);
    }

    public void setCollapsingFlags(int flags) {

        defaultFlags = flags;
        setFlags(toolbarCollapsing, flags);
    }

    public void setImageMode(int mode) {

        setMode(toolbarImage, mode);
    }

    public void setToolbarMode(int mode) {

        setMode(toolbar, mode);
    }

    private void setFlags(View view, int flags) {

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) view.getLayoutParams();
        params.setScrollFlags(flags);
    }

    private void setMode(View view, int mode) {

        CollapsingToolbarLayout.LayoutParams params = (CollapsingToolbarLayout.LayoutParams) view.getLayoutParams();
        params.setCollapseMode(mode);
    }
}
