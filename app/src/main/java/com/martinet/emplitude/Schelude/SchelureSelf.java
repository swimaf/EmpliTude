package com.martinet.emplitude.Schelude;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.martinet.emplitude.Alarm.ProgrammAlarm;
import com.martinet.emplitude.Alarm.ReveilActivity;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.Initialization.Initialisation;
import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Tool.EvenementInternet;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Tool.Response;

/**
 * CLASSZ QUI CE LANCE AUTOMATIQUEMENT SUIVANT LE NOMBRE DE JOUR DE RAFFRAICHISSEMENT DE L'UTLISATEUR
 */


public class SchelureSelf extends BroadcastReceiver implements Response<String, String>{

    final private static int TIME_RELOAD = 30*60*1000;
    final private static int TIME_SECOND = 24*60*60;

    private Context context;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    public void onReceive(Context context, Intent intent) {
        this.context = context;

        this.preference = context.getSharedPreferences(Constants.Preference.SCHELURE, 0);
        this.editor = preference.edit();

        //Si le smartphone démarre l'application lance une requete pour savoir quand mettre à jour l'emploi du temps
        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            ProgrammAlarm.setAlarm(context.getApplicationContext());
            this.refreshSchelure();
        }else{
            this.suivant();
        }
    }

    public void refreshSchelure(){
        long time = this.preference.getLong("time_next_refresh", 0);
        if(time - System.currentTimeMillis() < 0 ) {
            suivant();
        }else {
            Intent i = new Intent(context,SchelureSelf.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, i, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

    public void suivant(){
        if (!Constants.CONNECTED()) {
            //Si l'utilisateur n'est pas connecté à internet : lancement d'un receiver qui va ce déclancer lorsque l'utilisateur obtient internet
            receiver(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        } else {
            new Schedule().load(this);
        }
    }

    public void receiver(int i){
        ComponentName receiver = new ComponentName(context, EvenementInternet.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, i, PackageManager.DONT_KILL_APP);
    }


    @Override
    public void onSuccess(String value) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_event_light)
                        .setContentTitle("Empli'tude")
                        .setContentText("Mise à jour des derniers cours");
        Intent resultIntent = new Intent(context, Initialisation.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Initialisation.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(10, mBuilder.build());

        receiver(PackageManager.COMPONENT_ENABLED_STATE_DISABLED);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Global.global);
        long seconds = Integer.parseInt(preferences.getString("day_between_refresh", "15"))*TIME_SECOND;
        Intent i = new Intent(context,SchelureSelf.class);
        long timeNextRefresh = System.currentTimeMillis() + (seconds * 1000);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeNextRefresh, pendingIntent);
        this.editor.putLong("time_next_refresh", timeNextRefresh);
        this.editor.commit();

    }

    @Override
    public void onError(String message) {
        Intent i = new Intent(context,SchelureSelf.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TIME_RELOAD, pendingIntent);
    }
}
