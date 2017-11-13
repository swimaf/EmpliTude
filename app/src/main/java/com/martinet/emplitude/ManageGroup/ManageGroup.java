package com.martinet.emplitude.ManageGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.martinet.emplitude.Global;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.R;

public class ManageGroup extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setContentView(R.layout.manage_group);

        ListView listView = (ListView) findViewById(R.id.tvGroups) ;
        ManageGroupAdapter adapter = new ManageGroupAdapter(this, Global.global.getStudent().getGroups());
        listView.setAdapter(adapter);
        findViewById(R.id.addGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(GroupAdd.class);
            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openActivity(MainActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void openActivity(Class target) {
        Intent intent = new Intent(getApplicationContext(), target);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        intent.putExtra("openDrawer", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        openActivity(MainActivity.class);
    }

}

