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
import android.widget.ImageButton;
import android.widget.TextView;

import com.martinet.emplitude.About.About;
import com.martinet.emplitude.Alarm.ReveilActivity;
import com.martinet.emplitude.ManageGroup.GroupAdd;
import com.martinet.emplitude.ManageGroup.ManageGroup;
import com.martinet.emplitude.Models.Group;
import com.martinet.emplitude.Schelude.Display.ActivitySchelude;
import com.martinet.emplitude.Settings.Settings;
import com.martinet.emplitude.Sound.Son;
import com.martinet.emplitude.Todo.Todo;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment;
    private NavigationView navigationView;
    private ImageButton iconExpend;
    private MenuItem menuItem;
    private TextView type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        changeFragment(null, ActivitySchelude.class);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.emploi);
        View headerLayout = navigationView.getHeaderView(0);
        type = (TextView) headerLayout.findViewById(R.id.type);
        headerLayout.findViewById(R.id.listGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGroup(v);
            }
        });
        iconExpend = (ImageButton) headerLayout.findViewById(R.id.icon);
        type.setText(Global.global.getStudent().toString());
        initalisationListGroup(getIntent().getBooleanExtra("openDrawer", false));
    }

    private void initalisationListGroup(Boolean open) {
        for(Group group:Global.global.getStudent().getGroups()) {
            if(Global.global.getStudent().getDefaultGroup().equals(group)) {
                menuItem = navigationView.getMenu().add(R.id.listGroups, Menu.NONE, Menu.NONE, group.getName())
                        .setIcon(R.drawable.ic_radio_checked)
                        .setTitleCondensed(group.getIdentifiant())
                        .setChecked(true);
            } else {
                navigationView.getMenu().add(R.id.listGroups, Menu.NONE, Menu.NONE, group.getName())
                        .setIcon(R.drawable.ic_radio_unchecked)
                        .setTitleCondensed(group.getIdentifiant());
            }
        }
        navigationView.getMenu().setGroupVisible(R.id.listGroups, false);
        if(open) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
            onClickGroup(null);
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
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            this.changeActivity(Settings.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public void onClickGroup(View v) {
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.other);
        menuItem.setVisible(!menuItem.isVisible());
        menu.setGroupVisible(R.id.nav, menuItem.isVisible());
        menu.setGroupVisible(R.id.actionGroup, !menuItem.isVisible());
        menu.setGroupVisible(R.id.listGroups, !menuItem.isVisible());
        iconExpend.setRotation(iconExpend.getRotation()+180);

    }

    public void changeFragment(Bundle args, Class fragmentClass){
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
        } catch (Exception e) {
            System.out.println("Error load fragment");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        this.fragment = fragment;
        fragmentManager.beginTransaction().replace(R.id.frag, fragment).commit();

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

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle args =new Bundle();
        Class fragmentClass = null;

        if (id == R.id.emploi) {
            fragmentClass = ActivitySchelude.class;
        } else if (id == R.id.reveil) {
            fragmentClass = ReveilActivity.class;
        } else if (id == R.id.todo) {
            fragmentClass = Todo.class;
        } else if (id == R.id.son) {
            fragmentClass = Son.class;
        } else if (id == R.id.propos) {
            this.changeActivity(About.class);
            return false;
        } else if (id == R.id.manageGroups) {
            this.changeActivity(ManageGroup.class);
            this.finish();
            return false;
        }else if (id == R.id.addGroup) {
            this.changeActivity(GroupAdd.class);
            this.finish();
            return false;
        } else if (id == R.id.parametre) {
            this.changeActivity(Settings.class);
            this.finish();
            return false;
        } else if(item.getGroupId() == R.id.listGroups) {
            if(!item.isChecked()) {
                Global.global.getStudent()
                        .setDefaultGroup(Global.global.getStudent().getGroupById(item.getTitleCondensed().toString()));
                menuItem.setChecked(false);
                menuItem.setIcon(R.drawable.ic_radio_unchecked);
                menuItem = item;
                item.setChecked(true);
                item.setIcon(R.drawable.ic_radio_checked);
                Global.global.initVariables();
                changeFragment(null, ActivitySchelude.class);
                type.setText(Global.global.getStudent().toString());
            }
            return false;
        }
        this.changeFragment(args, fragmentClass);
        return true;
    }


}
