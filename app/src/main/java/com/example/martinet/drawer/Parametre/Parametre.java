package com.example.martinet.drawer.Parametre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.martinet.drawer.Accueil;
import com.example.martinet.drawer.Emploi.Information;
import com.example.martinet.drawer.R;

import java.io.File;

/**
 * Created by martinet on 13/11/15.
 */
public class Parametre extends AppCompatActivity {
    final private String store = System.getenv("EXTERNAL_STORAGE") ;
    final private File file = new File(this.store+"/.identifiant.txt");

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.parametre);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button supprimer = (Button)findViewById(R.id.supprimer);
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file.delete();
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