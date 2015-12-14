package com.example.martinet.drawer.Emploi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.martinet.drawer.R;


/**
 * Created by martinet on 04/11/15.
 */
public class Information extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.cour);
        Intent intent = getIntent();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(intent.getStringExtra("resumer"));

        TextView cour = (TextView) findViewById(R.id.infoCour);
        TextView ensei = (TextView) findViewById(R.id.infoEnsei);
        TextView salle = (TextView) findViewById(R.id.infoSalle);
        TextView heureD = (TextView) findViewById(R.id.infoHeureD);
        TextView heureF = (TextView) findViewById(R.id.infoHeureF);

        cour.setText(intent.getStringExtra("matiere"));
        ensei.setText(intent.getStringExtra("prof"));
        salle.setText(intent.getStringExtra("salle"));
        heureD.setText(intent.getStringExtra("dateD"));
        heureF.setText(intent.getStringExtra("dateF"));
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
