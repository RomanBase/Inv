package com.dontpanic.base.interfaces;

import android.view.View;

import com.dontpanic.base.model.ItemModel;

public interface OnItemSelectedListener<T extends ItemModel> {

    void onItemSelected(View view, T model);
}
