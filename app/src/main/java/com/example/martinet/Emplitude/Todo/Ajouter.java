package com.example.martinet.Emplitude.Todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.Emploi.Cour;
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
    private ImageButton dateButton;
    private Tache t;
    private int position;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        try{
            t = (Tache) intent.getSerializableExtra("Tache");
            position = (int) intent.getSerializableExtra("position");
        }catch (Exception e){

        }

        this.setContentView(R.layout.todo_ajouter);
        suivant = (FloatingActionButton) findViewById(R.id.fab);
        date = (EditText) findViewById(R.id.date);
        nom = (EditText) findViewById(R.id.nom);
        matiere = (EditText) findViewById(R.id.matiere);

        if (t != null) {
            nom.setText(t.getNom());
            date.setText(t.getDate().toString());
            matiere.setText(t.getMatiere());
        }
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t != null){
                    Todo.mesTaches.remove(position);
                }
                Tache t = new Tache(nom.getText().toString(), matiere.getText().toString(), new Date(date.getText().toString()));
                Todo.mesTaches.add(t);
                setResult(Activity.RESULT_OK);
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

