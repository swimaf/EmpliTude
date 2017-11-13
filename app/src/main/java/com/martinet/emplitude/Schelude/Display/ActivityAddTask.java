package com.martinet.emplitude.Schelude.Display;

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

import com.martinet.emplitude.Global;
import com.martinet.emplitude.Models.Task;
import com.martinet.emplitude.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by martinet on 30/01/16.
 */

/**
 * Classe permettant d'ajouter une tâche depuis l'emploi du temps
 */

public class ActivityAddTask extends AppCompatActivity {

    private static SimpleDateFormat h = new SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE);


    private FloatingActionButton suivant;
    private EditText nom;
    private Task t;
    private Date date;
    private String matiere;
    private TextView tmatiere;
    private TextView tdate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try{
            t = (Task) intent.getSerializableExtra("Task");
            Bundle bundle =  intent.getExtras();
            date = new Date(bundle.getLong("date"));
            matiere = bundle.getString("matiere");
        }catch (Exception ignored){}


        this.setContentView(R.layout.emploi_ajouter_tache);
        suivant = (FloatingActionButton) findViewById(R.id.fab);
        nom = (EditText) findViewById(R.id.nom);
        tdate = (TextView) findViewById(R.id.tdate);
        tdate.setText(h.format(date));

        tmatiere = (TextView) findViewById(R.id.tmatiere);
        tmatiere.setText(matiere);


        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nom.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Vous n'avez pas renseigné le champ !", Toast.LENGTH_SHORT).show();
                } else {
                    Task task = new Task(nom.getText().toString(), matiere, date);
                    Global.global.getTasks().add(task);
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

}

