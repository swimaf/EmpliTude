package com.example.martinet.Emplitude.Reveil;

/**
 * Created by Arnaud on 04/01/2016.
 */
/*
Classe qui defini les action a faire au moment ou le reveil sonne
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    //Varianle qui fait sonner le reveil
    public  static Ringtone ringtone;
    public  static Vibrator v;
    private Switch torch;
    private View view;
    private Camera camera;
    private Camera.Parameters p ;
    ReveilActivity r;
    //MainActivity m;
    @Override
    public void onReceive(Context context, Intent intent) {

        //Afficher message de reveil sur l'appli ( adaptable sur SonnerieActivity ) -> ancien code
        //MainActivity.getTextView2().setText("Go en maths :p !");

        //Faire jouer la sonerie

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        //Le son qui va sonner ( de base j'ai mis ici TYPE_ALARM )

        ringtone = RingtoneManager.getRingtone(context, alert);
        ringtone.play();

        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        //Faire Virbrer le telephone
        if(ReveilActivity.getDSonner()) {
            long[] pattern = { 0, 2000, 1000 };
            v.vibrate(pattern,0); // faire sonne en serie quand repeter
        }
/*
        if(ReveilActivity.getDTorche()) {
            this.camera         = Camera.open();
            this.p              = camera.getParameters();
            this.torch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(p);
                        camera.startPreview();
                    } else {
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(p);
                        camera.stopPreview();
                    }
                }
            });
        }*/
        //m.lanceur();
      //  r = new ReveilActivity();
      //  r.ouvrirSonnerieActivity();
        Intent intentone = new Intent(context.getApplicationContext(), SonnerieActivity.class);
        intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentone);
        //INTENT PAR DESSUS L'ecran
        // repeter();
    }


    //methode pour couper le reveil ( appeler dans le mainactivity)
    public  static void couperSonnerie(){
        ringtone.stop();
        v.cancel();
    }




}
