package com.martinet.emplitude.Parametre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.martinet.emplitude.Accueil;
import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Outil.Fichier;
import com.martinet.emplitude.R;


public class Parametre extends AppCompatActivity implements View.OnClickListener {

    final private static String PREFS_NAME = "Ade";

    private SharedPreferences settingPreference;
    private SharedPreferences.Editor settingPreferenceEdit;
    private int rafraichissement;
    private int delaiNotif;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.parametre);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.settingPreference = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        this.settingPreferenceEdit = settingPreference.edit();

        Spinner maj = (Spinner) findViewById(R.id.maj);
        Spinner notification = (Spinner) findViewById(R.id.notif);
        this.rafraichissement = this.settingPreference.getInt("rafraichissement", 7);

        ArrayAdapter<String> majAdapte = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.maj));
        maj.setAdapter(majAdapte);
        maj.setSelection(getPositionMAJ());
        maj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPositionRaffraichissement(position);
                settingPreferenceEdit.putInt("rafraichissement", rafraichissement);
                settingPreferenceEdit.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> notifAdapte = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.notif));
        notification.setAdapter(notifAdapte);

    }

    public void getPositionRaffraichissement(int position){
        switch (position) {
            case 0: rafraichissement = 1; break;
            case 1: rafraichissement = 3; break;
            case 2: rafraichissement = 7; break;
            case 3: rafraichissement = 14; break;
            default:break;
        }
    }

    public int getPositionMAJ(){
        int val = 0;
        switch (this.rafraichissement) {
            case 1: val = 0; break;
            case 3: val = 1; break;
            case 7: val = 2; break;
            case 14: val = 3; break;
            default:break;
        }
        return val;
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