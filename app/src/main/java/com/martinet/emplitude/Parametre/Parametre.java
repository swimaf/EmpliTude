package com.martinet.emplitude.Parametre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.martinet.emplitude.Accueil;
import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Outil.Fichier;
import com.martinet.emplitude.R;


public class Parametre extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.parametre);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Spinner maj = (Spinner) findViewById(R.id.maj);
        Spinner notification = (Spinner) findViewById(R.id.notif);

        ArrayAdapter<String> majAdapte = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.maj));
        maj.setAdapter(majAdapte);

        ArrayAdapter<String> notifAdapte = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.notif));
        notification.setAdapter(notifAdapte);

        Button supprimer = (Button)findViewById(R.id.supprimer);
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fichier.delete(Constants.identifiantFile, getBaseContext());
                Intent intent = new Intent(Parametre.this, Accueil.class);
                startActivity(intent);
                finish();
            }
        });
        Button couleur = (Button)findViewById(R.id.couleur);
        couleur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = Parametre.this.getSharedPreferences("Couleur", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Parametre.this, "Couleurs supprimées", Toast.LENGTH_SHORT).show();
            }
        });
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
}