package com.martinet.emplitude.Emploi;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.martinet.emplitude.Accueil;
import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Outil.EvenementInternet;
import com.martinet.emplitude.R;


public class ADE_automatique extends BroadcastReceiver implements ADE_retour{

    final private static String PREFS_NAME = "Ade";

    private Context context;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    public void onReceive(Context context, Intent intent) {
        this.context = context;

        this.preference = context.getSharedPreferences(PREFS_NAME, 0);
        this.editor = preference.edit();
        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            long time = this.preference.getLong("time", 0);
            if(time - System.currentTimeMillis() < 0 ) {
                suivant();
            }else {
                Intent i = new Intent(context,ADE_automatique.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, i, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }else{
            this.suivant();
        }
    }

    public void suivant(){
        if (!Constants.CONNECTED(context)) {
            receiver(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        } else {
            ADE_recuperation load = new ADE_recuperation(this, context);
            load.execute();
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


            long seconds = this.preference.getInt("rafraichissement", 7)*24*60*60;
            //long seconds = this.preference.getInt("rafraichissement", 7)*2;
            Intent i = new Intent(context,ADE_automatique.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, i, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), pendingIntent);
            this.editor.putLong("time", System.currentTimeMillis()+seconds*1000);
            this.editor.commit();

        }else{
            long seconds = 30*60;
            Intent i = new Intent(context,ADE_automatique.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, i, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), pendingIntent);
        }
    }


}
