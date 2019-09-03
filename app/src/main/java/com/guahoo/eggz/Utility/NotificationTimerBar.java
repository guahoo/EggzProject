package com.guahoo.eggz.Utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.guahoo.eggz.Activity.MainActivity;
import com.guahoo.eggz.R;

import java.util.Locale;

import static com.guahoo.eggz.Activity.MainActivity.START_TIME_IN_MILLIS;
import static com.guahoo.eggz.Activity.MainActivity.mtimeleftminutes;


public class NotificationTimerBar {

    Context context;
    String NOTIF_CHANNEL_ID;
    RemoteViews remoteViews;
    private PendingIntent contentIntent;
    int progress;
    NotificationManager notificationManager;

    public NotificationTimerBar(Context context) {
        this.context = context;
        remoteViews = new RemoteViews(context.getPackageName(),R.layout.notification_progress_bar);
        notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        initChannels(context);


    }



    public Notification getMyActivityNotification(){
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, new Intent(context, MainActivity.class), 0);
        remoteViews.setTextViewText(R.id.timeView_notificationBar,timeLeftFormatted());

        return new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_icon_round)
                .setContent(remoteViews)
                .setOngoing(true)
                .setContentIntent(contentIntent).getNotification();


    }


    public void updateNotification() {
        Notification notification = null;

            notification = getMyActivityNotification();

        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager != null) {
            mNotificationManager.notify(1, notification);
        }
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
        assert notificationManager != null;
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
        remoteViews.setProgressBar(R.id.progressBar,(int)START_TIME_IN_MILLIS,0,false);
    }
    public void maxProgress(){
        remoteViews.setProgressBar(R.id.progressBar,100,100,false);

    }

}
