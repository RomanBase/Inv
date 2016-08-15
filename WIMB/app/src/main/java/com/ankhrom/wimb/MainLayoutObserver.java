package com.ankhrom.wimb;


import android.content.Context;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.wimb.common.ToolbarObserver;
import com.ankhrom.wimb.interfaces.ToolbarToggler;
import com.ankhrom.wimb.databinding.ActivityCollapsingToolbarBinding;
import com.ankhrom.wimb.databinding.ActivityMainBinding;

public class MainLayoutObserver {

    private static final ToolbarToggler.State DEFAULT_TOOLBAR_STATE = ToolbarToggler.State.EXPANDED;

    public final ObservableString title = new ObservableString();
    public final ObservableField<Uri> imageSource = new ObservableField<>();

    private final Context context;
    private ToolbarObserver toolbarObserver;

    private ActivityMainBinding binding;
    private ActivityCollapsingToolbarBinding toolbarBinding;
    private ToolbarToggler.State currentToolbarState = DEFAULT_TOOLBAR_STATE;

    public MainLayoutObserver(@NonNull Context context) {
        this.context = context;
    }

    public void bind(@NonNull ActivityMainBinding binding, @NonNull ActivityCollapsingToolbarBinding toolbarBinding) {

        this.binding = binding;
        this.toolbarBinding = toolbarBinding;

        this.binding.setMain(this);
        this.toolbarBinding.setToolbar(this);

        toolbarObserver = new ToolbarObserver(context,
                toolbarBinding.toolbarCollapsing,
                toolbarBinding.toolbarAppbar,
                toolbarBinding.toolbar,
                toolbarBinding.toolbarImage
        );
    }

    public void toggleToolbar(ToolbarToggler toggler) {

        currentToolbarState = toggler.getToolbarState();

        switch (currentToolbarState) {
            case COLLAPSED:
                toolbarObserver.lock(false, toggler.animateStateChange(currentToolbarState));
                break;
            case EXPANDED:
                toolbarObserver.collapse(toggler.animateStateChange(currentToolbarState));
                break;
            case EXPANDED_FULL:
                toolbarObserver.expand(toggler.animateStateChange(currentToolbarState));
                break;
        }
    }

    public void setDefaultToolbarState() {

        toolbarObserver.unlock();

        currentToolbarState = DEFAULT_TOOLBAR_STATE;

    }
}
