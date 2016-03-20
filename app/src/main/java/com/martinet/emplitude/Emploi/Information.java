package com.martinet.emplitude.Emploi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.martinet.emplitude.Constants;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.MyApplication;
import com.martinet.emplitude.Outil.Fichier;
import com.martinet.emplitude.Outil.Jour;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Todo.Tache;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

/**
 * Classe permettant l'affichage complet du cours ainsi que sa liste de tâche associée
 */
public class Information extends AppCompatActivity {

    final static private SimpleDateFormat h = new SimpleDateFormat("HH:mm");

    private Cours cours;
    private TextView aucune;
    private FloatingActionButton ajouter;
    private LinearLayout taches;
    private Boolean first;
    private MyApplication application;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.emploi_cour);
        Intent intent = getIntent();
        this.first = intent.getBooleanExtra("FIRST", false);
        this.cours = (Cours) intent.getSerializableExtra("emploi_cour");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(cours.getResumer());
        final TextView cour = (TextView) findViewById(R.id.infoCour);
        this.application = (MyApplication) getApplicationContext();
        TextView ensei = (TextView) findViewById(R.id.infoEnsei);
        TextView salle = (TextView) findViewById(R.id.infoSalle);
        TextView heureD = (TextView) findViewById(R.id.infoHeureD);
        TextView heureF = (TextView) findViewById(R.id.infoHeureF);
        this.aucune = (TextView) findViewById(R.id.aucune);
        this.ajouter = (FloatingActionButton) findViewById(R.id.fab);
        this.taches = (LinearLayout) findViewById(R.id.taches);

        this.ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmploiAjouterTache.class);
                Bundle bundle = new Bundle();
                bundle.putLong("date", cours.getDateD().getTime());
                bundle.putSerializable("matiere",cours);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        cour.setText(this.cours.getMatiere());
        ensei.setText(this.cours.getProf());
        salle.setText(this.cours.getSalle());
        heureD.setText(h.format(this.cours.getDateD()));
        heureF.setText(h.format(this.cours.getDateF()));
        this.creationListeTaches();
    }


    /* Création de la liste de tâche associé*/
    public void creationListeTaches (){
        Vector matiere =this.getTache(cours.getMatiere(), cours.getDateD());
        taches.removeAllViews();
        if (matiere.size() == 0){
            aucune.setVisibility(View.VISIBLE);
        } else {
            Collections.sort(matiere, new Comparator<Object>() {
                public int compare(Object m1, Object m2) {
                    Date d = ((Tache) m1).getDate();
                    Date d2 = ((Tache) m2).getDate();
                    return d.compareTo(d2);
                }
            });

            aucune.setVisibility(View.GONE);

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;
            for(int i = 0; i<matiere.size(); i++){
                view = inflater.inflate(R.layout.emploi_tache, null, true);
                ((Button)(view.findViewById(R.id.tache))).setText(((Tache) matiere.get(i)).getNom());
                ((Button)(view.findViewById(R.id.tache))).setTransformationMethod(null);
                (view.findViewById(R.id.tache)).getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.DARKEN);
                (view.findViewById(R.id.tache)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
                taches.addView(view);
            }
        }
    }

    /*Si fermeture de l'activity*/
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
                finish();
                if(first){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Vector getTache(String matiere, Date date){
        Tache t;
        Vector liste = new Vector();
        Jour jour = new Jour(date);
        Jour jourTache;

        for(int i = 0; i< application.mesTaches.size(); i++){
            t = ((Tache)application.mesTaches.get(i));
            jourTache = new Jour(t.getDate());
            if(t.getCours().getMatiere().equals(matiere) && jourTache.getJour().equals(jour.getJour())){
                liste.add(t);
            }
        }
        return liste;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            this.creationListeTaches();
        }
    }

    /*Si fermeture de l'activity on enregistre la liste des tâches*/
    public void onStop(){
        Fichier.ecrireVector(Constants.TACHE_FILE, getApplicationContext(), application.mesTaches);
        super.onStop();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
