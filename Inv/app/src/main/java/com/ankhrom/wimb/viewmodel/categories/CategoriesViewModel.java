package com.ankhrom.wimb.viewmodel.categories;


import android.databinding.ObservableField;
import android.view.MenuItem;
import android.view.View;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.interfaces.viewmodel.MenuItemableViewModel;
import com.ankhrom.base.model.Model;
import com.ankhrom.base.model.ToolbarItemModel;
import com.ankhrom.base.observable.AdapterRecycleBinder;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.databinding.CategoriesPageBinding;
import com.ankhrom.wimb.model.category.CategoryItemModel;
import com.ankhrom.wimb.viewmodel.InvViewModel;

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
                        //binding.list.modifyCount(count = count == 3 ? 2 : 3);
                    }
                }.setShowAsAction(true).setImageResourceId(R.drawable.placeholder)
        };
    }

    @Override
    public int getLayoutResource() {
        return R.layout.categories_page;
    }
}
