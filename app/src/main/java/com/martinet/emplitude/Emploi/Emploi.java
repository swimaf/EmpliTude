package com.martinet.emplitude.Emploi;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Outil.Jour;
import com.martinet.emplitude.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * Classe permettant de gérer l'affichage de l'emploi du temps
 */

public class Emploi extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ADE_retour {

    private static final int NUM_PAGES = 15; //Nombre de jour visible

    private View viewAction;
    private FragmentActivity activity;
    private SwipeRefreshLayout swipe;
    private Toast toast;
    private RelativeLayout color;
    private Button button_picker;
    private ViewPager mPager;
    public HashMap<Integer, Fragment> fragmentReference;

    private PagerAdapter mPagerAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.getActivity().setTitle("Emploi du temps");
        this.activity = getActivity();
        View view = inflater.inflate(R.layout.emploi_du_temps, container, false);

        this.color = (RelativeLayout) view.findViewById(R.id.color);
        this.button_picker = (Button) color.findViewById(R.id.color_5);
        this.toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        //Ajout du listener le mise a jour auto
        this.swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(getResources().getIntArray(R.array.spinner));
        this.swipe.setOnRefreshListener(this);

        //Changement de la toolbar avec l'action precedent et suivant
        this.changeToolbar();
        this.loadCouleurs();    //Chargement du module de couleur
        Jour aujour = new Jour(new Date());
        this.setDateTitle(aujour.getJour());

        //Ajout du listener lors slider vers la droite ou la gauche
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.getLayoutParams().height= Constants.getHeight(getContext());
        mPagerAdapter = new ScreenSlidePagerAdapter(activity.getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.getLayoutParams().height = Constants.getHeight(getContext())+100;
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int page) {
                Jour calendar = new Jour(new Date());
                calendar.ajouterJour(page);
                setDateTitle(calendar.getJour());
                colorBarVisibility(ViewGroup.GONE);
            }

            public void onPageScrollStateChanged(int state) {
                colorBarVisibility(ViewGroup.GONE);
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                colorBarVisibility(ViewGroup.GONE);
            }
        });
        return view;
    }


    public void changeToolbar(){
        ViewGroup tool = (ViewGroup) getActivity().findViewById(R.id.tool);
        LayoutInflater vi = LayoutInflater.from(getContext());
        View v = vi.inflate(R.layout.emploi_action, null);
        this.viewAction = v;
        tool.addView(v);
        ImageButton pre = (ImageButton) v.findViewById(R.id.precedent);
        ImageButton next = (ImageButton) v.findViewById(R.id.suivant);
        pre.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = mPager.getCurrentItem()-1;
                if(i < 0){
                    setToast("Vous ne pouvez pas aller dans le passé !");
                }else{
                    mPager.setCurrentItem(i, true);
                }

            }
        });
        next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = mPager.getCurrentItem() + 1;
                if (i > 14) {
                    setToast("Vous ne pouvez pas aller plus loin que 15 jours !");
                } else {
                    mPager.setCurrentItem(i, true);
                }
            }
        });
    }

    public void refresh(){
        int index = mPager.getCurrentItem();
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(index);
    }

    //Lorsque l'utilisateur change de page on supprime la toolbar personnalisé
    public void onDestroyView(){
        super.onDestroyView();
        ViewGroup tool = (ViewGroup) getActivity().findViewById(R.id.tool);
        tool.removeView(viewAction);
    }

    //Lorsque l'utilisateur glisse son doigt vers le vas
    public void onRefresh() {
        if (Constants.CONNECTED(getContext())) {
            //Si connecté à internet : mise à jour de l'emploi du temps
            ADE_recuperation load = new ADE_recuperation(this, getContext());
            load.execute();
            this.refresh();
        } else {
            //Sinon affichage d'undn message d'erreur
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

    //Retourne le jour courant
    public JourEmploi getFragment(){
        return ((JourEmploi)fragmentReference.get(mPager.getCurrentItem()));
    }

    //Chargement de la barre des couleurs
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
                    getFragment().setColorButton(colorBar[k]);
                }
            });
        }

        //Lorque l'utilisateur selectionne le bouton personnalisé : ouverture du color picker
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

    //Change la visibilité de la barre des couleurs
    public void colorBarVisibility(int mode){
        color.setVisibility(mode);
    }


    //Change la toolbar avec le nom du jour
    public void setDateTitle(String date){
        TextView jour = (TextView) viewAction.findViewById(R.id.jour);
        jour.setText(date);
    }

    /**
     * Classe interne pour la gestion de tout les jours avec le slider
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentReference = new HashMap<Integer, Fragment>();
        }

        @Override
        public Fragment getItem(int position) {
            Jour calendar = new Jour(new Date());
            calendar.ajouterJour(position);
            JourEmploi j = new JourEmploi();
            j.setJour(calendar.getDate());
            fragmentReference.put(position, j); //Sauvegarde des jours
            return j;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
