package com.martinet.emplitude.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.Initialization.Initialisation;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.R;

public class Settings extends PreferenceActivity {

    private AppCompatDelegate mDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.settings_general, new General()).commit();
    }

    public void reset() {
        if(Constants.CONNECTED()) {
            Global.global.removeStudent();
            Intent intent = new Intent(Settings.this, Initialisation.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Vous devez être connecté à internet !", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteColor() {
        SharedPreferences settings = this.getSharedPreferences("Couleur", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
        Toast.makeText(this, "Couleurs supprimées", Toast.LENGTH_SHORT).show();
    }

    public static class General extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_general);
            Preference resetSettings = findPreference("settings_reset");
            resetSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    ((Settings) getActivity()).reset();
                    return true;
                }
            });
            Preference colorSettings = findPreference("settings_colors");
            colorSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    ((Settings) getActivity()).deleteColor();
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    private void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    private ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }


    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
}