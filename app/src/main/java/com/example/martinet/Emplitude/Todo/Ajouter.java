package com.example.martinet.Emplitude.Todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.MainActivity;
import com.example.martinet.Emplitude.Outil.Fichier;
import com.example.martinet.Emplitude.R;

import java.util.Date;
import java.util.Vector;


/**
 * Created by piaud on 04/11/15.
 */
public class Ajouter extends AppCompatActivity {


    private FloatingActionButton suivant;
    private EditText nom, date, matiere;
    private Vector<Object> listeTaches;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.todo_ajouter);
        suivant = (FloatingActionButton) findViewById(R.id.fab);
        nom = (EditText) findViewById(R.id.nom);
        date = (EditText) findViewById(R.id.date);
        matiere = (EditText) findViewById(R.id.matiere);

        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tache t = new Tache(nom.getText().toString(), matiere.getText().toString(), new Date(date.getText().toString()));
                listeTaches = Fichier.readAll(Constants.tacheFile, getBaseContext());
                listeTaches.add(t);
                Fichier.ecrireVector(Constants.tacheFile, getBaseContext(), listeTaches);
                ((Todo)((MainActivity) getParent()).getFragment()).rafraichir(listeTaches);
                finish();
            }
        });
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

