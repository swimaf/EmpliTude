package com.example.martinet.drawer.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;

/**
 * Created by martinet on 06/12/15.
 */
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.martinet.drawer.Accueil;
import com.example.martinet.drawer.Emploi.ADE_information;
import com.example.martinet.drawer.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

public class Widget extends AppWidgetProvider {

    private Hashtable cours;
    private static SimpleDateFormat h = new SimpleDateFormat("HH:mm");
    private static final String PREFS_NAME = "Couleur";

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        this.settings       = context.getSharedPreferences(PREFS_NAME,0);
        this.editor         = settings.edit();
        HashMap couleur = (HashMap) settings.getAll();

        try {
            ADE_information fichier = new ADE_information();
            this.cours = fichier.getLast();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date dateD = (Date) cours.get("dateD");
        Date dateF = (Date) cours.get("dateF");
        Object c = couleur.get(cours.get("matiere"));
        String contenu = this.cours.get("resumer")+" \n"+h.format(dateD)+" - "+h.format(dateF);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.cours, contenu);
        if (c != null) {
            views.setInt(R.id.cours, "setBackgroundColor", Integer.parseInt(c.toString()));
        }
        Intent intent = new Intent(context, Accueil.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.cours, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

}