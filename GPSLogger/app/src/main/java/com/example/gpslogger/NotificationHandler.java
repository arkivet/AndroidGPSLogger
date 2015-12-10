package com.example.gpslogger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

public class NotificationHandler extends AppCompatActivity {
    private Context context;
    private Notification notification = null;
    private int clicked;

    public NotificationHandler(){

    }

    public NotificationHandler(Context context, int clicked){
        super();
        this.context = context;
        this.clicked = clicked;
    }

    public void createNotification(){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        switch(clicked){
            case 0:
                notification = new Notification.Builder(context.getApplicationContext())
                        .setContentTitle("GPS Logger")
                        .setContentText("Logging Active")
                        .setContentIntent(pendingNotificationIntent)
                        .setSmallIcon(R.mipmap.ic_launcher).build();
                break;
            case 1:
                notification = new Notification.Builder(context.getApplicationContext())
                        .setContentTitle("GPS Logger")
                        .setContentText("Logging Inactive")
                        .setContentIntent(pendingNotificationIntent)
                        .setSmallIcon(R.mipmap.ic_launcher).build();
                break;
            case 2:
                notification = new Notification.Builder(context.getApplicationContext())
                        .setPriority(Notification.PRIORITY_MIN).build();
                break;
        }
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.priority = Notification.PRIORITY_MAX;
        notificationManager.notify(0, notification);
    }
}
