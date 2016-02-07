package com.martinet.emplitude;

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
import android.view.View;
import android.widget.TextView;

import com.martinet.emplitude.Emploi.Emploi;
import com.martinet.emplitude.Outil.Fichier;
import com.martinet.emplitude.Outil.Utilisateur;
import com.martinet.emplitude.Parametre.Parametre;
import com.martinet.emplitude.Propos.Propos;
import com.martinet.emplitude.Reveil.ReveilActivity;
import com.martinet.emplitude.Son.Son;
import com.martinet.emplitude.Todo.Todo;


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

        View headerLayout = navigationView.getHeaderView(0);
        Utilisateur utilisateur = (Utilisateur) Fichier.lire(Constants.identifiantFile,getApplicationContext(), 0);
        TextView type = (TextView) headerLayout.findViewById(R.id.type);
        type.setText(utilisateur.toString());


        Todo.mesTaches = Fichier.readAll(Constants.tacheFile, getBaseContext());

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
