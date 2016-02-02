package com.example.martinet.Emplitude.Widget;

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

import com.example.martinet.Emplitude.Accueil;
import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.Emploi.ADE_information;
import com.example.martinet.Emplitude.Emploi.Cour;
import com.example.martinet.Emplitude.Emploi.Information;
import com.example.martinet.Emplitude.Emploi.JourEmploi;
import com.example.martinet.Emplitude.Outil.Fichier;
import com.example.martinet.Emplitude.R;
import com.example.martinet.Emplitude.Todo.Todo;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Widget extends AppWidgetProvider {

    private Cour cours;
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
        Todo.mesTaches = Fichier.readAll(Constants.tacheFile, context);
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
                views.setInt(R.id.cours, "setTextColor", JourEmploi.getColorWB(Integer.parseInt(c.toString())));
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