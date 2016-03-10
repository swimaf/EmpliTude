package com.martinet.emplitude.Todo;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.martinet.emplitude.MyApplication;
import com.martinet.emplitude.R;

/**
 * Created by Flo on 09/03/2016.
 */
public class NotifTache extends BroadcastReceiver {

    private MyApplication monApplication;

    @Override
    public void onReceive(Context context, Intent intent) {
        monApplication = (MyApplication) context.getApplicationContext();
        Tache t = (Tache) monApplication.mesTaches.get(intent.getIntExtra("indexTache", 0));
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_list)
                        .setContentTitle("Empli'tude")
                        .setContentText(t.getNom()+" dans pas longtemps");

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(10, mBuilder.build());
    }
}
