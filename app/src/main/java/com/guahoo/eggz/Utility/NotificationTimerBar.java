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
    String NOTIF_CHANNEL_ID="1";
    RemoteViews remoteViews;
    private PendingIntent contentIntent;
    int progress;
    NotificationManager notificationManager;

    public NotificationTimerBar(Context context) {
        this.context = context;
        remoteViews = new RemoteViews(context.getPackageName(),R.layout.notification_progress_bar);
        notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);




    }



    public Notification getMyActivityNotification(){
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, new Intent(context, MainActivity.class), 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_icon_round)
                    .setContent(remoteViews)
                    .setOngoing(true)
                    .setChannelId(NOTIF_CHANNEL_ID)
                    .setContentIntent(contentIntent).getNotification();

        }else {

            return new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_icon_round)
                    .setContent(remoteViews)
                    .setOngoing(true)
                    .setContentIntent(contentIntent).getNotification();
        }




    }


    public void updateNotification() {
        Notification notification = null;

        notification = getMyActivityNotification();

        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager != null) {
            mNotificationManager.notify(1, notification);
        }
        progress= (int)( START_TIME_IN_MILLIS-mtimeleftminutes);
        remoteViews.setProgressBar(R.id.progressBar,(int)START_TIME_IN_MILLIS,progress,false);
        remoteViews.setTextViewText(R.id.timeView_notificationBar,timeLeftFormatted());





    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Eggz";
            String description = "EggzNotificationChannel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
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
        remoteViews.setTextViewText(R.id.timeView_notificationBar,"Bon appetit!");
        remoteViews.setProgressBar(R.id.progressBar,100,100,false);

    }


}
