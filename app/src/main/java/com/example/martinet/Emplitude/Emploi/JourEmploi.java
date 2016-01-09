package com.example.martinet.Emplitude.Emploi;

/**
 * Created by martinet on 09/01/16.
 */

import android.support.v4.widget.SwipeRefreshLayout;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinet.Emplitude.MainActivity;
import com.example.martinet.Emplitude.Outil.OnSwipeTouchListener;
import com.example.martinet.Emplitude.R;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


public class JourEmploi extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, ADE_retour, View.OnLongClickListener {
    private static final String PREFS_NAME = "Couleur";

    private int HEIGHT;
    private Date dateJour;
    private SwipeRefreshLayout swipe;
    private Vector<Cours> cours;
    private RelativeLayout color;
    private btnCour activeButton;
    private RelativeLayout picker;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private ScrollView scrollView;
    private GridLayout g;
    private ViewGroup.LayoutParams heureLayout;
    private FrameLayout l;
    private Button button_picker;
    private Toast toast;
    private RelativeLayout vide;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Initialisation des variables

        View view           = inflater.inflate(R.layout.emploi_du_temps, container, false);
        this.dateJour       = new Date(getArguments().getLong("dateJour"));
        this.scrollView     = (ScrollView) view.findViewById(R.id.scrollView3);
        this.settings       = getActivity().getSharedPreferences(PREFS_NAME, 0);
        this.l              = (FrameLayout) view.findViewById(R.id.frame);
        this.g              = (GridLayout) view.findViewById(R.id.heures);
        this.color          = (RelativeLayout) view.findViewById(R.id.color);
        this.vide           = (RelativeLayout) view.findViewById(R.id.vide);
        this.picker         = (RelativeLayout) view.findViewById(R.id.picker);
        this.button_picker  = (Button) color.findViewById(R.id.color_5);
        this.swipe          = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        this.editor         = settings.edit();
        this.toast          = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        this.cours          = null;
        this.HEIGHT         = this.getHeight();

        this.scrollView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                Emploi e = (Emploi) ((MainActivity) getActivity()).getFragment();
                e.modifierDate(-1);
            }

            public void onSwipeLeft() {
                Emploi e = (Emploi) ((MainActivity) getActivity()).getFragment();
                e.modifierDate(1);
            }

            public boolean onTouch(View v, MotionEvent event) {
                color.setVisibility(View.GONE);
                return gestureDetector.onTouchEvent(event);
            }
        });


        //Définition du rechargement manuel

        this.swipe.setColorSchemeColors(getResources().getIntArray(R.array.spinner));
        this.swipe.setOnRefreshListener(this);

        //Appel des methodes d'affichages des différentes parties

        this.loadHeures();      //Chargement de la bar des heures
        this.loadCours();       //Chargement des différents boutons et de leur informations
        this.loadCouleurs();    //Chargement du module de couleur

        return view;
    }

    public void loadHeures(){
        this.heureLayout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HEIGHT / 12);

        TextView v;
        String[] heures = getResources().getStringArray(R.array.heures);
        for (String h : heures) {
            v = new TextView(getContext());
            v.setGravity(Gravity.CENTER_VERTICAL);
            v.setLayoutParams(heureLayout);
            v.setBackgroundResource(R.drawable.border_heure);
            v.setText(h);
            this.g.addView(v);
        }
    }

    public void loadCours(){
        try {
            ADE_information fichier = new ADE_information(this.dateJour);
            this.cours = fichier.getCours();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int ECART = (int) (HEIGHT / (60.0));
        HashMap couleur = (HashMap) settings.getAll();
        Date dateD, dateF;
        long diff;
        btnCour bouton;
        FrameLayout.LayoutParams layoutButton;
        int height_button, top, color;
        double minute;
        if(cours != null) {
            for (int i = 0; i < cours.size(); i++) {
                dateD = cours.get(i).getDateD();
                dateF = cours.get(i).getDateF();
                diff = dateF.getTime() - dateD.getTime();
                height_button = (int) ((this.HEIGHT / 12) * (diff / (1000.0 * 60 * 60)) + ECART);
                bouton = new btnCour(i, cours.get(i).getResumer(), getContext());
                layoutButton = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height_button);
                minute = (dateD.getMinutes()) / 60.0;
                top = (int) (((dateD.getHours() + minute) - 8) * this.HEIGHT / 12 + (ECART * 2));
                bouton.setOnClickListener(this);
                bouton.setOnLongClickListener(this);
                layoutButton.setMargins(10, top, 10, 10);
                bouton.setLayoutParams(layoutButton);
                Object c = couleur.get(cours.get(i).getMatiere());
                if (c != null) {
                    color = (int)c;
                    bouton.getBackground().setColorFilter(Integer.parseInt(c.toString()), PorterDuff.Mode.DARKEN);
                    bouton.setTextColor(getColorWB(color));
                }else{
                    bouton.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.DARKEN);
                }
                this.l.addView(bouton);
            }
        }
        if(cours == null){
            this.setToast("Le planning sur ADE n'a pas été chargé correctement");
        }else if(cours.size() ==0){
            this.vide.setVisibility(View.VISIBLE);
            this.vide.getLayoutParams().height = HEIGHT;
        }
    }

    public void loadCouleurs(){
        this.picker.bringToFront();
        this.picker.findViewById(R.id.annuler).setOnClickListener(new DialogPicker());
        this.picker.findViewById(R.id.valider).setOnClickListener(new DialogPicker());
        final int[] colorBar = getResources().getIntArray(R.array.colorBar);
        int id;
        Vector<Button> button = new Vector<>();
        for (int i = 1; i < 5; i++) {
            id = getResources().getIdentifier("color_" + i, "id", getActivity().getPackageName());
            button.add((Button) color.findViewById(id));
            button.get(i - 1).getBackground().setColorFilter(colorBar[i - 1], PorterDuff.Mode.MULTIPLY);
            button.get(i - 1).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    LinearLayout ll = (LinearLayout) color.findViewById(R.id.color_linear);
                    int k = ll.indexOfChild(b);
                    String matiere = cours.get(activeButton.getIndex()).getMatiere();
                    editor.putInt(matiere, colorBar[k]);
                    editor.commit();
                }
            });
        }
        button_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color.setVisibility(View.GONE);
                picker.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setToast(String value){
        toast.setText(value);
        toast.show();
    }

    public int getHeight(){
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        if (width > height) {
            height = width - 150;
        } else {
            height = height - 150;
        }
        return height;
    }

    public void onClick(View v) {
        btnCour c = (btnCour) v;
        int index = c.getIndex();
        Bundle objetbunble = new Bundle();
        Intent intent = new Intent(getContext(), Information.class);
        objetbunble.putSerializable("cour", this.cours.get(index));
        intent.putExtras(objetbunble);
        this.startActivity(intent);
        this.getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void onRefresh() {
        if (isOnline()) {
            ADE_recuperation load = new ADE_recuperation(this);
            load.execute();
        } else {
            ADE_recuperation.INFO = "Vous n'êtes pas connecté à internet !";
            this.retour(ADE_recuperation.ERROR_INTERNET);
        }
    }

    public void retour(int value) {
        this.setToast(ADE_recuperation.INFO);
        swipe.setRefreshing(false);
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public boolean onLongClick(View v) {
        this.color.setVisibility(View.VISIBLE);
        this.activeButton = (btnCour) v;
        return true;
    }

    public static int getBrightness(int color){
        return (int)Math.sqrt( Color.red(color) * Color.red(color) * .241 +
                Color.green(color) * Color.green(color) * .691 +
                Color.blue(color) * Color.blue(color) * .068);
    }

    public static int getColorWB(int color){
        if(getBrightness(color) < 130){
            return Color.WHITE;
        }else {
            return Color.BLACK;
        }
    }

    private class DialogPicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button click = (Button) v;
            int id = click.getId();

            if (id == R.id.valider) {
                ColorPickerView	mColorPickerView;
                mColorPickerView = (ColorPickerView) picker.findViewById(R.id.colorpickerview__color_picker_view);

                activeButton.getBackground().setColorFilter(mColorPickerView.getColor(), PorterDuff.Mode.MULTIPLY);

                String matiere = cours.get(activeButton.getIndex()).getMatiere();
                editor.putInt(matiere, mColorPickerView.getColor());
                editor.commit();
                /*MainActivity a = (MainActivity)getActivity();
                MainActivity.premier = true;
                a.modifierDate(0);*/

            } else if (id == R.id.annuler) {
                picker.setVisibility(View.GONE);
            }
        }

    }
}
