package com.martinet.emplitude;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.martinet.emplitude.Emploi.ADE_automatique;
import com.martinet.emplitude.Outil.Fichier;
import com.martinet.emplitude.Outil.Jour;

import java.util.Date;


/**
 * Classe qui est ouverte la première elle permet de savoir si c'est la première connexion de l'utilisation
 */

public class Introduction extends Activity implements View.OnClickListener {

    private Button suivant;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.preference = getSharedPreferences(Constants.PREFERENCE_ADE, 0);
        this.editor = preference.edit();

        //Verifie si le fichier d'identification de l'utilisateur existe
        if(Fichier.existe(Constants.IDENTIFIANT_FILE, getBaseContext())){
            rafraichirAuto();
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }else {
            this.setContentView(R.layout.introduction);
            this.suivant = (Button)this.findViewById(R.id.suivant);
            this.suivant.setOnClickListener(this);
        }


    }

    /* Lors du clic sur le bouton "suivant"*/
    public void onClick(View v) {
        if(Constants.CONNECTED(getBaseContext())) {
            Intent intent = new Intent(this, Accueil.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }else{
            Toast.makeText(getApplicationContext(), "Vous devez être connecté à internet !", Toast.LENGTH_SHORT).show();
        }
    }

    /*Permet d'initialiser la prochaine mise à jour auto de l'emploi du temps en fonction du délai choisie dans les parametres*/
    public void rafraichirAuto(){
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        long time = this.preference.getLong("time", 0);
        if(time - System.currentTimeMillis() < 0 ) {
            Jour jour = new Jour(new Date());
            jour.ajouterJour(7);
            long seconds = this.preference.getInt("rafraichissement", 7)*50;
            this.editor.putLong("time", System.currentTimeMillis()+seconds*1000);
            this.editor.commit();

            Intent intent = new Intent(getApplicationContext(), ADE_automatique.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), pendingIntent);
        }
    }
}