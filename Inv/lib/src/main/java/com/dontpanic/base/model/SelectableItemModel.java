package com.dontpanic.base.model;

import android.view.View;

import com.dontpanic.base.interfaces.OnItemSelectedListener;
import com.dontpanic.base.interfaces.SelectableItem;

public abstract class SelectableItemModel extends ItemModel implements SelectableItem {

    private OnItemSelectedListener itemSelectedListener;

    public SelectableItemModel() {
    }

    public SelectableItemModel(OnItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    @Override
    public void onItemSelected(View view) {

        if(itemSelectedListener != null){
            itemSelectedListener.onItemSelected(view, this);
        }
    }
}
