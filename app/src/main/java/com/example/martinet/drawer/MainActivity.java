package com.example.martinet.drawer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.martinet.drawer.Emploi.Emploi;
import com.example.martinet.drawer.Emploi.Information;
import com.example.martinet.drawer.Emploi.Jour;
import com.example.martinet.drawer.Parametre.Parametre;
import com.example.martinet.drawer.Propos.Propos;
import com.example.martinet.drawer.Reveil.Reveil;
import com.example.martinet.drawer.Son.Son;
import com.example.martinet.drawer.Todo.Todo;

import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static Jour calendrier = new Jour(new Date());
    static Boolean is_emploi = true;
    public static Boolean premier = true;
    private View v;
    private ActionBarDrawerToggle toggle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        changeToolbar();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void changeToolbar(){
        ViewGroup tool = (ViewGroup) findViewById(R.id.tool);
        LayoutInflater vi = LayoutInflater.from(this);
        View v = vi.inflate(R.layout.emploi_action, null);
        this.v = v;
        tool.addView(v);
        modifierDate(0);
        ImageButton pre = (ImageButton) findViewById(R.id.precedent);
        ImageButton next = (ImageButton) findViewById(R.id.suivant);
        pre.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity.this.modifierDate(-1);
            }
        });
        next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.modifierDate(1);
            }
        });

    }
    public void modifierDate(int i){
        calendrier.ajouterJour(i);
        TextView jour = (TextView) findViewById(R.id.jour);
        jour.setText(calendrier.getJour());
        jour.refreshDrawableState();
        if (i!=0 || premier) {
            System.out.println("sss");
            Fragment fragment = null;
            Class fragmentClass = Emploi.class;
            Bundle args =new Bundle();
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                args.putString("dateJour", calendrier.getDateJour());
                fragment.setArguments(args);
            } catch (Exception e) {
                System.out.println("Erreur load fragment");
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(!premier) {
                if (i < 0) {
                    ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else {
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
            premier = false;
            ft.replace(R.id.frag, fragment);
            ft.commit();
        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            this.changeActivity(Parametre.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onResume(){
        super.onResume();
        System.out.println("sss");
        if(is_emploi){
            System.out.println("sss2");

            v.setVisibility(View.VISIBLE);
        }
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
        fragementManager.beginTransaction().replace(R.id.frag, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
        this.v.setVisibility(View.GONE);
        if (id == R.id.emploi) {
            this.v.setVisibility(View.VISIBLE);
            is_emploi = true;
            fragmentClass = Emploi.class;
            v.setVisibility(View.VISIBLE);
            args.putString("dateJour", calendrier.getDateJour());
        } else if (id == R.id.reveil) {
            fragmentClass = Reveil.class;
            is_emploi = false;
        } else if (id == R.id.todo) {
            fragmentClass = Todo.class;
            is_emploi = false;
        } else if (id == R.id.son) {
            fragmentClass = Son.class;
            is_emploi = false;
        } else if (id == R.id.propos) {
            this.changeActivity(Propos.class);
            return false;
        } else if (id == R.id.parametre) {
            this.changeActivity(Parametre.class);
            return false;
        }
        this.changeFragment(args,fragmentClass);
        return true;
    }


}
