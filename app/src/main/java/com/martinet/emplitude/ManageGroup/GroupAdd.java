package com.martinet.emplitude.ManageGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Initialization.Creation;
import com.martinet.emplitude.Initialization.SelectionCity;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.R;

public class GroupAdd extends AppCompatActivity implements Creation {

    private Button btnNext;
    private Button btnPrevious;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setContentView(R.layout.group_add);
        this.btnNext = (Button) findViewById(R.id.btnNext);
        this.btnPrevious = (Button) findViewById(R.id.btnPrevious);
        Constants.changeFragment(this, R.id.selection, SelectionCity.newInstance());

    }

    @Override
    public View.OnClickListener onRevert() {
        return null;
    }

    @Override
    public View.OnClickListener onValidate() {
        return null;
    }

    @Override
    public Button getBtnNext() {
        return btnNext;
    }

    @Override
    public Button getBtnPrevious() {
        return btnPrevious;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return this;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openMainActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        intent.putExtra("openDrawer", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        openMainActivity();
    }

}

