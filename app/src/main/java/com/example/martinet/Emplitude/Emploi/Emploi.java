package com.example.martinet.Emplitude.Emploi;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.Outil.OnSwipeTouchListener;
import com.example.martinet.Emplitude.R;
import java.util.Date;
import java.util.Vector;


public class Emploi extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ADE_retour {

    private long CONST_DURATION_OF_DAY = 1000 * 60 * 60 * 24;

    public static Jour calendrier = new Jour(new Date());
    private View viewAction;
    private FragmentActivity activity;
    private Fragment fragment;
    private ScrollView swiper;
    private SwipeRefreshLayout swipe;
    private Toast toast;
    private RelativeLayout color;
    private Button button_picker;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.getActivity().setTitle("Emploi du temps");
        this.activity = getActivity();
        View view = inflater.inflate(R.layout.emploi_du_temps, container, false);

        this.swiper      = (ScrollView) view.findViewById(R.id.swiper);
        this.toast  = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        this.swipe  = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        this.color = (RelativeLayout) view.findViewById(R.id.color);
        this.button_picker = (Button) color.findViewById(R.id.color_5);

        //Ajout du listener le mise a jour auto
        this.swipe.setColorSchemeColors(getResources().getIntArray(R.array.spinner));
        this.swipe.setOnRefreshListener(this);

        //Changement de la toolbar avec l'action precedent et suivant
        changeToolbar();
        this.loadCouleurs();    //Chargement du module de couleur

        //Ajout du listener lors slider vers la droite ou la gauche
        this.swiper.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                modifierDate(-1);
            }

            public void onSwipeLeft() {
                modifierDate(1);
                colorBarVisibility(View.GONE);
            }

            public boolean onTouch(View v, MotionEvent event) {
                colorBarVisibility(View.GONE);
                return gestureDetector.onTouchEvent(event);
            }
        });
        //Placer devant tout le monde
        this.swiper.bringToFront();
        return view;
    }

    public void changeToolbar(){
        ViewGroup tool = (ViewGroup) getActivity().findViewById(R.id.tool);
        LayoutInflater vi = LayoutInflater.from(getContext());
        View v = vi.inflate(R.layout.emploi_action, null);
        this.viewAction = v;
        tool.addView(v);
        modifierDate(0);
        ImageButton pre = (ImageButton) v.findViewById(R.id.precedent);
        ImageButton next = (ImageButton) v.findViewById(R.id.suivant);
        pre.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifierDate(-1);
            }
        });
        next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifierDate(1);
            }
        });
    }

    public void modifierDate(int i){
        calendrier.ajouterJour(i);
        if(!calendrier.est_inferieur()) {

            long diff = Math.abs(calendrier.getDate().getTime() - System.currentTimeMillis());
            int nombre_jour = (int)(diff/CONST_DURATION_OF_DAY);

            if(nombre_jour < 14){

                TextView jour = (TextView) viewAction.findViewById(R.id.jour);
                jour.setText(calendrier.getJour());

                Fragment fragment = null;
                Class fragmentClass = JourEmploi.class;
                Bundle args = new Bundle();
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    args.putLong("dateJour", calendrier.getDate().getTime());
                    fragment.setArguments(args);
                } catch (Exception e) {
                    System.out.println("Erreur load fragment");
                }
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                if (i != 0) {
                    if (i < 0) {
                        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } else {
                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
                this.fragment = fragment;
                ft.replace(R.id.reloader, fragment);
                ft.commit();
            }else{
                calendrier.ajouterJour(-i);
                Toast.makeText(activity.getApplicationContext(), "Vous ne pouvez pas aller plus loin !", Toast.LENGTH_SHORT).show();
            }

        }else{
            calendrier.ajouterJour(-i);
            Toast.makeText(activity.getApplicationContext(), "Vous ne pouvez pas aller par ici !", Toast.LENGTH_SHORT).show();
        }
    }

    public Fragment getFragment(){
        return fragment;
    }

    public void onDestroyView(){
        super.onDestroyView();
        ViewGroup tool = (ViewGroup) getActivity().findViewById(R.id.tool);
        tool.removeView(viewAction);
    }

    public void onRefresh() {
        if (Constants.CONNECTED(getContext())) {
            ADE_recuperation load = new ADE_recuperation(this, getContext());
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

    public void setToast(String value) {
        toast.setText(value);
        toast.show();
    }

    public void loadCouleurs() {

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
                    colorBarVisibility(View.GONE);
                    ((JourEmploi) fragment).setColorButton(colorBar[k]);
                }
            });
        }
        button_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorBarVisibility(View.GONE);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ColorPicker editNameDialog = new ColorPicker();
                editNameDialog.show(fm, "Color Picker");
            }
        });
    }


    public void colorBarVisibility(int mode){
        color.setVisibility(mode);
    }
}
