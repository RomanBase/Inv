package com.dontpanic.base.interfaces;

import android.content.Intent;

public interface ActivityEventListener {

    void onResume();

    void onPause();

    boolean onBaseBackPressed();

    boolean onBaseActivityResult(int requestCode, int resultCode, Intent data);
}
