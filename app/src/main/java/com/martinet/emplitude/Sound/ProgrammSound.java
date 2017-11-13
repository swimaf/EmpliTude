package com.martinet.emplitude.Sound;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.martinet.emplitude.Global;
import com.martinet.emplitude.Initialization.Initialisation;
import com.martinet.emplitude.Models.Lesson;
import com.martinet.emplitude.R;

/**
 * Created by martinet on 21/09/16.
 */

public class ProgrammSound {
    private ProgrammSound() {}

    public static void setReceiverOnLessonEnding(Context context, Lesson lesson) {
        context = context.getApplicationContext();
        if (lesson == null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, lesson.getDateEnd().getTime() - 600, getPendingIntent(context, OnLessonBegin.class));
            //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+30000, getPendingIntent(context, OnLessonEnding.class));
        }
    }

    public static void setReceiverOnLessonBegin(Context context) {
        context = context.getApplicationContext();
        Lesson lesson = Lesson.getNext(Global.global);
        if(lesson != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = getPendingIntent(context, OnLessonBegin.class, lesson);
            alarmManager.set(AlarmManager.RTC_WAKEUP, lesson.getDateBegin().getTime() - 600, pendingIntent);
            //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+30000, pendingIntent);
        }
    }


    public static void loadReceiver(Context context) {
        context = context.getApplicationContext();
        Lesson lesson = Lesson.getNext(Global.global);
        if(lesson != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, lesson.getDateBegin().getTime() - 600, getPendingIntent(context, OnLessonEnding.class));
            alarmManager.set(AlarmManager.RTC_WAKEUP, lesson.getDateEnd().getTime() - 600, getPendingIntent(context, OnLessonBegin.class));
        }
    }

    private static PendingIntent getPendingIntent(Context context, Class name) {
        Intent intent = new Intent(context, name);
        return PendingIntent.getBroadcast(context, 1, intent, 0);
    }
    private static PendingIntent getPendingIntent(Context context, Class name, Lesson lesson) {
        Intent intent = new Intent(context, name);
        intent.putExtra("lesson", lesson);
        return PendingIntent.getBroadcast(context, 1, intent, 0);
    }

    public static void cancelReceiver(Context context) {
        context = context.getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(getPendingIntent(context, OnLessonEnding.class));
        alarmManager.cancel(getPendingIntent(context, OnLessonBegin.class));
    }
}
