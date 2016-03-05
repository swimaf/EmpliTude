package com.martinet.emplitude.Repas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.martinet.emplitude.R;

import java.util.Date;

/**
 * Classe permettant l'affichage complet du jour ainsi que sa liste de tâche associée
 */
public class RepasJour extends AppCompatActivity {

    private Date dateJour;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.repas_page);
        Intent intent = getIntent();
        this.dateJour = new Date((intent.getLongExtra("date", 0)));
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
