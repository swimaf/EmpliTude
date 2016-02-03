package com.example.martinet.Emplitude.Todo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.martinet.Emplitude.Emploi.ADE_information;
import com.example.martinet.Emplitude.Emploi.Cour;
import com.example.martinet.Emplitude.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;


/**
 * Created by piaud on 04/11/15.
 */
public class Ajouter extends AppCompatActivity {


    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    private FloatingActionButton suivant;
    private EditText nom, date;
    private Spinner matiere;
    private Tache t;
    private int position;
    private Date datePicker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        try{
            t = (Tache) intent.getSerializableExtra("Tache");
            position = (int) intent.getSerializableExtra("position");
        }catch (Exception e){
            position = 0;
        }

        this.setContentView(R.layout.todo_ajouter);
        suivant = (FloatingActionButton) findViewById(R.id.fab);
        date = (EditText) findViewById(R.id.date);
        nom = (EditText) findViewById(R.id.nom);
        matiere = (Spinner) findViewById(R.id.matiere);

        if (t != null) {
            nom.setText(t.getNom());

            date.setText(simpleDateFormat.format(t.getDate()));
            datePicker = t.getDate();
            ADE_information ade = new ADE_information(t.getDate(), getApplicationContext());
            Vector<Cour> v = ade.getCours();
            ArrayList listeCour = new ArrayList();
            listeCour.add("Aucune");
            for (int i = 0; i < v.size(); i++) {
                listeCour.add(v.get(i).getMatiere());
            }
            ArrayAdapter departement = new ArrayAdapter<>(Ajouter.this, android.R.layout.simple_spinner_dropdown_item, listeCour);
            matiere.setAdapter(departement);
            matiere.setSelection(listeCour.indexOf(t.getMatiere()));
        }

        ArrayList listeCour = new ArrayList();
        listeCour.add("Aucune");
        ArrayAdapter departement = new ArrayAdapter<>(Ajouter.this, android.R.layout.simple_spinner_dropdown_item, listeCour);
        matiere.setAdapter(departement);

        date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogDate dialogDate = DialogDate.newInstance(new Date(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        datePicker = new Date(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                        date.setText(simpleDateFormat.format(datePicker));
                        ADE_information ade = new ADE_information(datePicker, getApplicationContext());
                        Vector<Cour> v = ade.getCours();
                        ArrayList listeCour = new ArrayList();
                        listeCour.add("Aucune");
                        for (int i = 0; i < v.size(); i++) {
                            listeCour.add(v.get(i).getMatiere());
                        }
                        ArrayAdapter departement = new ArrayAdapter<>(Ajouter.this, android.R.layout.simple_spinner_dropdown_item, listeCour);
                        matiere.setAdapter(departement);
                    }
                });
                dialogDate.show(getFragmentManager(), "s");
            }
        });




        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nom.getText() == null || datePicker == null){
                    Toast.makeText(getApplicationContext(), "Tous les champs n'ont pas été renseigné !", Toast.LENGTH_SHORT).show();
                }else{
                    if (t != null){
                        System.out.println(position);
                        Todo.mesTaches.remove(position);
                    }
                    Tache tache = new Tache(nom.getText().toString(), matiere.getSelectedItem().toString(), datePicker);
                    Todo.mesTaches.add(tache);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
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

