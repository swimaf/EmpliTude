package com.martinet.emplitude.Todo;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;

import com.martinet.emplitude.R;

/**
 * Created by Flo on 09/03/2016.
 */
public class GenerationTaches extends BroadcastReceiver {

    //Nom du fichier sharedPreference
    final private static String PREFS_NAME = "Ade";

    private Context context;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    public void retour(int value) {

            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_event_light)
                            .setContentTitle("Empli'tude")
                            .setContentText("Mise à jour auto effectué");

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());

            long seconds = this.preference.getInt("rafraichissement", 7)*24*60*60;
            Intent i = new Intent(context,GenerationTaches.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, i, 0);
            PendingIntent pendingIntent1 = PendingIntent.
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), pendingIntent);
            alarmManager.
            this.editor.putLong("time", System.currentTimeMillis()+seconds*1000);
            this.editor.commit();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
