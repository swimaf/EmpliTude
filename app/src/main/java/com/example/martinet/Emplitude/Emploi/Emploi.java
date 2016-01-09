package com.example.martinet.Emplitude.Emploi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinet.Emplitude.R;
import java.util.Date;


public class Emploi extends Fragment{

    public static Jour calendrier = new Jour(new Date());
    public static Boolean premier = true;
    private View viewAction;
    private FragmentActivity a;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.getActivity().setTitle("Emploi du temps");
        this.a = this.getActivity();
        premier = true;
        changeToolbar();
        this.a = getActivity();
        return inflater.inflate(R.layout.emploi_du_temps, container, false);
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
        pre.setOnClickListener(new Button.OnClickListener(){
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
            TextView jour = (TextView) viewAction.findViewById(R.id.jour);
            jour.setText(calendrier.getJour());
            if (i != 0 || premier) {
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
                FragmentTransaction ft = a.getSupportFragmentManager().beginTransaction();
                if (!premier) {
                    if (i < 0) {
                        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } else {
                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
                premier = false;
                ft.replace(R.id.frag, fragment);
                ft.commit();
            }
        }else{
            calendrier.ajouterJour(-i);
            Toast.makeText(a.getApplicationContext(), "Vous ne pouvez pas aller par ici !", Toast.LENGTH_SHORT).show();
        }
    }


    public void onDestroyView() {
        super.onDestroyView();
    }

}
