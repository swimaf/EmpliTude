package com.martinet.emplitude.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;

/**
 * Created by martinet on 06/12/15.
 */
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;


import com.martinet.emplitude.Global;
import com.martinet.emplitude.Initialization.Initialisation;
import com.martinet.emplitude.Schelude.Display.ActivityLesson;
import com.martinet.emplitude.Schelude.FragmentLesson;
import com.martinet.emplitude.Models.Lesson;
import com.martinet.emplitude.R;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Widget extends AppWidgetProvider {

    private Lesson lesson;
    private static SimpleDateFormat h = new SimpleDateFormat("HH:mm");
    private static final String PREFS_NAME = "Couleur";
    public static int i =0;
    private SharedPreferences settings;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        this.settings       = context.getSharedPreferences(PREFS_NAME,0);
        HashMap couleur = (HashMap) settings.getAll();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent intent;
        try {
            this.lesson = Lesson.getNext(Global.global);
        }catch (Exception ignore) {}

        if(this.lesson != null) {
            Object c = couleur.get(lesson.getDiscipline());
            String contenu = this.lesson.getSummary() + " \n" + this.lesson.getLocation() + "\n" + h.format(lesson.getDateBegin()) + " - " + h.format(lesson.getDateEnd());

            views.setTextViewText(R.id.cours, contenu);
            if (c != null) {
                views.setInt(R.id.cours, "setBackgroundColor", Integer.parseInt(c.toString()));
                views.setInt(R.id.cours, "setTextColor", FragmentLesson.getColorWB(Integer.parseInt(c.toString())));
            }

            Bundle objetbunble = new Bundle();
            intent = new Intent(context, ActivityLesson.class);
            objetbunble.putSerializable("emploi_cour", this.lesson);
            objetbunble.putBoolean("widget", true);
            intent.putExtras(objetbunble);
        }else{
            intent = new Intent(context, Initialisation.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.cours, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

}