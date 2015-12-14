package com.example.martinet.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;


/**
 * Created by martinet on 16/11/15.
 */
public class Introduction extends Activity implements View.OnClickListener {

    final private String store = System.getenv("EXTERNAL_STORAGE") ;
    final private File file = new File(this.store+"/.identifiant.txt");

    private Button suivant;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(file.exists()){
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }else {
            this.setContentView(R.layout.introduction);
            this.suivant = (Button)this.findViewById(R.id.suivant);
            this.suivant.setOnClickListener(this);
        }


    }
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void onClick(View v) {
        if(isOnline()) {
            Intent intent = new Intent(this, Accueil.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }else{
            Toast.makeText(getApplicationContext(), "Vous devez être connecté à internet !", Toast.LENGTH_SHORT).show();
        }
    }


}