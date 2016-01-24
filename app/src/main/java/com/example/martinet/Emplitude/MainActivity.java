package com.example.martinet.Emplitude;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.martinet.Emplitude.Emploi.Emploi;
import com.example.martinet.Emplitude.Outil.Fichier;
import com.example.martinet.Emplitude.Parametre.Parametre;
import com.example.martinet.Emplitude.Propos.Propos;
import com.example.martinet.Emplitude.Reveil.ReveilActivity;
import com.example.martinet.Emplitude.Son.Son;
import com.example.martinet.Emplitude.Todo.Todo;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        changeFragment(null, Emploi.class);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            this.changeActivity(Parametre.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public void changeFragment(Bundle args, Class fragmentClass){
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
        } catch (Exception e) {
            System.out.println("Erreur load fragment");
        }
        FragmentManager fragementManager = getSupportFragmentManager();
        this.fragment = fragment;
        fragementManager.beginTransaction().replace(R.id.frag, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public Fragment getFragment(){
        return fragment;
    }

    public void changeActivity(Class nom){
        Intent intent = new Intent(this, nom);
        this.startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle args =new Bundle();
        Class fragmentClass = null;

        if (id == R.id.emploi) {
            fragmentClass = Emploi.class;
        } else if (id == R.id.reveil) {
            fragmentClass = ReveilActivity.class;
        } else if (id == R.id.todo) {
            fragmentClass = Todo.class;
        } else if (id == R.id.son) {
            fragmentClass = Son.class;
        } else if (id == R.id.propos) {
            this.changeActivity(Propos.class);
            return false;
        } else if (id == R.id.parametre) {
            this.changeActivity(Parametre.class);
            return false;
        }
        this.changeFragment(args, fragmentClass);
        return true;
    }


}
