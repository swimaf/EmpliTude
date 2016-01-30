package com.example.martinet.Emplitude.Emploi;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.Outil.Fichier;
import com.example.martinet.Emplitude.Outil.Jour;
import com.example.martinet.Emplitude.R;
import com.example.martinet.Emplitude.Todo.Ajouter;
import com.example.martinet.Emplitude.Todo.Tache;
import com.example.martinet.Emplitude.Todo.Todo;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;


public class Information extends AppCompatActivity {

    final static private SimpleDateFormat h = new SimpleDateFormat("HH:mm");

    private ListView list;
    private Cour cours;
    private TextView aucune;
    private FloatingActionButton ajouter;
    private LinearLayout taches;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.emploi_cour);
        Intent intent = getIntent();
        this.cours = (Cour) intent.getSerializableExtra("emploi_cour");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(cours.getResumer());
        final TextView cour = (TextView) findViewById(R.id.infoCour);
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
                bundle.putLong("date",cours.getDateD().getTime());
                bundle.putString("matiere",cours.getMatiere());
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
                ((Button)(view.findViewById(R.id.tache))).getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.DARKEN);
                taches.addView(view);
            }
        }
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

    public Vector getTache(String matiere, Date date){
        Tache t;
        Vector liste = new Vector();
        Jour jour = new Jour(date);
        Jour jourTache;
        for(int i=0; i<Todo.mesTaches.size(); i++){
            t = ((Tache)Todo.mesTaches.get(i));
            jourTache = new Jour(t.getDate());
            if(t.getMatiere().equals(matiere) && jourTache.getJour().equals(jour.getJour())){
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

    public void onStop(){
        Fichier.ecrireVector(Constants.tacheFile, getApplicationContext(), Todo.mesTaches);
        super.onStop();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
