package com.example.martinet.Emplitude.Reveil;

/**
 * Created by Arnaud on 04/01/2016.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReveilActivity extends Fragment implements NumberPicker.OnValueChangeListener {
    //---------------------------------------------------------------
    //------------------Declaration Variables------------------------
    //---------------------------------------------------------------
    public static final String MesPREFERENCES = "mesPreferences";
    public static final String keyDSonner = "keyDSonner";
    public static final String keyDFondu = "keyDFondu";
    public static final String keyDTorche = "keyDTorche";
    public static final String keyNpDureePrepa = "keyNpDureePrepa";
    public static final String keyDActivDesactiv = "keyDActivDesactiv";
    public static final String keyNbRepetition = "keyNbRepetition";
    public static final String keyNbRepetitionRestante = "keyNbRepetitionRestante";
    public static final String keyMinRepetition = "keyMinRepetition";
    public static final String keyMinTempo = "keyMinTempo";
    public static int minRepetition = 0;
    public static SharedPreferences sharedpreferences;
    public static AlarmManager alarmManager;
    public static Button btnRepeter;
    public static Button btnTempo;
    public static boolean dSonner = false;
    public static boolean dFondu = false;
    public static boolean dTorche = false;
    public static boolean dActivDesaciv = true;
    public static int nbRepetionRestante = 0;
    private static int timeHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private static int timeMinute = Calendar.getInstance().get(Calendar.MINUTE);
    private static PendingIntent pendingIntent;
    private static Context mContext;
    private FragmentActivity myContext;
    private TextView textView1;
    private TextView tvrTempsPreparation;
    private TextView tvrMinutes;
    private TextView tvrRepeter;
    private TextView tvrSon;
    private TextView tvrTempo;
    private Switch switchVibreur;
    private Switch switchTorche;
    private Switch switchFondu;
    private Switch switchActivDesactiv;
    private NumberPicker npDureePrepa;
    private NumberPicker nprMinTempo;
    private NumberPicker nprFoisRepeter;
    private NumberPicker nprMinRepeter;
    private Button btn1;
    private Button btnSon;
    private View view;
    private int tmpa;
    private ProgrammerAlarm proAlarm;
    private ImageButton help;






    //---------------------------------------------------------------------
    //------------------Methode principale OnCreate------------------------
    //---------------------------------------------------------------------
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = inflater.inflate(R.layout.reveil_activity, container, false);

        //setContentView(R.layout.reveil_activity);
        mContext = container.getContext();
        // pour affecter this a la variable utilisé par la suite.
        initialiser();
        appelListeners();
        appelSwitch();
        initNPTempsPrepa();
        proAlarm.setAlarmAuto();

        return this.view;
    }


    //------------------Methode pour appeler les listeners------------------------
    private void appelListeners() {
        //Listener pour le bouton Repeter
        OnClickListener listenerRepeter = new OnClickListener() {

            @Override
            public void onClick(View v) {
                afficherDialogRepeter();
                Log.i("btnRepeter", "Appui bouton repeter");

            }
        };
        //Listener pour le bouton Temporisation
        OnClickListener listenerTemporisation = new OnClickListener() {

            @Override
            public void onClick(View v) {
                afficherDialogTempo();
                Log.i("btnTempo", "Appui bouton tempo");
            }
        };
        //Listener pour le bouton Son
        OnClickListener listenerSon = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("btnSOn", "Appui bouton son");

            }
        };
        //Listener1 -> bonton lancer l'alarme
        OnClickListener listener1 = new OnClickListener() {
            public void onClick(View view) {
                // textView2.setText("");
                Bundle bundle = new Bundle();
                bundle.putInt(MyConstants.HOUR, timeHour);
                bundle.putInt(MyConstants.MINUTE, timeMinute);
                MyDialogFragment fragment = new MyDialogFragment(new MyHandler());
                fragment.setArguments(bundle);
                FragmentManager manager = myContext.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(fragment, MyConstants.TIME_PICKER);
                transaction.commit();

            }
        };

        OnClickListener listenerhelp = new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("Aide");
                TextView editText = new TextView(mContext);
                editText.setText("Selectionner la durée dont vous avez besoin entre votre réveil et votre arrivé en emploi_cour.\n Le temps de préparation sélectionner determinera l'heure de  sonnerie reveil");
                LinearLayout linearLayout= new LinearLayout(mContext);
                linearLayout.setPadding(20, 10, 20, 10);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(editText);
                dialog.setView(linearLayout);
                dialog.show();


            }
        };

        //Set On Click Listener
        btn1.setOnClickListener(listener1);
        //btn2.setOnClickListener(listener2);
        btnTempo.setOnClickListener(listenerTemporisation);
        btnSon.setOnClickListener(listenerSon);
        help.setOnClickListener(listenerhelp);


    }


    //------------------Methode pour initialiser les variables-----------------------

    private void initialiser() {

        sharedpreferences = getActivity().getSharedPreferences(MesPREFERENCES, Context.MODE_PRIVATE);

        //TextViews
        textView1 = (TextView) view.findViewById(R.id.msg1);
        textView1.setText(timeHour + ":" + timeMinute);


        //AlarmManager
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        //Itent
        Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);

        //Switch
        switchVibreur = (Switch) view.findViewById(R.id.srVibreur);
        switchTorche = (Switch) view.findViewById(R.id.srTorche);
        switchFondu = (Switch) view.findViewById(R.id.srFondu);
        switchActivDesactiv = (Switch) view.findViewById(R.id.srActiv);


        //NumberPicker
        npDureePrepa = (NumberPicker) view.findViewById(R.id.nprTempsPreparation);

        //Button
        btn1 = (Button) view.findViewById(R.id.button1);//Bouton programmer pour test
        //btn2 = (Button) findViewById(R.id.button2);//Bouton arreter pour test

        help = (ImageButton) view.findViewById(R.id.ibrhelp);
        btnTempo = (Button) view.findViewById(R.id.brTempoDroite);

        if(sharedpreferences.contains(keyMinTempo)) {
            btnTempo.setText(String.valueOf(sharedpreferences.getInt(keyMinTempo, 0)) + " min");
        }else{
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(keyMinTempo, getMinTempo());
            editor.commit();
        }


        btnSon = (Button) view.findViewById(R.id.brSonDroite);

        tvrMinutes = (TextView) view.findViewById(R.id.tvrMinutes);
        tvrSon = (TextView) view.findViewById(R.id.tvrSon);
        tvrTempsPreparation = (TextView) view.findViewById(R.id.tvrTempsPreparation);
        tvrTempo = (TextView) view.findViewById(R.id.tvrTempo);

       tmpa = sharedpreferences.getInt(keyNbRepetitionRestante, 0);

        proAlarm = new ProgrammerAlarm(getContext(),alarmManager,pendingIntent);

    }

    //----------------------------------------------------------------------------
    //Pour l'initialisation du timepicker de temps de preparation-----------------
    //----------------------------------------------------------------------------
    public void initNPTempsPrepa() {
        npDureePrepa.setMaxValue(180);
        npDureePrepa.setMinValue(1);
        if (sharedpreferences.getInt(keyNpDureePrepa,0)>-1) {
            npDureePrepa.setValue(sharedpreferences.getInt(keyNpDureePrepa, 0));
        } else{
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(keyNpDureePrepa,45);
            editor.commit();
        }
        npDureePrepa.setWrapSelectorWheel(true);//Permet de choisir la valeur au dessus de min soit la valeur max.
        npDureePrepa.setOnValueChangedListener(this);
    }


    //Permet de recuperer les valeurs donné dans le timepicker dans des variables, les afficher, puis appeler le setalarm
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            timeHour = bundle.getInt(MyConstants.HOUR);
            timeMinute = bundle.getInt(MyConstants.MINUTE);
            textView1.setText(timeHour + ":" + timeMinute);
            setAlarm();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }


    //----------------------------------------------------------------------------
    //Pour ouvrir quand sa sonne une nouvelle fenetre avec arreter/reporter/message
    //----------------------------------------------------------------------------
    public void ouvrirSonnerieActivity() {
        Intent it = new Intent(getActivity(), SonnerieActivity.class);
        startActivity(it);
    }

    //----------------------------------------------------------------------------
    //------------Methode generer automatiquement---------------------------------
    //----------------------------------------------------------------------------
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        if(picker==npDureePrepa){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(keyNpDureePrepa,newVal);
            editor.commit();
        }else if(picker==nprMinTempo){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(keyMinTempo,newVal);
            editor.commit();
        }else if(picker==nprMinRepeter){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(keyMinRepetition,newVal);
            editor.commit();
        }else if(picker==nprFoisRepeter){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(keyNbRepetition,newVal);
            editor.commit();
    }
    }


    //==================================================================
    //==================================================================
    //==========================SWITCH==================================
    //==================================================================
    //==================================================================


    //---------------------------------------------------------------
    //-------Methode pour savoir si les switch sont activé ou non----
    //---------------------------------------------------------------


    public void appelSwitch() {
        switchActivDesactiv.setChecked(sharedpreferences.getBoolean(keyDActivDesactiv,false));
        switchActivDesactiv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//Si le switch est activé alors
                    Log.i("test", "Reveil actif");
                    switchActivDesactiv.setText("Activé");
                    dActivDesaciv=true;
                    switchFondu.setEnabled(true);
                    switchFondu.setFocusable(true);
                    switchTorche.setEnabled(true);
                    switchTorche.setFocusable(true);
                    switchVibreur.setEnabled(true);
                    switchVibreur.setFocusable(true);
                    btnTempo.setEnabled(true);
                    btnTempo.setFocusable(true);
                    btnSon.setEnabled(true);
                    btnSon.setFocusable(true);
                    npDureePrepa.setEnabled(true);
                    npDureePrepa.setFocusable(true);
                    btnSon.setTextColor(getResources().getColor(R.color.black));
                    btnTempo.setTextColor(getResources().getColor(R.color.black));
                    tvrTempo.setTextColor(getResources().getColor(R.color.black));
                    tvrSon.setTextColor(getResources().getColor(R.color.black));
                    tvrMinutes.setTextColor(getResources().getColor(R.color.black));
                    tvrTempsPreparation.setTextColor(getResources().getColor(R.color.black));
                    proAlarm = new ProgrammerAlarm(getContext(),alarmManager,pendingIntent);
                    proAlarm.setAlarmAuto();


                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(keyDActivDesactiv, dActivDesaciv);
                    editor.commit();
                } else {
                    Log.i("test", "Reveil innactif");
                    switchActivDesactiv.setText("Désactivé");
                    dActivDesaciv = false;
                    switchFondu.setEnabled(false);
                    switchFondu.setFocusable(false);
                    switchTorche.setEnabled(false);
                    switchTorche.setFocusable(false);
                    switchVibreur.setEnabled(false);
                    switchVibreur.setFocusable(false);
                    btnTempo.setEnabled(false);
                    btnTempo.setFocusable(false);
                    btnSon.setEnabled(false);
                    btnSon.setFocusable(false);
                    npDureePrepa.setEnabled(false);
                    npDureePrepa.setFocusable(false);
                    btnSon.setTextColor(getResources().getColor(R.color.grisDesactiv));
                    btnTempo.setTextColor(getResources().getColor(R.color.grisDesactiv));
                    tvrSon.setTextColor(getResources().getColor(R.color.grisDesactiv));
                    tvrMinutes.setTextColor(getResources().getColor(R.color.grisDesactiv));
                    tvrTempsPreparation.setTextColor(getResources().getColor(R.color.grisDesactiv));
                    tvrTempo.setTextColor(getResources().getColor(R.color.grisDesactiv));
                    cancelAlarm();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(keyDActivDesactiv, dActivDesaciv);
                    editor.commit();

                }
            }
        });
        switchVibreur.setChecked(sharedpreferences.getBoolean(keyDSonner,false));
        switchVibreur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//Si le switch est activé alors
                    Log.i("vibswitchon", "Vibreur actif");
                    dSonner = true;
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(keyDSonner, dSonner);
                    editor.commit();
                } else {
                    Log.i("vibswitchoff", "Vibreur innactif");
                    dSonner = false;
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(keyDSonner, dSonner);
                    editor.commit();

                }
            }
        });
        switchFondu.setChecked(sharedpreferences.getBoolean(keyDFondu, false));
        switchFondu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("fonduswitchon", "Demarrage fondu actif");
                    dFondu = true;
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(keyDFondu, dFondu);
                    editor.commit();
                } else {
                    Log.i("fonduswitchoff", "Demarrage fondu innactif");
                    dFondu = false;
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(keyDFondu, dFondu);
                    editor.commit();
                }
            }
        });

        switchTorche.setChecked(sharedpreferences.getBoolean(keyDTorche,false));
        switchTorche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("torchswitchon", "Lampe torche actif");
                    dTorche = true;
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(keyDTorche, dTorche);
                    editor.commit();
                } else {
                    Log.i("torchswitchoff", "Lampe torche innactif");
                    dTorche = false;
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(keyDTorche, dTorche);
                    editor.commit();
                }
            }
        });
    }


    //==================================================================
    //==================================================================
    //==========================SETTER D'ALARM==========================
    //==================================================================
    //==================================================================


    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        //Met dans un object calendar l'heure ou il doit sonner puis la minute
        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);
        //Set l'alarme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//Choix en fonction de la version d'android
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    //Fonction pour re set automatiquement l'alarme apres un tempo
    public static void setAlarmTempo() {
        int tps = getMinTempo();// Je recupere la valeur de tempo ( que j'ajouterais apres )
        SimpleDateFormat heure = new SimpleDateFormat("HH");
        SimpleDateFormat minute = new SimpleDateFormat("mm");

        Calendar c = Calendar.getInstance();
        // Je recup heure minute actuel
        String ha = heure.format(c.getTime());
        String ma = minute.format(c.getTime());
        // Conversion en int
        int entierHa = Integer.parseInt(ha);
        int entierMa = Integer.parseInt(ma);
        Log.i("heureAcctuel", ha);
        Log.i("minAcctuel", ma);

        if (entierMa + tps >= 60) {//Ex : si il est 6h58 que je tempo de 5mn, je peux pas dire je sonne a la 63ème minute
            entierMa = (entierMa + tps) - 60;// j'adiitionne j'obtiens 63, j'enleve 60, j'ai 3
            entierHa++;// je rajoute 1h car j'ai enlever 60mn
        } else {//sinon j'ai juste a ajouter les minute |ex : j'ajoute 5 mn si il esty 6h20
            entierMa = entierMa + tps;//20+5 = 25mn et les heures on pas changé
        }
        //variable tempo pour test console ( supprimable car fonctionne )
        String hap = String.valueOf(entierHa);
        String map = String.valueOf(entierMa);
        //affichage console
        Log.i("heureDeReport", hap);
        Log.i("minDeReport", map);
        Calendar calendar = Calendar.getInstance();
        //Met dans un object calendar l'heure ou il doit sonner puis la minute
        calendar.set(Calendar.HOUR_OF_DAY, entierHa);
        calendar.set(Calendar.MINUTE, entierMa);
        //Set l'alarme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//Choix en fonction de la version d'android
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        }
    }

    public void setAlarmRepeter() {

        int tps = Constants.intervaleSonnerieRepeter + minRepetition;//Intervale entre les deux sonneries -> Faudrait faire un constante
        SimpleDateFormat heure = new SimpleDateFormat("HH");
        SimpleDateFormat minute = new SimpleDateFormat("mm");

        if (tmpa!= 0) {
            Calendar c = Calendar.getInstance();
            // Je recup heure minute actuel
            String ha = heure.format(c.getTime());
            String ma = minute.format(c.getTime());
            // Conversion en int
            int entierHa = Integer.parseInt(ha);
            int entierMa = Integer.parseInt(ma);
            Log.i("heureAcctuel", ha);

            Log.i("minAcctuel", ma);

            if (entierMa + tps >= 60) {//Ex : si il est 6h58 que je tempo de 5mn, je peux pas dire je sonne a la 63ème minute
                entierMa = (entierMa + tps) - 60;// j'adiitionne j'obtiens 63, j'enleve 60, j'ai 3
                entierHa++;// je rajoute 1h car j'ai enlever 60mn
            } else {//sinon j'ai juste a ajouter les minute |ex : j'ajoute 5 mn si il esty 6h20
                entierMa = entierMa + tps;//20+5 = 25mn et les heures on pas changé
            }
            //variable tempo pour test console ( supprimable car fonctionne )
            String hap = String.valueOf(entierHa);
            String map = String.valueOf(entierMa);
            //affichage console
            Log.i("heureDeReport", hap);
            Log.i("minDeReport", map);
            Calendar calendar = Calendar.getInstance();
            //Met dans un object calendar l'heure ou il doit sonner puis la minute
            calendar.set(Calendar.HOUR_OF_DAY, entierHa);
            calendar.set(Calendar.MINUTE, entierMa);
            //Set l'alarme
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//Choix en fonction de la version d'android
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            }

            SharedPreferences.Editor editor = sharedpreferences.edit();
            int tmp = sharedpreferences.getInt(keyNbRepetitionRestante,0);
            tmp--;
            editor.putInt(keyNbRepetitionRestante,tmp);
            editor.commit();
        }
    }

    public void cancelAlarm() {
        //nbRepetionRestante = 0;
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
    public void cancelAlarmEtSetReveil() {
        proAlarm = new ProgrammerAlarm(this.getContext(),alarmManager,pendingIntent);
        proAlarm.setAlarmAuto();
       //nbRepetionRestante = 0;
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }


    //==================================================================
    //==================================================================
    //==========================BOITE DE DIALOGUE=======================
    //==================================================================
    //==================================================================


    //--------------------------------------------------------------------------------------
    //----------------Methode pour ouvrir la boite de dialog de choix de tempo--------------
    //--------------------------------------------------------------------------------------
    public void afficherDialogTempo() {

        final Dialog dialogTempo = new Dialog(getActivity());
        dialogTempo.setTitle("Choix durée temporisation");
        dialogTempo.setContentView(R.layout.dialog_temporisation);
        Button brValiderTempo = (Button) dialogTempo.findViewById(R.id.brValiderTempo);
        Button brAnnulerTempo = (Button) dialogTempo.findViewById(R.id.brAnnulerTempo);
        nprMinTempo = (NumberPicker) dialogTempo.findViewById(R.id.nprTempo);
        nprMinTempo.setMaxValue(30);
        nprMinTempo.setMinValue(1);
        nprMinTempo.setValue(getMinTempo());//Pour changer valeur aller dans strings.xml
        nprMinTempo.setWrapSelectorWheel(true);
        nprMinTempo.setOnValueChangedListener(this);
        brValiderTempo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(keyMinTempo, nprMinTempo.getValue());
                editor.commit();
                btnTempo.setText(String.valueOf(sharedpreferences.getInt(keyMinTempo, 0)) + " min");

                dialogTempo.dismiss();
            }
        });
        brAnnulerTempo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTempo.dismiss();
            }
        });
        dialogTempo.show();


    }

    //--------------------------------------------------------------------------------------
    //----------------Methode pour ouvrir la boite de dialog de repetion -------------------
    //--------------------------------------------------------------------------------------
    public void afficherDialogRepeter() {

        final Dialog dialogRepeter = new Dialog(getActivity());
        dialogRepeter.setTitle("Choix répétitions");
        dialogRepeter.setContentView(R.layout.dialog_repeter);
        Button brValiderRepeter = (Button) dialogRepeter.findViewById(R.id.brValiderRepeter);
        Button brAnnulerRepeter = (Button) dialogRepeter.findViewById(R.id.brAnnulerRepeter);
        nprFoisRepeter = (NumberPicker) dialogRepeter.findViewById(R.id.nprFoisRepeter);
        nprMinRepeter = (NumberPicker) dialogRepeter.findViewById(R.id.nprMinRepeter);

        nprFoisRepeter.setMaxValue(10);
        nprMinRepeter.setMaxValue(30);
        nprMinRepeter.setMinValue(1);
        nprFoisRepeter.setWrapSelectorWheel(true);//Permet de faire la roulette vers le haut ( au dessus de min pour arriver sur max )
        nprFoisRepeter.setOnValueChangedListener(this);
        nprMinRepeter.setWrapSelectorWheel(true);
        nprMinRepeter.setOnValueChangedListener(this);
        brValiderRepeter.setOnClickListener(new OnClickListener() {
            //Action sur clique "ok" pour valider
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(keyNbRepetition, nprFoisRepeter.getValue());
                editor.putInt(keyMinRepetition, nprMinRepeter.getValue());
                editor.commit();
                btnRepeter.setText(String.valueOf(sharedpreferences.getInt(keyNbRepetition, 0)) + " fois " + String.valueOf(sharedpreferences.getInt(keyMinRepetition, 0)) + " min");

                dialogRepeter.dismiss();
            }
        });
        brAnnulerRepeter.setOnClickListener(new OnClickListener() {
            //Action sur le clique "Annuler"
            @Override
            public void onClick(View v) {
                dialogRepeter.dismiss();
            }
        });

        dialogRepeter.show();//Pour afficher la boite de dialogue


    }


    //==================================================================
    //==================================================================
    //==========================GETTERS=================================
    //==================================================================
    //==================================================================


    public static int getMinTempo() {
        return  5;
    }

    //--------------------------------------------------------------------
    //-------Methodes pour qui renvoie si les switch sont activé ou non---
    //--------------------------------------------------------------------
    public static boolean getDSonner() {
        //return dSonner;//vibreur
        return sharedpreferences.getBoolean(keyDSonner,true);
    }

    public static boolean getDFondu() {
        //return dFondu;
        return sharedpreferences.getBoolean(keyDFondu,true);
    }

    public static boolean getDTorche() {
        return  sharedpreferences.getBoolean(keyDFondu,true);
        //return dTorche;
    }
}