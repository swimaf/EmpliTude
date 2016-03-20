package com.martinet.emplitude.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;


import com.martinet.emplitude.Accueil;
import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Emploi.ADE_information;
import com.martinet.emplitude.Emploi.Cours;
import com.martinet.emplitude.Emploi.Information;
import com.martinet.emplitude.R;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Génère le widget qui affiche le prochain cours
 */

public class Widget extends AppWidgetProvider {

    private static SimpleDateFormat h = new SimpleDateFormat("HH:mm");

    private Cours cours;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFERENCE_COULEUR, Context.MODE_PRIVATE);
        HashMap couleur = (HashMap) settings.getAll();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent intent;
        try {
            ADE_information fichier = new ADE_information(context);
            this.cours = fichier.getNext();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(this.cours != null) {
            Object c = couleur.get(cours.getMatiere());
            String contenu = this.cours.getResumer() + " \n" + this.cours.getSalle() + "\n" + h.format(cours.getDateD()) + " - " + h.format(cours.getDateF());

            views.setTextViewText(R.id.cours, contenu);
            if (c != null) {
                views.setInt(R.id.cours, "setBackgroundColor", Integer.parseInt(c.toString()));
                views.setInt(R.id.cours, "setTextColor", Constants.getColorWB(Integer.parseInt(c.toString())));
            }

            Bundle objetbunble = new Bundle();
            intent = new Intent(context, Information.class);
            objetbunble.putSerializable("emploi_cour", this.cours);
            objetbunble.putBoolean("FIRST", true);
            intent.putExtras(objetbunble);
        }else{
            intent = new Intent(context, Accueil.class);
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