package com.dontpanic.inv.viewmodel.categories;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.dontpanic.base.animators.BaseAnim;
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

    @Override
    public void init(InitArgs args) {
        super.init(args);

        parentItemView = args.getArg(View.class);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (parentItemView == null) {
            return;
        }

        View root = view.getRootView().findViewById(R.id.root_container);

        Rect item = new Rect(parentItemView.getLeft(), parentItemView.getTop(), parentItemView.getRight(), parentItemView.getBottom());
        Rect screen = new Rect(0, 0, root.getRight() - root.getLeft(), root.getBottom() - root.getTop());

        BaseAnim.transit(view, item, screen);
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
