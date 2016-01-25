package com.example.martinet.Emplitude.Emploi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.martinet.Emplitude.Outil.Jour;
import com.example.martinet.Emplitude.R;
import com.example.martinet.Emplitude.Todo.Adapter;
import com.example.martinet.Emplitude.Todo.Ajouter;
import com.example.martinet.Emplitude.Todo.Tache;
import com.example.martinet.Emplitude.Todo.Todo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;


public class Information extends AppCompatActivity {

    final static private SimpleDateFormat h = new SimpleDateFormat("HH:mm");

    private ListView list;
    private Cour cour;
    private TextView aucune;
    private FloatingActionButton ajouter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.cour);
        Intent intent = getIntent();
        this.cour = (Cour) intent.getSerializableExtra("cour");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(cour.getResumer());
        TextView cour = (TextView) findViewById(R.id.infoCour);
        TextView ensei = (TextView) findViewById(R.id.infoEnsei);
        TextView salle = (TextView) findViewById(R.id.infoSalle);
        TextView heureD = (TextView) findViewById(R.id.infoHeureD);
        TextView heureF = (TextView) findViewById(R.id.infoHeureF);
        this.list = (ListView) findViewById(R.id.taches);
        this.aucune = (TextView) findViewById(R.id.aucune);
        this.ajouter = (FloatingActionButton) findViewById(R.id.fab);

        this.ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Ajouter.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        cour.setText(this.cour.getMatiere());
        ensei.setText(this.cour.getProf());
        salle.setText(this.cour.getSalle());
        heureD.setText(h.format(this.cour.getDateD()));
        heureF.setText(h.format(this.cour.getDateF()));
        this.creationListeTaches();
    }


    public void creationListeTaches (){
        Vector matiere =this.getTache(cour.getMatiere(), cour.getDateD());
        if (matiere.size() == 0){
            aucune.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }else {
            Collections.sort(matiere, new Comparator<Object>() {
                public int compare(Object m1, Object m2) {
                    Date d = ((Tache) m1).getDate();
                    Date d2 = ((Tache) m2).getDate();
                    return d.compareTo(d2);
                }
            });
            Adapter adapter = new Adapter(this, matiere);
            aucune.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            list.setAdapter(adapter);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
