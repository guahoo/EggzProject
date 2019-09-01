package com.guahoo.eggz.Utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.guahoo.eggz.Activity.MainActivity;
import com.guahoo.eggz.R;

import java.util.Locale;

import static com.guahoo.eggz.Activity.MainActivity.START_TIME_IN_MILLIS;
import static com.guahoo.eggz.Activity.MainActivity.mtimeleftminutes;


public class NotificationTimerBar {
    NotificationCompat.Builder builder;
    //long START_TIME_IN_MILLIS;
    Context context;
    SharedPreferences sPrefs;
    InitString initString;
    String PREFERENCES;
   // String timeLeftFormatted;
    String NOTIF_CHANNEL_ID;
    RemoteViews remoteViews;
    private PendingIntent contentIntent;
    int progress;
    NotificationManager notificationManager;

    public NotificationTimerBar(Context context) {
        this.context = context;
        remoteViews = new RemoteViews(context.getPackageName(),R.layout.notification_progress_bar);
        remoteViews.setOnClickPendingIntent(R.id.root,contentIntent);
        notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public Notification getMyActivityNotification(String text){
        Intent closeButton = new Intent("Pause");
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, new Intent(context, MainActivity.class), 0);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, closeButton, 0);
        remoteViews.setTextViewText(R.id.timeView_notificationBar,timeLeftFormatted());
        remoteViews.setOnClickPendingIntent(R.id.button_pause_notify,pendingSwitchIntent);



        return new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_icon_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_icon_round))
                .setContent(remoteViews)
                .setOngoing(false)
                .setOnlyAlertOnce(false)
                .setContentIntent(contentIntent).getNotification();


    }


    public void updateNotification() {
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            notification = getMyActivityNotification(timeLeftFormatted());
        }
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notification);
        remoteViews.setTextViewText(R.id.timeView_notificationBar,timeLeftFormatted());
        progress= (int)( START_TIME_IN_MILLIS-mtimeleftminutes);

        remoteViews.setProgressBar(R.id.progressBar,(int)START_TIME_IN_MILLIS,progress,false);


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
        notificationManager.cancelAll();

    }
    public String  timeLeftFormatted(){
        int minutes = (int) mtimeleftminutes / 1000 / 60;
        int seconds = (int) mtimeleftminutes / 1000 % 60;
        return String.format ( Locale.getDefault (), "%02d:%02d", minutes, seconds );

    }
    public void resetProgress(){
        progress=0;
    }

}
