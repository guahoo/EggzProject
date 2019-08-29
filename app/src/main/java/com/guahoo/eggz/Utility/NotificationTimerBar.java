package com.guahoo.eggz.Utility;

import android.app.Notification;
import android.app.NotificationChannel;
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
import static com.guahoo.eggz.Activity.MainActivity.mtimeleftminutes;


public class NotificationTimerBar {
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
//    long mtimeleftminutes;
    long START_TIME_IN_MILLIS;
    Context context;
    SharedPreferences sPrefs;
    InitString initString;
    String PREFERENCES;
    String timeLeftFormatted;
    String NOTIF_CHANNEL_ID;

    public NotificationTimerBar(Context context) {
        this.context = context;
    }


    public Notification getMyActivityNotification(String text){
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, new Intent(context, MainActivity.class), 0);
        return new Notification.Builder(context)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_icon_round)
                .setOngoing ( true )
                .setOnlyAlertOnce ( true )
                .setContentIntent(contentIntent).getNotification();
    }

    public void updateNotification() {
        int minutes = (int) mtimeleftminutes / 1000 / 60;
        int seconds = (int) mtimeleftminutes / 1000 % 60;
        String timeLeftFormatted = String.format ( Locale.getDefault (), "%02d:%02d", minutes, seconds );
        String text = timeLeftFormatted;
        Notification notification = getMyActivityNotification(text);
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService ( Context.NOTIFICATION_SERVICE );
        NotificationChannel channel = new NotificationChannel( "1", NOTIF_CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT );
        channel.setDescription ( "Channel description" );
        notificationManager.createNotificationChannel ( channel );
    }
    public void hideNotification(){

    }

}
