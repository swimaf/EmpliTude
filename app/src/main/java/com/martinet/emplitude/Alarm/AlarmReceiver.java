package com.martinet.emplitude.Alarm;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;


public class AlarmReceiver extends WakefulBroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context.getApplicationContext(), SonnerieActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

    }
}

