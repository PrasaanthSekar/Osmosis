package com.mindtree.osmosis.service;

/**
 * Created by M1032467 on 12/11/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.mindtree.osmosis.Home;
import com.mindtree.osmosis.R;

public class CheckWaterLevel extends Service {

    int count = 0;
    final static String GROUP_NOTI = "group_noti";
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10;

    public CheckWaterLevel() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {

                h.postDelayed(this, MIN_TIME_BW_UPDATES);
            }
        }, MIN_TIME_BW_UPDATES);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    private void notifyUser(String message) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_water)
                        .setContentTitle("Movement Tracker")
                        .setContentText(message)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                        .setGroup(GROUP_NOTI)
                        .setGroupSummary(true)
                        .setStyle(new NotificationCompat.InboxStyle());
        Intent resultIntent = new Intent(getApplicationContext(), Home.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(Home.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(count, mBuilder.build());
    }
}
