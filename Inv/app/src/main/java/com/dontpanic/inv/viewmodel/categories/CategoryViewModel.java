package com.dontpanic.inv.viewmodel.categories;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.dontpanic.base.animators.PageAnimation;
import com.dontpanic.base.common.statics.FragmentHelper;
import com.dontpanic.base.common.statics.ScreenHelper;
import com.dontpanic.base.custom.args.InitArgs;
import com.dontpanic.base.interfaces.viewmodel.AnimableTransitionViewModel;
import com.dontpanic.base.interfaces.viewmodel.CloseableViewModel;
import com.dontpanic.base.model.Model;
import com.dontpanic.inv.model.category.CategoryModel;
import com.dontpanic.inv.viewmodel.InvViewModel;
import com.dontpanicbase.inv.R;
import com.dontpanicbase.inv.databinding.CategoryPageBinding;

public class CategoryViewModel extends InvViewModel<CategoryPageBinding, Model> implements CloseableViewModel, AnimableTransitionViewModel {

    private View parentItemView;

    private PageAnimation animation = new PageAnimation();

    @Override
    public void init(InitArgs args) {
        super.init(args);

        parentItemView = args.getArg(View.class);
    }

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
                parentItemView = null;
                FragmentHelper.removePage(getContext(), getFragment());
            }
        });

        return true;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (parentItemView == null) {
            return;
        }

        float padding = getResources().getDimension(R.dimen.base_padding);
        float iconSize = ScreenHelper.getPx(getContext(), 96.0f);

        PageAnimation.Item page = new PageAnimation.Item(view.findViewById(R.id.category_background), parentItemView, (View) parentItemView.getParent());
        page.setBackground(ContextCompat.getColor(getContext(), R.color.item_active_color), ContextCompat.getColor(getContext(), R.color.background_color));

        PageAnimation.Item icon = new PageAnimation.Item(view.findViewById(R.id.category_image), parentItemView, new RectF(padding, padding, iconSize, iconSize));

        animation.addItem(page);
        animation.addItem(icon);
        animation.startShowAnimation(null);
    }

    @Override
    protected void onCreateViewBinding(CategoryPageBinding binding) {
        super.onCreateViewBinding(binding);

        CategoryModel model = new CategoryModel();

        binding.setM(model);
    }

    @Override
    public int getLayoutResource() {

        return R.layout.category_page;
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public int getTransition() {
        return FragmentTransaction.TRANSIT_NONE;
    }
}
