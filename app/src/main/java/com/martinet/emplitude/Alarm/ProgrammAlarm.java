package com.martinet.emplitude.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.Models.Lesson;

import java.util.Date;

/**
 * Created by root on 23/01/16.
 */
public class ProgrammAlarm {

    private ProgrammAlarm() {}

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, 0);

        return pendingIntent;
    }

    public static void setAlarm(Context context) {
        ProgrammAlarm.cancelAlarm(context);
        SharedPreferences preference_alarm = Global.global.getSharedPreferences(Constants.Preference.ALARM, 0);
        if(preference_alarm.getBoolean(ReveilActivity.KEY_ACTIVATION, true)) {
            Lesson lesson = Lesson.getNextLessonForAlarm();
            if(lesson != null) {
                set(context,  lesson.getDateBegin().getTime()-(preference_alarm.getInt(ReveilActivity.KEY_PREPARATION_TIME, 45)*60*1000));
                //set(context,  System.currentTimeMillis()+2000);
            }
        }
    }

    private static void set(Context context, long time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, lesson.getDateBegin().getTime()-(preparationTime*60*1000), pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(time, getPendingIntent(context));
            alarmManager.setAlarmClock(alarmClockInfo, getPendingIntent(context));
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, getPendingIntent(context));
        }
    }
    public static void reporterAlarm(Context context, long time) {
        ProgrammAlarm.cancelAlarm(context);
        set(context, System.currentTimeMillis()+time);
    }

    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(getPendingIntent(context));
        }
    }


}
