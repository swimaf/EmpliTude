package com.example.martinet.Emplitude.Emploi;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.example.martinet.Emplitude.Accueil;
import com.example.martinet.Emplitude.MainActivity;
import com.example.martinet.Emplitude.Outil.EvenementInternet;
import com.example.martinet.Emplitude.R;

/**
 * Created by martinet on 02/01/16.
 */

public class ADE_automatique extends BroadcastReceiver implements ADE_retour{

    final private static String PREFS_NAME = "Ade";

    private Context context;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        //Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        //vibrator.vibrate(2000);

        this.preference = context.getSharedPreferences(PREFS_NAME, 0);
        this.editor = preference.edit();

        if(!connected(context)){
            receiver(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        }else{
            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_event_light)
                            .setContentTitle("Empli'tude")
                            .setContentText("Mise à jour auto effectué");
            Intent resultIntent = new Intent(context, Accueil.class);


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(Accueil.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());



            receiver(PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        }
    }

    public void receiver(int i){
        ComponentName receiver = new ComponentName(context, EvenementInternet.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, i, PackageManager.DONT_KILL_APP);
    }
    @Override
    public void retour(int value) {
        if(value != ADE_recuperation.ERROR_ADE){
            int seconds = 20;
            Intent i = new Intent(context,ADE_automatique.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 123456789, i, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), pendingIntent);
        }
    }

    public Boolean connected(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }
}
