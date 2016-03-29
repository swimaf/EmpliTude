package com.martinet.emplitude.Emploi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.martinet.emplitude.Constants;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Todo.Todo;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;


public class JourEmploi extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private int HEIGHT;
    private Date dateJour;
    private Vector<Cours> cours;
    private Button activeButton;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private GridLayout grille;
    private FrameLayout liste_cours;
    private RelativeLayout vide;

    public void setJour(Date date){
        this.dateJour =date;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Initialisation des variables

        View view = inflater.inflate(R.layout.emploi_du_jour, container, false);
        this.settings = getActivity().getSharedPreferences(Constants.PREFERENCE_COULEUR, 0);
        this.liste_cours = (FrameLayout) view.findViewById(R.id.frame);
        this.grille = (GridLayout) view.findViewById(R.id.heures);
        this.vide = (RelativeLayout) view.findViewById(R.id.vide);
        this.editor = settings.edit();
        this.cours = null;
        this.HEIGHT = Constants.getHeight(getContext())+100;

        //Appel des methodes d'affichages des différentes parties

        this.loadHeures();      //Chargement de la bar des heures
        this.loadCours();       //Chargement des différents boutons et de leur informations

        return view;
    }

    /*Génération de la liste des heures*/
    public void loadHeures() {
        int hauteur= HEIGHT/12;
        TextView heureJournee;
        String[] heures = getResources().getStringArray(R.array.heures);

        for (String h : heures) {
            heureJournee = new TextView(getContext());
            heureJournee.setGravity(Gravity.CENTER_VERTICAL);
            heureJournee.setHeight(hauteur);
            heureJournee.setWidth(HEIGHT);
            heureJournee.setBackgroundResource(R.drawable.border_heure);
            heureJournee.setText(h);
            this.grille.addView(heureJournee);
        }
    }

    public void loadCours() {
        ADE_information fichier = new ADE_information(this.dateJour, getContext());
        this.cours = fichier.getCours();

        int ECART = (int) (HEIGHT / (60.0));
        HashMap couleur = (HashMap) settings.getAll();
        Date dateD, dateF;
        long diff;
        Button bouton;
        FrameLayout.LayoutParams layoutButton;
        int height_button, top, color;
        double minute;
        Calendar calendar = Calendar.getInstance(Locale.FRANCE);

        if (cours != null) {
            for (int i = 0; i < cours.size(); i++) {
                dateD = cours.get(i).getDateD();
                dateF = cours.get(i).getDateF();
                calendar.setTime(dateD);

                diff = dateF.getTime() - dateD.getTime();
                height_button = (int) ((this.HEIGHT / 12) * (diff / (1000.0 * 60 * 60)) + ECART);
                bouton = new Button(getContext());
                bouton.setText(cours.get(i).getResumer());
                layoutButton = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height_button);
                minute = (calendar.get(Calendar.MINUTE)) / 60.0;
                top = (int) (((calendar.get(Calendar.HOUR_OF_DAY) + minute) - 8) * this.HEIGHT / 12 + (ECART * 2));
                bouton.setOnClickListener(this);
                bouton.setOnLongClickListener(this);
                layoutButton.setMargins(10, top, 10, 10);
                bouton.setLayoutParams(layoutButton);
                Object c = couleur.get(cours.get(i).getMatiere());
                if (c != null) {
                    color = (int) c;
                    bouton.getBackground().setColorFilter(Integer.parseInt(c.toString()), PorterDuff.Mode.DARKEN);
                    bouton.setTextColor(Constants.getColorWB(color));
                } else {
                    bouton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.DARKEN);
                }
                this.liste_cours.addView(bouton);
            }
        }
        if (cours == null) {
            Toast.makeText(getContext(), "Le planning sur ADE n'a pas été chargé correctement", Toast.LENGTH_SHORT).show();
        } else if (cours.size() == 0) {
            this.vide.setVisibility(View.VISIBLE);
            this.vide.getLayoutParams().height = HEIGHT;
        }
    }

    /*Mise à jour de la couleur d'une matière*/
    public void setColorButton(int color){
        String matiere = cours.get(liste_cours.indexOfChild(activeButton)).getMatiere();
        editor.putInt(matiere, color);
        editor.commit();
        reloadJour();
    }


    /*Clic sur un cours*/
    public void onClick(View v) {

        Bundle objetbunble = new Bundle();
        Intent intent = new Intent(getContext(), Information.class);
        objetbunble.putSerializable("emploi_cour", this.cours.get(liste_cours.indexOfChild(v)));
        intent.putExtras(objetbunble);
        this.startActivityForResult(intent, 0);

        this.getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            ((MainActivity)getActivity()).changeFragment(null, Todo.class);
        }
    }


    /*Rechargement du jour*/
    public void reloadJour(){
        Emploi e = (Emploi) ((MainActivity) getActivity()).getFragment();
        e.refresh();
    }

    /*Pression longue sur un cours*/
    public boolean onLongClick(View v) {
        ((Emploi)((MainActivity) getActivity()).getFragment()).colorBarVisibility(View.VISIBLE);
        this.activeButton = (Button) v;
        return true;
    }



}