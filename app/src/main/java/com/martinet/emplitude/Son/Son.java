package com.martinet.emplitude.Son;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Emploi.ADE_information;
import com.martinet.emplitude.Emploi.Cours;
import com.martinet.emplitude.R;


/**
 * Created by martinet on 13/11/15.
 */
public class Son extends Fragment {

    private Switch activer;
    private SeekBar sonnerie;
    private SeekBar notification;
    private SeekBar media;
    private CheckBox vibrer;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private FrameLayout color;
    private Vibrator vibrator;
    private AudioManager amanager;
    private int sonMaxSonnerie;
    private int sonMaxMedia;
    private int sonMaxNotification;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.getActivity().setTitle("Gestion des sons");

        View view =inflater.inflate(R.layout.sons, container, false);

        this.activer = (Switch) view.findViewById(R.id.activer);
        this.sonnerie = (SeekBar) view.findViewById(R.id.sonnerie);
        this.notification = (SeekBar) view.findViewById(R.id.notification);
        this.media = (SeekBar) view.findViewById(R.id.media);
        this.vibrer = (CheckBox) view.findViewById(R.id.vibreur);
        this.color = (FrameLayout) view.findViewById(R.id.color);

        this.sharedpreferences = getActivity().getSharedPreferences(Constants.PREFERENCE_SON, Context.MODE_PRIVATE);
        this.editor = sharedpreferences.edit();
        this.vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        this.amanager = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);

        this.sonMaxSonnerie = this.amanager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        this.sonMaxNotification = this.amanager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        this.sonMaxMedia = this.amanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        this.initialisation();
        return view;
    }

    public void toggleActivation(boolean activer){
        if(activer){
            this.activer.setText(R.string.active);
            this.color.setVisibility(View.GONE);
            this.lancerReceiver();

        }else {
            this.activer.setText(R.string.deactive);
            this.color.setVisibility(View.VISIBLE);
            this.annulerReceiver();

        }
        this.sonnerie.setEnabled(activer);
        this.media.setEnabled(activer);
        this.notification.setEnabled(activer);
        this.vibrer.setEnabled(activer);
    }

    public void initialisation(){

        if(!sharedpreferences.getBoolean("activer", true)){
            this.toggleActivation(false);
        }

        activer.setChecked(sharedpreferences.getBoolean("activer", true));
        sonnerie.setProgress(sharedpreferences.getInt("sonnerie", 0));
        notification.setProgress(sharedpreferences.getInt("notification", 0));
        media.setProgress(sharedpreferences.getInt("media", 0));
        vibrer.setChecked(sharedpreferences.getBoolean("vibrer", true));

        sonnerie.setMax(sonMaxSonnerie);
        notification.setMax(sonMaxNotification);
        media.setMax(sonMaxMedia);

        activer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("activer", isChecked);
                toggleActivation(isChecked);
            }
        });
        sonnerie.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("sonnerie", progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        notification.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("notification", progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        media.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("media", progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        vibrer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("vibrer", isChecked);
                if (isChecked) {
                    vibrator.vibrate(500);
                }
            }
        });

        if(!sharedpreferences.getBoolean("lancer", false)){
            this.lancerReceiver();
        }
    }

    public void lancerReceiver(){
        ADE_information ade_information = new ADE_information(getContext());
        Cours prochainCours = ade_information.getNext();
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), LancerSonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, prochainCours.getDateD().getTime() - 60, pendingIntent);

        Intent intent2 = new Intent(getContext(), FermerSonReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getContext(), 1, intent2, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, prochainCours.getDateF().getTime() - 60, pendingIntent2);
    }

    public void annulerReceiver(){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), LancerSonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
        alarmManager.cancel(pendingIntent);

        Intent intent2 = new Intent(getContext(), FermerSonReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getContext(), 1, intent2, 0);
        alarmManager.cancel(pendingIntent2);
    }

    public void onStop(){
        editor.commit();
        super.onStop();
    }
}
