package com.example.martinet.Emplitude.Son;

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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.R;
import com.example.martinet.Emplitude.Reveil.ReveilActivity;


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

        this.initialisation();
        return view;
    }

    public void toggleActivation(boolean activer){
        if(activer){
            this.activer.setText("Activé");
            this.color.setVisibility(View.GONE);
        }else {
            this.activer.setText("Désactivé");
            this.color.setVisibility(View.VISIBLE);
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

        activer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("activer", isChecked);
                toggleActivation(isChecked);
            }
        });
        sonnerie.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("sonnerie", progress);
                AudioManager amanager=(AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
                amanager.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_VIBRATE);
                amanager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, AudioManager.FLAG_VIBRATE);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        notification.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("notification", progress);
                AudioManager amanager=(AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
                amanager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_VIBRATE);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        media.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("media", progress);
                AudioManager amanager=(AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
                amanager.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_VIBRATE);
                amanager.setStreamVolume(AudioManager.STREAM_ALARM, 0, AudioManager.FLAG_VIBRATE);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        vibrer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("vibrer", isChecked);
                if(isChecked){
                    vibrator.vibrate(500);
                }
            }
        });
    }

    public void onStop(){
        editor.commit();
        super.onStop();
    }
}
