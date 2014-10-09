package com.asusoda.taskdevil.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /***** For start Service  ****/
        Intent myIntent = new Intent(context, NotificationReceiver.class);
        context.startService(myIntent);
    }

}