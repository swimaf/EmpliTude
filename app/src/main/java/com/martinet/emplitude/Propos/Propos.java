package com.martinet.emplitude.Propos;
/**
 * Created by martinet on 17/11/15.
 */
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;


import com.martinet.emplitude.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Propos extends AppCompatActivity {

    ExpandableListView expandableListView;
    HashMap expandableListDetail;
    ArrayList<String> expandableListTitle;
    ExpandableListAdapter expandableListAdapter;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.propos);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpendableView.getData();
        expandableListTitle = new ArrayList(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

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