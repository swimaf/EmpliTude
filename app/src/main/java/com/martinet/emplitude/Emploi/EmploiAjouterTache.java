package com.martinet.emplitude.Emploi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.martinet.emplitude.MyApplication;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Todo.Tache;
import com.martinet.emplitude.Todo.Todo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by martinet on 30/01/16.
 */

/**
 * Classe permettant d'ajouter une tâche depuis l'emploi du temps
 */

public class EmploiAjouterTache extends AppCompatActivity {

    private static SimpleDateFormat h = new SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE);


    private FloatingActionButton suivant;
    private EditText nom;
    private Tache t;
    private Date date;
    private Cours matiere;
    private TextView tmatiere;
    private TextView tdate;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try{
            t = (Tache) intent.getSerializableExtra("Tache"); //Récuparation de la tache passée en parametre
            Bundle bundle =  intent.getExtras();
            date = new Date(bundle.getLong("date"));
            matiere = (Cours)bundle.getSerializable("matiere");
        }catch (Exception ignored){


        }


        this.setContentView(R.layout.emploi_ajouter_tache);
        suivant = (FloatingActionButton) findViewById(R.id.fab);
        nom = (EditText) findViewById(R.id.nom);
        tdate = (TextView) findViewById(R.id.tdate);
        tdate.setText(h.format(date));

        tmatiere = (TextView) findViewById(R.id.tmatiere);
        tmatiere.setText(matiere.getMatiere());


        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nom.getText().toString().equals("")) {
                    //Affichage d'une erreur si le nom de la tâche n'a pas été renseigné
                    Toast.makeText(getApplicationContext(), "Vous n'avez pas renseigné le champ !", Toast.LENGTH_SHORT).show();
                } else {
                    //Ajout de la tâche à la liste de de tâche
                    Tache tache = new Tache(nom.getText().toString(), matiere, date);
                    ((MyApplication)getApplicationContext()).mesTaches.add(tache);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
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

