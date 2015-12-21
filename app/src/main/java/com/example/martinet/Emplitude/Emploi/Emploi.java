package com.example.martinet.Emplitude.Emploi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;


public class Emploi extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, loadFichier, View.OnLongClickListener {
    private static final String PREFS_NAME = "Couleur";
    private static String[] heures = new String[]{"08h00", "09h00", "10h00", "11h00", "12h00", "13h00", "14h00", "15h00", "16h00", "17h00", "18h00", "19h00"};

    private int HEIGHT;
    private View view;
    private String dateJour;
    private SwipeRefreshLayout swipe;
    private Vector<Hashtable> cours;
    private RelativeLayout color;
    private Cour activeButton;
    private RelativeLayout picker;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private ScrollView scrollView;
    private GridLayout g;
    private ViewGroup.LayoutParams heureLayout;
    private FrameLayout l;
    private Button button_picker;
    private Toast toast;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Initialisation des variables

        this.view           = inflater.inflate(R.layout.emploi_du_temps, container, false);
        this.dateJour       = getArguments().getString("dateJour");
        this.scrollView     = (ScrollView) view.findViewById(R.id.scrollView3);
        this.settings       = getActivity().getSharedPreferences(PREFS_NAME, 0);
        this.l              = (FrameLayout) view.findViewById(R.id.frame);
        this.g              = (GridLayout) view.findViewById(R.id.heures);
        this.color          = (RelativeLayout) view.findViewById(R.id.color);
        this.picker         = (RelativeLayout) view.findViewById(R.id.picker);
        this.button_picker  = (Button) color.findViewById(R.id.color_5);
        this.swipe          = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        this.editor         = settings.edit();
        this.toast          = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        this.cours          = null;
        this.HEIGHT         = this.getHeight();

        this.scrollView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                MainActivity a = (MainActivity)getActivity();
                a.modifierDate(-1);
            }
            public void onSwipeLeft() {
                MainActivity a = (MainActivity)getActivity();
                a.modifierDate(1);
            }
            public boolean onTouch(View v, MotionEvent event) {
                color.setVisibility(View.GONE);
                return gestureDetector.onTouchEvent(event);
            }
        });

        //Définition du rechargement manuel

        this.swipe.setColorSchemeColors(getResources().getIntArray(R.array.spinner));
        this.swipe.setOnRefreshListener(this);

        //Titre de la toolbar
        this.getActivity().setTitle("Emploi du temps");

        //Appel des methodes d'affichages des différentes parties

        this.loadHeures();      //Chargement de la bar des heures
        this.loadCours();       //Chargement des différents boutons et de leur informations
        this.loadCouleurs();    //Chargement du module de couleur

        return this.view;
    }

    public void loadHeures(){
        this.heureLayout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HEIGHT / 12);

        TextView v;
        for (String h : heures) {
            v = new TextView(getContext());
            v.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            v.setLayoutParams(heureLayout);
            v.setText(h + "  —");
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
        Cour bouton;
        FrameLayout.LayoutParams layoutButton;
        int height_button, top;
        double minute;
        if(cours != null) {
            for (int i = 0; i < cours.size(); i++) {
                dateD = (Date) cours.get(i).get("dateD");
                dateF = (Date) cours.get(i).get("dateF");
                diff = dateF.getTime() - dateD.getTime();
                height_button = (int) ((this.HEIGHT / 12) * (diff / (1000.0 * 60 * 60)) + ECART);
                bouton = new Cour(i, cours.get(i).get("resumer") + "", getContext());
                layoutButton = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height_button);
                minute = (dateD.getMinutes()) / 60.0;
                top = (int) (((dateD.getHours() + minute) - 8) * this.HEIGHT / 12 + (ECART * 2));
                bouton.setOnClickListener(this);
                bouton.setOnLongClickListener(this);
                layoutButton.setMargins(10, top, 10, 10);
                bouton.setLayoutParams(layoutButton);

                Object c = couleur.get(cours.get(i).get("matiere"));
                if (c != null) {
                    int color = (int)c;
                    bouton.getBackground().setColorFilter(Integer.parseInt(c.toString()), PorterDuff.Mode.MULTIPLY);
                    bouton.setTextColor(getColorWB(color));
                }
                this.l.addView(bouton);
            }
        }
        if(cours == null){
            this.setToast("Le planning sur ADE n'a pas été chargé correctement");
        }else if(cours.size() ==0){
            this.setToast("Vous n'avez pas de cours aujourd'hui !");
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
                    String matiere = (String)cours.get(activeButton.getIndex()).get("matiere");
                    editor.putInt(matiere,colorBar[k]);
                    editor.commit();
                    MainActivity a = (MainActivity)getActivity();
                    MainActivity.premier = true;
                    a.modifierDate(0);
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
        Cour c = (Cour) v;
        int index = c.getIndex();
        Bundle objetbunble = new Bundle();
        Intent intent = new Intent(getContext(), Information.class);
        objetbunble.putString("matiere", this.cours.get(index).get("matiere") + "");
        objetbunble.putString("prof", this.cours.get(index).get("prof") + "");
        Date d = (Date) this.cours.get(index).get("dateD");
        Date d2 = (Date) this.cours.get(index).get("dateF");
        SimpleDateFormat h = new SimpleDateFormat("HH:mm");
        objetbunble.putString("dateD", h.format(d));
        objetbunble.putString("dateF", h.format(d2));
        objetbunble.putString("resumer", this.cours.get(index).get("resumer") + "");
        objetbunble.putString("salle", this.cours.get(index).get("salle") + "");
        intent.putExtras(objetbunble);
        this.startActivity(intent);
        this.getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void onRefresh() {
        if (isOnline()) {
            ADE_recuperation load = new ADE_recuperation(this);
            load.execute();
        } else {
            this.retour("Vous n'êtes pas connecté à internet !");
        }
    }

    public void retour(String value) {
        this.setToast(value);
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
        this.activeButton = (Cour) v;
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

                String matiere = (String)cours.get(activeButton.getIndex()).get("matiere");
                editor.putInt(matiere, mColorPickerView.getColor());
                editor.commit();
                MainActivity a = (MainActivity)getActivity();
                MainActivity.premier = true;
                a.modifierDate(0);

            } else if (id == R.id.annuler) {
                picker.setVisibility(View.GONE);
            }
        }

    }
}
