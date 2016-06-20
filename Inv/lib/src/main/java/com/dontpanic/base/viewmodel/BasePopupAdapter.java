package com.dontpanic.base.viewmodel;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.dontpanic.base.interfaces.PopupModelAdapter;
import com.dontpanic.base.model.PopupModel;

public class BasePopupAdapter implements PopupModelAdapter {

    private Context context;
    private ViewGroup root;

    private final List<PopupModel> stack;
    private boolean stricktMode = true;

    public BasePopupAdapter() {

        stack = new ArrayList<>();
    }

    @Override
    public void init(Context context, ViewGroup root) {
        this.context = context;
        this.root = root;
    }

    @Override
    public void show(PopupModel popupModel) {
        popupModel.setAdapter(this);

        if (stricktMode) {
            if (isActive()) {
                int c = stack.size();
                if (c == 1) {
                    PopupModel cpm = stack.get(0);
                    if (isReplaceable(cpm, popupModel)) {
                        popupModel.init(cpm.getPopup(), cpm.getParent(), cpm.getBinding());
                    }

                    stack.clear();
                    stack.add(popupModel);
                    return;
                } else if (c > 0) {
                    for (PopupModel popup : stack) {
                        popupModel.getParent().removeView(popupModel.getPopup());
                    }

                    stack.clear();
                }
            }
        }

        stack.add(popupModel);
        popupModel.init(context, root);
    }

    @Override
    public void hide(PopupModel popupModel) {

        stack.remove(popupModel);
        if (popupModel != null) {
            ViewGroup parent = popupModel.getParent();
            if (parent != null) {
                parent.removeView(popupModel.getPopup());
            }
        }
    }

    @Override
    public boolean isActive() {
        return !stack.isEmpty();
    }

    @Override
    public PopupModel getCurrentPopupModel() {

        return isActive() ? stack.get(stack.size() - 1) : null;
    }

    public boolean isReplaceable(PopupModel pm1, PopupModel pm2) {

        return pm1.getLayoutResource() == pm2.getLayoutResource() && pm1.getVariableBindingResource() == pm2.getVariableBindingResource();
    }

    public void setStricktMode(boolean stricktMode) {
        this.stricktMode = stricktMode;
    }
}
