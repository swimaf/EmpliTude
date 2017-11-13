package com.martinet.emplitude.Schelude.Display;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.Models.Lesson;
import com.martinet.emplitude.Models.Task;
import com.martinet.emplitude.R;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Classe permettant l'onSuccess complet du jour ainsi que sa liste de tâche associée
 */
public class ActivityLesson extends AppCompatActivity {

    final static private SimpleDateFormat h = new SimpleDateFormat("HH:mm");

    private Lesson lesson;
    private TextView aucune;
    private FloatingActionButton ajouter;
    private LinearLayout taches;
    private Boolean widget;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.emploi_cour);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.lesson = (Lesson) getIntent().getSerializableExtra("emploi_cour");
        this.widget = getIntent().getBooleanExtra("widget", false);
        this.getSupportActionBar().setTitle(lesson.getSummary());

        HashMap<String, String> fields = new LinkedHashMap<>();
        fields.put("Matière :", lesson.getDiscipline());
        fields.put("Début du cours :", h.format(this.lesson.getDateBegin()));
        fields.put("Fin du cours :", h.format(this.lesson.getDateEnd()));
        fields.put("Salle :", lesson.getLocation());
        fields.put("Personnel :", lesson.getPersonnel());
        fields.put("Remarques :", lesson.getNote());

        for(String field:fields.keySet()) {
            if(fields.get(field) != null && !"".equals(fields.get(field)) ) {
                View view = LayoutInflater.from(this).inflate(R.layout.lesson_information, null);
                ((TextView) view.findViewById(R.id.field)).setText(field);
                ((TextView) view.findViewById(R.id.value)).setText(fields.get(field));
                LinearLayout llLesson = (LinearLayout) findViewById(R.id.llLessons);
                llLesson.addView(view);
            }
        }

        this.aucune = (TextView) findViewById(R.id.aucune);
        this.ajouter = (FloatingActionButton) findViewById(R.id.fab);
        this.taches = (LinearLayout) findViewById(R.id.taches);

        this.ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityAddTask.class);
                Bundle bundle = new Bundle();
                bundle.putLong("date",lesson.getDateBegin().getTime());
                bundle.putString("matiere",lesson.getDiscipline());
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        this.creationListeTaches();
    }


    public void creationListeTaches (){
        List<Task> tasks = Task.getTasksByLesson(lesson.getDiscipline(), lesson.getDateBegin());
        taches.removeAllViews();

        if (tasks.size() == 0){
            aucune.setVisibility(View.VISIBLE);
        } else {
            aucune.setVisibility(View.GONE);
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;
            for(int i = 0; i<tasks.size(); i++){
                view = inflater.inflate(R.layout.emploi_tache, null, true);
                ((Button)(view.findViewById(R.id.task))).setText(tasks.get(i).getNom());
                ((Button)(view.findViewById(R.id.task))).setTransformationMethod(null);
                (view.findViewById(R.id.task)).getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.DARKEN);
                /*(view.findViewById(R.id.task)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });*/
                taches.addView(view);
            }
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
                finish();
                if(widget) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            this.creationListeTaches();
        }
    }

}
