package com.octrinsic.medicalapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class CheckForRefferrals extends Service {

    NotificationCompat.Builder notification;
    private static final int uniqueID = 1;
    DatabaseHelper dbHelper;
    Cursor c = null;

    public CheckForRefferrals() {
        dbHelper = new DatabaseHelper(getApplicationContext());
       c = dbHelper.getReferrals(Doctor.nationalPassportId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(c != null && (c.getCount() > 0)) {
            intent = new Intent(this, RefferralView.class);
            notification = new NotificationCompat.Builder(this);
            notification.setSmallIcon(R.drawable.mims_logo);
            notification.setTicker("Referral");
            notification.setWhen(System.currentTimeMillis());
            notification.setContentTitle("MyAfya");
            notification.setContentText("You have a referral notification");
            notification.setAutoCancel(true);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(uniqueID, notification.build());
            return START_STICKY;
        }
        return 0;
    }
}
