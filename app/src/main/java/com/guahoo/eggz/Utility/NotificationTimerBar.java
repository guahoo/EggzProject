package com.guahoo.eggz.Utility;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.guahoo.eggz.Activity.MainActivity;
import com.guahoo.eggz.R;

import java.util.Locale;
import java.util.Objects;

import static android.app.Notification.DEFAULT_ALL;
import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;


public class NotificationTimerBar {
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    long mtimeleftminutes;
    long START_TIME_IN_MILLIS;
    Context context;
    SharedPreferences sPrefs;
    InitString initString;
    String PREFERENCES;
    String timeLeftFormatted;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public NotificationTimerBar(Context context) {
        this.context=context;
        initString = new InitString();
        sPrefs = context.getSharedPreferences (PREFERENCES, MODE_PRIVATE );
        String ns = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager) context.getSystemService(ns);
        builder=new NotificationCompat.Builder ( context );
        mtimeleftminutes = MainActivity.mtimeleftminutes;
        int minutes = (int) mtimeleftminutes / 1000 / 60;
        int seconds = (int) mtimeleftminutes / 1000 % 60;
        timeLeftFormatted = String.format ( Locale.getDefault (), "%02d:%02d", minutes, seconds );
        sendNotification();
        updateNotification();


    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendNotification(){
        Intent notificationIntent = new Intent ( context, MainActivity.class )
                .addFlags( Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP );
        PendingIntent pendingIntent = PendingIntent.getActivity ( context, 0,
                notificationIntent, 0 );





        builder
                .setSmallIcon (R.drawable.ic_icon_round )
                .setPriority (PRIORITY_HIGH )
                .setOngoing ( false )
                .setOnlyAlertOnce ( false )
                .setDefaults( DEFAULT_ALL )
                .setContentTitle ( timeLeftFormatted )
                .setContentIntent( pendingIntent);
        notificationManager.notify ( 1,builder.build () );



    }
    public void updateNotification(){
        int minutes = (int) mtimeleftminutes / 1000 / 60;
        int seconds = (int) mtimeleftminutes / 1000 % 60;
        String timeLeftFormatted = String.format ( Locale.getDefault (), "%02d:%02d", minutes, seconds );
        builder.setContentTitle(timeLeftFormatted );
        notificationManager.notify(1, builder.build());
    }

    public Thread updateProgressThread = new Thread(){
        public void run(){
            for (int i=0;i<10;i++){
                builder.setProgress(10,i,false);
            }
        }
    };

}
