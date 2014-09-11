package com.asusoda.taskdevil.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Tyler on 9/10/2014.
 */
public class Notifications extends Service {
    public class LocalBinder extends Binder {
        Notifications getService() { return Notifications.this; }
    }

    private final IBinder mBinder = new LocalBinder();



    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
