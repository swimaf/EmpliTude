package com.example.martinet.Emplitude;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.martinet.Emplitude.Emploi.ADE_automatique;
import com.example.martinet.Emplitude.Emploi.Jour;
import com.example.martinet.Emplitude.Outil.Fichier;

import java.io.File;
import java.util.Date;


/**
 * Created by martinet on 16/11/15.
 */
public class Introduction extends Activity implements View.OnClickListener {

    final private static String PREFS_NAME = "Ade";

    private Button suivant;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.preference = getSharedPreferences(PREFS_NAME, 0);
        this.editor = preference.edit();

        if(Fichier.existe(Constants.identifiantFile, getBaseContext())){
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
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClick(View v) {
        if(isOnline()) {
            Intent intent = new Intent(this, Accueil.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }else{
            Toast.makeText(getApplicationContext(), "Vous devez être connecté à internet !", Toast.LENGTH_SHORT).show();
        }
    }

    public void rafraichirAuto(){
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Date date = new Date(this.preference.getLong("time", 0));
        Date now = new Date();
        System.out.println(date.getTime() - now.getTime());
        if(!this.preference.contains("maj") || date.before(now)) {
            Jour jour = new Jour(new Date());
            jour.ajouterJour(7);
            System.out.println("ssss");
            //long seconds = this.preference.getInt("rafraichissement", 7)*24*60*60;
            long seconds = this.preference.getInt("rafraichissement", 7)*20;
            this.editor.putLong("time", jour.getDate().getTime()+seconds*1000);
            this.editor.commit();

            Intent intent = new Intent(getApplicationContext(), ADE_automatique.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (seconds * 1000), pendingIntent);
        }
    }

}