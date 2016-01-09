package com.example.martinet.Emplitude.Emploi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.martinet.Emplitude.R;

import java.text.SimpleDateFormat;


/**
 * Created by martinet on 04/11/15.
 */
public class Information extends AppCompatActivity {

    final static private SimpleDateFormat h = new SimpleDateFormat("HH:mm");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.cour);
        Intent intent = getIntent();
        Cour c = (Cour) intent.getSerializableExtra("cour");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(c.getResumer());

        TextView cour = (TextView) findViewById(R.id.infoCour);
        TextView ensei = (TextView) findViewById(R.id.infoEnsei);
        TextView salle = (TextView) findViewById(R.id.infoSalle);
        TextView heureD = (TextView) findViewById(R.id.infoHeureD);
        TextView heureF = (TextView) findViewById(R.id.infoHeureF);

        cour.setText(c.getMatiere());
        ensei.setText(c.getProf());
        salle.setText(c.getSalle());
        heureD.setText(h.format(c.getDateD()));
        heureF.setText(h.format(c.getDateF()));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.overridePendingTransition(android.R.anim.fade_out,android.R.anim.fade_in);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
