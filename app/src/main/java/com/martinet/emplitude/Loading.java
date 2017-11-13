package com.martinet.emplitude;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.martinet.emplitude.Alarm.ProgrammAlarm;
import com.martinet.emplitude.Alarm.ReveilActivity;
import com.martinet.emplitude.Models.Lesson;
import com.martinet.emplitude.Models.Task;
import com.martinet.emplitude.Schelude.SchelureSelf;
import com.martinet.emplitude.Tool.Fichier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinet on 22/08/16.
 */


public class Loading extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        openMainActivity();
    }



    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        this.startActivity(intent);
        this.finish();
    }

}
