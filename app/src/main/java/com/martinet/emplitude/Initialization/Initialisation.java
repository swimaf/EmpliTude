package com.martinet.emplitude.Initialization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.Loading;
import com.martinet.emplitude.R;


/**
 * Created by martinet on 16/11/15.
 */
public class Initialisation extends FragmentActivity implements Creation {


    private Button btnNext;
    private Button btnPrevious;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Global.global.getStudent() != null) {
            initializeVariables();
        } else {
            setContentView(R.layout.initalisation);
            btnNext = (Button) findViewById(R.id.next);
            btnPrevious = (Button) findViewById(R.id.previous);
            if (savedInstanceState != null) {
                changeFragment(Selection.newInstance());
            } else {
                initFragment();
            }
        }
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


    private void initFragment() {
        changeFragment(Bienvenue.newInstance());
    }

    private void changeFragment(Fragment fragment) {
        Constants.changeFragment(this, R.id.fragment, fragment);
    }

    private void initializeVariables() {
        Intent intent = new Intent(this, Loading.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        this.startActivity(intent);
        this.finish();
    }

}
