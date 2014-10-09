package com.asusoda.taskdevil.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Pair;
import android.app.Notification;
import android.widget.Toast;

import com.asusoda.taskdevil.activities.MainActivity;
import com.asusoda.taskdevil.data_access_layer.DataAccess;
import com.asusoda.taskdevil.data_access_layer.DatabaseManager;
import com.asusoda.taskdevil.models.Task;
import com.asusoda.taskdevil.receivers.NotificationReceiver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tyler on 9/10/2014.
 */
public class Notifier extends Service {
    private HashMap<Pair< Integer, Long >, PendingIntent> mNotifications; // [ID, time] => Alarm

    private NotificationManager mNM;
    private AlarmManager mAM;

    PendingIntent mPI;

    public class LocalBinder extends Binder {
        public Notifier getService() { return Notifier.this; }
    }

    private final IBinder mBinder = new LocalBinder();

    // if bound service is just now being created
    @Override
    public void onCreate() {
        //Toast.makeText(this, "Service is apparently starting", Toast.LENGTH_LONG).show();
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mAM = (AlarmManager)getSystemService(ALARM_SERVICE);

        mNotifications = new HashMap<Pair< Integer, Long >, PendingIntent>();
        scheduleNextHour();
    }

    // used in scheduleNextHour to call scheduleNextHour in an hour
    final private Handler callThisInAnHour = new Handler();

    // we should get the next hour from this second forward.
    // then the next time we get tasks will be in 59 minutes
    // at 59 minutes we will get the next hour and cut out the tasks that already exist
    public void scheduleNextHour() {
        // set options for next hour retrieval
        DataAccess.TaskRetrieveOptions options  = new DataAccess.TaskRetrieveOptions();
        options.nextHour = new Boolean(true);
        // this returns the next hour of tasks
        ArrayList<Task> taskList = DataAccess.getTasks(this, options);

        // go 1 minute into the future!
        long aMinuteFromNow = System.currentTimeMillis() * 1000L + 1L;

        if (!taskList.isEmpty()) {

            HashMap<Pair< Integer, Long>, PendingIntent> temp = new HashMap<Pair< Integer, Long>, PendingIntent>();
            // we need to get to the first entry of the task list that is not in the collision zone
            Pair<Integer, Long>  tempKey = new Pair<Integer, Long>( taskList.get(0).getId(), taskList.get(0).getNotificationAt());
            while (mNotifications.containsKey(tempKey)) {
                taskList.remove(0);
                temp.put(tempKey, mNotifications.get(tempKey));
                tempKey = new Pair<Integer, Long>( taskList.get(0).getId(), taskList.get(0).getNotificationAt());
            }

            mNotifications = temp;

            while (!taskList.isEmpty()) {
                // create pending intents for tasks
                Task t = taskList.remove(0);
                long timeAt = t.getNotificationAt() * 1000L;
                mAM.set(AlarmManager.RTC_WAKEUP, timeAt, mPI);
                Intent i = new Intent(this, NotificationReceiver.class);
                i.putExtra("title", t.getTitle());
                i.putExtra("description", t.getDescription());
                mPI = PendingIntent.getBroadcast(this, 0, i, 0);
                mAM.set(AlarmManager.RTC_WAKEUP, timeAt, mPI);
                mNotifications.put(new Pair<Integer, Long>(t.getId(), timeAt), mPI);
                mPI = null;
            }
        }

        callThisInAnHour.postDelayed(new Runnable() {
            @Override
            public void run() { scheduleNextHour(); }
        }, 3540*1000L); // this is an hour - 1 minute
    }

    @Override
    public IBinder onBind(Intent intent) {
       return mBinder;
    }
}
