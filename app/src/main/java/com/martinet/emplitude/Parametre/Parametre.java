package com.martinet.emplitude.Parametre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.martinet.emplitude.Accueil;
import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Outil.Fichier;
import com.martinet.emplitude.R;


public class Parametre extends AppCompatActivity implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.parametre);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Spinner maj = (Spinner) findViewById(R.id.maj);
        Spinner notification = (Spinner) findViewById(R.id.notif);

        ArrayAdapter<String> majAdapte = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.maj));
        maj.setAdapter(majAdapte);

        ArrayAdapter<String> notifAdapte = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.notif));
        notification.setAdapter(notifAdapte);

    }

    public void supprimerProfil(View v) {
        if(Constants.CONNECTED(getApplicationContext())) {
            Fichier.delete(Constants.identifiantFile, getBaseContext());
            Intent intent = new Intent(Parametre.this, Accueil.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(Parametre.this, "Vous devez être connecté à internet !", Toast.LENGTH_SHORT).show();
        }
    }

    public void supprimerCouleur(View v) {
        SharedPreferences settings = Parametre.this.getSharedPreferences("Couleur", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
        Toast.makeText(Parametre.this, "Couleurs supprimées", Toast.LENGTH_SHORT).show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.overridePendingTransition(android.R.anim.fade_out,android.R.anim.fade_in);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onClick(View v) {}
}