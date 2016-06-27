package com.dontpanic.inv.viewmodel.categories;


import android.databinding.ObservableField;
import android.view.MenuItem;
import android.view.View;

import com.dontpanic.base.interfaces.OnItemSelectedListener;
import com.dontpanic.base.interfaces.viewmodel.MenuItemableViewModel;
import com.dontpanic.base.model.Model;
import com.dontpanic.base.model.ToolbarItemModel;
import com.dontpanic.base.observable.AdapterRecycleBinder;
import com.dontpanic.inv.model.category.CategoryItemModel;
import com.dontpanic.inv.viewmodel.InvViewModel;
import com.dontpanicbase.inv.R;
import com.dontpanicbase.inv.databinding.CategoriesPageBinding;

public class CategoriesViewModel extends InvViewModel<CategoriesPageBinding, Model> implements MenuItemableViewModel, OnItemSelectedListener<CategoryItemModel> {

    public final ObservableField<AdapterRecycleBinder<CategoryItemModel>> adapter = new ObservableField<>();

    @Override
    public void onInit() {
        super.onInit();

        AdapterRecycleBinder<CategoryItemModel> binder = new AdapterRecycleBinder<CategoryItemModel>(getContext());
        for (int i = 0; i < 10; i++) {
            CategoryItemModel item = new CategoryItemModel();
            item.setOnItemSelectedListener(this);
            binder.add(item);
        }

        adapter.set(binder);
    }

    @Override
    public void onItemSelected(View view, CategoryItemModel model) {

        getNavigation().addViewModel(getFactory().getViewModel(CategoryViewModel.class, view), true);
    }

    int count = 2;

    @Override
    public ToolbarItemModel[] getMenuItems() {
        return new ToolbarItemModel[]{
                new ToolbarItemModel("modify") {
                    @Override
                    public void onClick(MenuItem item) {
                        binding.list.modifyCount(count = count == 3 ? 2 : 3);
                    }
                }.setShowAsAction(true).setImageResourceId(R.drawable.placeholder)
        };
    }

    @Override
    public int getLayoutResource() {
        return R.layout.categories_page;
    }
}
