package com.martinet.emplitude.Sound;

import android.content.Context;
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
import com.martinet.emplitude.R;

public class Son extends Fragment {

    private Switch activer;
    private SeekBar sonnerie;
    private SeekBar notification;
    private SeekBar media;
    private CheckBox vibrer;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private FrameLayout color;
    private Vibrator vibrator;
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

        this.preferences = getActivity().getSharedPreferences(Constants.Preference.SOUND, Context.MODE_PRIVATE);
        this.editor = preferences.edit();
        this.vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        this.sonMaxSonnerie = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        this.sonMaxNotification = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        this.sonMaxMedia = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        this.initialisation();
        return view;
    }

    public void toggleActivation(boolean activer){
        if(activer){
            this.activer.setText(R.string.active);
            this.color.setVisibility(View.GONE);
            ProgrammSound.loadReceiver(getContext());

        }else {
            this.activer.setText(R.string.deactive);
            this.color.setVisibility(View.VISIBLE);
            ProgrammSound.cancelReceiver(getContext());
        }
        this.sonnerie.setEnabled(activer);
        this.media.setEnabled(activer);
        this.notification.setEnabled(activer);
        this.vibrer.setEnabled(activer);
    }

    public void initialisation(){

        if(!preferences.getBoolean("activer", true)){
            this.toggleActivation(false);
        }

        activer.setChecked(preferences.getBoolean("activer", true));
        sonnerie.setProgress(preferences.getInt("sonnerie", 0));
        notification.setProgress(preferences.getInt("notification", 0));
        media.setProgress(preferences.getInt("media", 0));
        vibrer.setChecked(preferences.getBoolean("vibrer", true));

        sonnerie.setMax(sonMaxSonnerie);
        notification.setMax(sonMaxNotification);
        media.setMax(sonMaxMedia);

        activer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleActivation(isChecked);
            }
        });
        vibrer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vibrator.vibrate(500);
                }
            }
        });

        if(!preferences.getBoolean("lancer", false)){
            ProgrammSound.loadReceiver(getContext());
        }
    }
    public void onStop(){
        editor.putBoolean("activer", activer.isChecked());
        editor.putBoolean("vibrer", vibrer.isChecked());
        editor.putInt("sonnerie", sonnerie.getProgress());
        editor.putInt("notification", notification.getProgress());
        editor.putInt("media", media.getProgress());

        editor.commit();
        ProgrammSound.cancelReceiver(getContext());
        if(preferences.getBoolean("activer", true)) {
            ProgrammSound.setReceiverOnLessonBegin(getContext());
        }
        super.onStop();
    }
}
