package at.technikum_wien.cheers;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.util.List;

/**
 * Created by Nadja on 20.01.18.
 */

public class NotificationReceiver extends BroadcastReceiver {

    private static final String DRINK_REMINDER_NOTIFICATION_CHANNEL_ID = "drink_reminder_id";
    private static final int DRINK_REMINDER_NOTIFICATION_ID = 1;


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder oreoBuilder;
        NotificationCompat.Builder builder;

        //Bitmap Logo
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.title_screen);


        //Intent
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long when = System.currentTimeMillis();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(
                    DRINK_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.drinkReminderNotificationChannelID),
                    importance);

            notificationManager.createNotificationChannel(mChannel);

            oreoBuilder = new Notification.Builder(context, DRINK_REMINDER_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.arrow_up_float)
                    .setContentTitle("Cheers!")
                    .setContentIntent(pendingIntent)
                    .setContentText("Es ist wieder Zeit Spaß zu haben!")
                    //.setAutoCancel(true).setWhen(when)
                    .setAutoCancel(true);

            if(bitmap != null){
                oreoBuilder.setLargeIcon(bitmap);
            }

            notificationManager.notify(DRINK_REMINDER_NOTIFICATION_ID, oreoBuilder.build());

        }else{
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(android.R.drawable.arrow_up_float)
                    .setContentTitle("Cheers!")
                    .setContentText("Es ist wieder Zeit Spaß zu haben!")
                    .setContentIntent(pendingIntent)
                    //.setAutoCancel(true).setWhen(when)
                    .setAutoCancel(true);

            if(bitmap != null){
                builder.setLargeIcon(bitmap);
            }


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }

            notificationManager.notify(DRINK_REMINDER_NOTIFICATION_ID, builder.build());
        }


    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
