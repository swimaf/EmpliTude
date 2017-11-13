package com.martinet.emplitude.Todo;

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


import com.martinet.emplitude.Dialog.DialogPicker;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.Models.Lesson;
import com.martinet.emplitude.Models.Task;
import com.martinet.emplitude.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by piaud on 04/11/15.
 */
public class Ajouter extends AppCompatActivity {


    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    private FloatingActionButton suivant;
    private EditText nom, date;
    private Spinner matiere;
    private Task task;
    private int position;
    private Date datePicker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        try{
            task = (Task) intent.getSerializableExtra("Task");
            position = (int) intent.getSerializableExtra("position");
        }catch (Exception e){
            position = 0;
        }

        this.setContentView(R.layout.todo_ajouter);
        suivant = (FloatingActionButton) findViewById(R.id.fab);
        date = (EditText) findViewById(R.id.date);
        nom = (EditText) findViewById(R.id.nom);
        matiere = (Spinner) findViewById(R.id.matiere);

        if (task != null) {
            nom.setText(task.getNom());

            date.setText(simpleDateFormat.format(task.getDate()));
            datePicker = task.getDate();
            ArrayList<String> listLessons = new ArrayList<>();
            listLessons.add("Aucune");
            List<Lesson> lessons = Lesson.getLessonsByDay(Global.global, datePicker);
            if(lessons != null) {
                for (Lesson lesson : lessons) {
                    if (lesson.getDiscipline() != null) {
                        listLessons.add(lesson.getDiscipline());
                    }
                }
            }
            ArrayAdapter departement = new ArrayAdapter<>(Ajouter.this, android.R.layout.simple_spinner_dropdown_item, listLessons);
            matiere.setAdapter(departement);
        } else {
            ArrayList<String> listLessons = new ArrayList();
            listLessons.add("Aucune");
            ArrayAdapter departement = new ArrayAdapter<>(Ajouter.this, android.R.layout.simple_spinner_dropdown_item, listLessons);
            matiere.setAdapter(departement);
        }


        date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogPicker dialogPicker = DialogPicker.newInstance(new Date(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, view.getYear());
                        calendar.set(Calendar.MONTH, view.getMonth());
                        calendar.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                        datePicker = calendar.getTime();
                        date.setText(simpleDateFormat.format(datePicker));
                        ArrayList<String> listLessons = new ArrayList<>();
                        listLessons.add("Aucune");
                        List<Lesson> lessons = Lesson.getLessonsByDay(Global.global, datePicker);
                        if(lessons != null) {
                            for (Lesson lesson : lessons) {
                                if (lesson.getDiscipline() != null) {
                                    listLessons.add(lesson.getDiscipline());
                                }
                            }
                        }
                        ArrayAdapter departement = new ArrayAdapter<>(Ajouter.this, android.R.layout.simple_spinner_dropdown_item, listLessons);
                        matiere.setAdapter(departement);
                    }
                });
                dialogPicker.show(getFragmentManager(), "Select day");
            }
        });




        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datePicker == null || nom.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Tous les champs n'ont pas été renseigné !", Toast.LENGTH_SHORT).show();
                }else{
                    if (task != null){
                        Global.global.getTasks().remove(position);
                    }
                    Task task = new Task(nom.getText().toString(), matiere.getSelectedItem().toString(), datePicker);
                    Global.global.getTasks().add(task);
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

