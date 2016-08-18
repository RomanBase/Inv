package com.ankhrom.base.custom.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.ankhrom.base.R;
import com.ankhrom.base.custom.decorator.ListSpaceDecorator;

public class BaseListView extends RecyclerView {
    public BaseListView(Context context) {
        super(context);
        init(context, null);
    }

    public BaseListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        int spacing;
        boolean includeEdges;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseListView, 0, 0);

        spacing = ta.getInteger(R.styleable.BaseListView_spacing, 0);
        includeEdges = ta.getBoolean(R.styleable.BaseListView_includeEdge, true);
        ta.recycle();

        setLayoutManager(new LinearLayoutManager(context));

        if (spacing > 0) {
            addItemDecoration(new ListSpaceDecorator(spacing, includeEdges));
        }
    }
}
