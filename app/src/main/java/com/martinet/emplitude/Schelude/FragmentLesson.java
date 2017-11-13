package com.martinet.emplitude.Schelude;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.Models.Lesson;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Schelude.Display.ActivityLesson;
import com.martinet.emplitude.Schelude.Display.ActivitySchelude;
import com.martinet.emplitude.Todo.Todo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class FragmentLesson extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private static final String PREFS_NAME = "Couleur";

    private int HEIGHT;
    private int WIDTH;
    private Date dateJour;
    private List<Lesson> lessons;
    private Button activeButton;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private FrameLayout flLessons;
    private RelativeLayout rlNoLesson;

    public static FragmentLesson newInstance(Date date) {
        FragmentLesson myFragment = new FragmentLesson();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        myFragment.setArguments(args);
        return myFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.emploi_du_jour, container, false);
        this.dateJour = (Date) (getArguments().getSerializable("date"));
        this.settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        this.flLessons = (FrameLayout) view.findViewById(R.id.frame);
        this.editor = settings.edit();
        this.HEIGHT = Constants.getHeight(getContext())+100;
        this.WIDTH = Constants.getWidth(getContext()) - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65 , getResources().getDisplayMetrics());;
        this.rlNoLesson = (RelativeLayout) view.findViewById(R.id.vide);
        this.loadCours();
        return view;
    }



    public void loadCours() {
        lessons = Lesson.getLessonsByDay((Global) getActivity().getApplication(), this.dateJour);
        List<Lesson> lessonsForDisplay = new ArrayList<>();
        lessonsForDisplay.addAll(lessons);

        if (lessons == null) {
            Toast.makeText(getContext(), "Echec lors de la récupération de l'emploi du temps", Toast.LENGTH_SHORT).show();
        } else if (lessons.size() == 0) {

            this.rlNoLesson.setVisibility(View.VISIBLE);
            this.rlNoLesson.getLayoutParams().height = HEIGHT;

        } else {
            for (Lesson element: lessons) {
                if(lessonsForDisplay.contains(element)) {
                    List<Lesson> lessonAtSameTime = nbLessonAtSameTime(element);
                    int index = 0;
                    for (Lesson lesson:lessonAtSameTime) {
                        displayButtonLesson(lesson, lessonAtSameTime.size(), index);
                        ++index;
                    }
                    lessonsForDisplay.removeAll(lessonAtSameTime);
                }
            }
        }
    }

    public void displayButtonLesson(Lesson lesson, int indexOfLessonsAtSameTime, int index) {
        int top, color;
        final int ECART = (int) (HEIGHT / (60.0));
        final HashMap couleur = (HashMap) settings.getAll();

        Date dateD = lesson.getDateBegin();
        Date dateF = lesson.getDateEnd();
        long diff = dateF.getTime() - dateD.getTime();
        int heightButton = (int) ((this.HEIGHT / 12) * (diff / (1000.0 * 60 * 60)) + ECART);
        Button bouton = new Button(getContext());
        bouton.setText(lesson.getSummary());
        bouton.setOnClickListener(this);
        bouton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

        FrameLayout.LayoutParams layoutButton = new FrameLayout.LayoutParams(WIDTH / indexOfLessonsAtSameTime, heightButton);
        double minute = (dateD.getMinutes()) / 60.0;
        top = (int) (((dateD.getHours() + minute) - 8) * this.HEIGHT / 12 + (ECART * 2));
        bouton.setOnLongClickListener(this);
        layoutButton.setMargins(10+((WIDTH/indexOfLessonsAtSameTime)*index), top, 10, 10);
        bouton.setLayoutParams(layoutButton);
        bouton.setTag(lessons.indexOf(lesson));
        Object c = couleur.get(lesson.getDiscipline());
        if (c != null) {
            color = (int) c;
            bouton.getBackground().setColorFilter(Integer.parseInt(c.toString()), PorterDuff.Mode.DARKEN);
            bouton.setTextColor(getColorWB(color));
        } else {
            bouton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.DARKEN);
        }
        this.flLessons.addView(bouton);
    }


    public List<Lesson> nbLessonAtSameTime(Lesson lesson) {
        final long lessonBegin = lesson.getDateBegin().getTime();
        final long lessonEnd = lesson.getDateEnd().getTime();
        Constants.Predicate<Lesson> predicate = new Constants.Predicate<Lesson>() {
            @Override
            public boolean apply(Lesson value) {
                long valueBegin = value.getDateBegin().getTime();
                long valueEnd = value.getDateEnd().getTime();
                return (lessonBegin <= valueBegin && lessonEnd > valueBegin ) || (valueEnd > lessonBegin && valueEnd < lessonEnd) || (lessonBegin >= valueBegin && lessonEnd < valueEnd);
            }
        };
        List<Lesson> lessonsAtSameTime = Constants.filter(lessons, predicate);
        return lessonsAtSameTime;
    }

    public void setColorButton(int color){
        String matiere = lessons.get(flLessons.indexOfChild(activeButton)).getDiscipline();
        editor.putInt(matiere, color);
        editor.commit();
        reloadJour();
    }


    public void onClick(View v) {
        Bundle objetbunble = new Bundle();
        Intent intent = new Intent(getContext(), ActivityLesson.class);
        objetbunble.putSerializable("emploi_cour", this.lessons.get(Integer.parseInt(v.getTag().toString())));
        intent.putExtras(objetbunble);
        this.startActivity(intent);

        this.getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            ((MainActivity)getActivity()).changeFragment(null, Todo.class);
        }
    }

    public void reloadJour(){
        ActivitySchelude e = (ActivitySchelude) ((MainActivity) getActivity()).getFragment();
        e.refresh();
    }

    @Override
    public boolean onLongClick(View v) {
        ((ActivitySchelude)((MainActivity) getActivity()).getFragment()).colorBarVisibility(View.VISIBLE);
        this.activeButton = (Button) v;
        return true;
    }

    public static int getBrightness(int color) {
        return (int) Math.sqrt(Color.red(color) * Color.red(color) * .241 +
                Color.green(color) * Color.green(color) * .691 +
                Color.blue(color) * Color.blue(color) * .068);
    }

    public static int getColorWB(int color) {
        return (getBrightness(color) < 130) ? Color.WHITE : Color.BLACK;
    }

}