package com.dontpanic.base.interfaces;

public interface OnActivityStateChangedListener {

    void onActivityResume();

    void onActivityPause();

    void onActivityDestroy();
}
