package com.martinet.emplitude;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.martinet.emplitude.Alarm.ProgrammAlarm;
import com.martinet.emplitude.Models.Lesson;
import com.martinet.emplitude.Models.Student;
import com.martinet.emplitude.Models.Task;
import com.martinet.emplitude.Schelude.SchelureSelf;
import com.martinet.emplitude.Tool.Fichier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinet on 17/08/16.
 */

public class Global extends Application
{
    public static Global global;

    private List<Lesson> lessons;
    private List<Task> tasks;
    private Student student;

    @Override
    public void onCreate() {
        super.onCreate();
        initVariables();
        Global.global = this;
    }

    public void initVariables() {
        super.onCreate();
        student = (Student) Fichier.readObject(Constants.Files.STUDENT, getApplicationContext(), 0);
        if (student != null) {
            lessons = new Fichier<Lesson>().readObjects(student.getDefaultGroup().getName() + ".cours", getApplicationContext());
        }
        tasks = new Fichier<Task>().readObjects(Constants.Files.TASK, getApplicationContext());
        if(tasks == null) {
            tasks = new ArrayList<>();
        }
        Global.global = this;
        ProgrammAlarm.setAlarm(this);
        loadSchelureSelf();
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Student getStudent() {
        return student;
    }

    public void removeStudent() {
        Fichier.delete(Constants.Files.STUDENT, getBaseContext());
        Global.global.student = null;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void onTrimMemory(int level) {
        new Fichier<Task>().write(Constants.Files.TASK, tasks, this);
        super.onTrimMemory(level);
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


    private void loadSchelureSelf() {
        SharedPreferences preference = Global.global.getSharedPreferences(Constants.Preference.SCHELURE, 0);
        SharedPreferences.Editor editor = preference.edit();
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        long time =preference.getLong("time_next_refresh", 0);
        if(time - System.currentTimeMillis() < 0 ) {
            long seconds =  Integer.parseInt(preference.getString("day_between_refresh", "15"))*24*60*60;
            editor.putLong("time_next_refresh", System.currentTimeMillis()+seconds*1000);
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), SchelureSelf.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), pendingIntent);
        }
    }
}