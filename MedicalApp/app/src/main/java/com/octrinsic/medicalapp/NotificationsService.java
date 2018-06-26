package com.octrinsic.medicalapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationsService extends Service {
    NotificationCompat.Builder notification;
    private static final int uniqueID = 5;
    DatabaseHelper dbHelper;
    public NotificationsService() {
    }

    @Override
    public void onCreate() {
        dbHelper = new DatabaseHelper(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean areThereNofications = dbHelper.checkForNotifications(Patient.nationalpassportid);
        if(areThereNofications) {
            showNotification(intent);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void showNotification(Intent intent){
        intent = new Intent(this, NotificationInfo.class);
        notification = new NotificationCompat.Builder(getApplicationContext());
        notification.setSmallIcon(R.drawable.ic_launcher);
        notification.setTicker("Appointment");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("MyAfya");
        notification.setContentText("You have an appointment message");
        notification.setAutoCancel(true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
    }
}
