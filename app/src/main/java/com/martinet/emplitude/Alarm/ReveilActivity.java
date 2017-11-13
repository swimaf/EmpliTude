package com.martinet.emplitude.Alarm;
/**
 * Created by Arnaud on 04/01/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Dialog.DialogRepeat;
import com.martinet.emplitude.R;


public class ReveilActivity extends Fragment {

    public static final String KEY_SONNERIE = "SONNERIE";
    public static final String KEY_VIBRATE = "VIBRATE";
    public static final String KEY_FONDU = "FONDU";
    public static final String KEY_PREPARATION_TIME = "KEY_PREPARATION_TIME";
    public static final String KEY_ACTIVATION = "ACTIVATION";
    public static final String KEY_REPEAT = "KEY_REPEAT";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Switch stVibrate;
    private Switch stFondu;
    private Switch stActivation;
    private NumberPicker npDureePrepa;
    private LinearLayout btnRepeat;
    private LinearLayout btnSound;
    private LinearLayout llVibrate;
    private LinearLayout llFondu;
    private View view;
    private LinearLayout llActivate;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = inflater.inflate(R.layout.alarm, container, false);
        this.getActivity().setTitle("Réveil");

        initialiser();
        appelListeners();
        initPreparationTime();
        return this.view;

    }

    private void initialiser() {
        preferences = getActivity().getSharedPreferences(Constants.Preference.ALARM, Context.MODE_PRIVATE);
        editor = preferences.edit();

        stVibrate    = (Switch) view.findViewById(R.id.srVibreur);
        stFondu      = (Switch) view.findViewById(R.id.srFondu);
        stActivation = (Switch) view.findViewById(R.id.srActivation);
        npDureePrepa = (NumberPicker) view.findViewById(R.id.nprTempsPreparation);
        btnRepeat    = (LinearLayout) view.findViewById(R.id.llRepeat);
        btnSound     = (LinearLayout) view.findViewById(R.id.llSound);
        llFondu      = (LinearLayout) view.findViewById(R.id.llFondu);
        llVibrate    = (LinearLayout) view.findViewById(R.id.llVibrate);
        llActivate   = (LinearLayout) view.findViewById(R.id.activate);

        stVibrate.setChecked(preferences.getBoolean(KEY_VIBRATE, false));
        stFondu.setChecked(preferences.getBoolean(KEY_FONDU, false));
        stActivation.setChecked(preferences.getBoolean(KEY_ACTIVATION, true));
        toogleFonctionality();

    }


    private void appelListeners() {
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherDialogRepeter();
            }
        });
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent myIntent = new Intent(getActivity(), AudioPlayerDemoActivity.class);
                startActivity(myIntent);*/
            }
        });
        llVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(KEY_VIBRATE, !stVibrate.isChecked());
                stVibrate.setChecked(!stVibrate.isChecked());
            }
        });
        llFondu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(KEY_FONDU, !stFondu.isChecked());
                stFondu.setChecked(!stFondu.isChecked());
            }
        });
        stActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(KEY_ACTIVATION, stActivation.isChecked());
                toogleFonctionality();
            }
        });
    }

    public void toogleFonctionality() {
        if(!stActivation.isChecked()) {
            ((ViewGroup) llActivate.getParent()).setBackgroundColor(getResources().getColor(R.color.gray_transparente));
            stActivation.setText("Désactivé");
            for (int i = 0; i < llActivate.getChildCount(); i++) {
                llActivate.getChildAt(i).setEnabled(false);
                llActivate.getChildAt(i).setClickable(false);
            }

        } else {
            stActivation.setText("Activé");
            ((ViewGroup) llActivate.getParent()).setBackgroundResource(0);
            for (int i = 0; i < llActivate.getChildCount(); i++) {
                llActivate.getChildAt(i).setEnabled(true);
                llActivate.getChildAt(i).setClickable(true);
            }
        }
    }

    public void initPreparationTime() {
        npDureePrepa.setMaxValue(180);
        npDureePrepa.setMinValue(1);

        if (!preferences.contains(KEY_PREPARATION_TIME)) {
            editor.putInt(KEY_PREPARATION_TIME,45);
            editor.commit();
        }
        npDureePrepa.setValue(preferences.getInt(KEY_PREPARATION_TIME, 45));
        npDureePrepa.setWrapSelectorWheel(true);
    }


    public void afficherDialogRepeter() {
        DialogRepeat dialog = DialogRepeat.getInstance(preferences.getInt(KEY_REPEAT, 0));
        dialog.show(getActivity().getSupportFragmentManager(), "Dialog répéter");
    }

    public void editRepeat(int value) {
        editor.putInt(KEY_REPEAT, value);
        editor.apply();
    }

    public void onStop() {
        super.onStop();
        editor.putInt(KEY_PREPARATION_TIME, npDureePrepa.getValue());
        editor.commit();
        ProgrammAlarm.setAlarm(getActivity().getApplicationContext());
    }


}
