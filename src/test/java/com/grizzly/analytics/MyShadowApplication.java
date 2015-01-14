package com.grizzly.analytics;

import android.app.Application;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowApplication;

/**
 * Created by fpardo on 1/9/15.
 */
@Implements(Application.class)
public class MyShadowApplication extends ShadowApplication {
    @Implementation
    public boolean bindService(Intent intent, final ServiceConnection serviceConnection, int i) {
        Log.d("Robolectric", intent.getAction());
        return false;
    }
}

