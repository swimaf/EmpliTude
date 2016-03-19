package com.martinet.emplitude;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.martinet.emplitude.Emploi.ADE_recuperation;
import com.martinet.emplitude.Emploi.ADE_retour;
import com.martinet.emplitude.Outil.Enseignant;
import com.martinet.emplitude.Outil.Etudiant;
import com.martinet.emplitude.Outil.External;
import com.martinet.emplitude.Outil.External_retour;
import com.martinet.emplitude.Outil.Fichier;
import com.martinet.emplitude.Outil.Utilisateur;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


/**
 * Created by martinet on 16/11/15.
 */
public class Accueil extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ADE_retour, External_retour {

    final private static String PREFS_NAME = "Ade";

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    final private String SITE = "http://climax.heb3.org/Emplitude/recup/";

    private Boolean is_etudiant = true;
    private RelativeLayout etudiant;
    private RelativeLayout enseignant;
    private Spinner spinner;
    private Spinner spinner2;
    private Button suivant;
    private RadioButton button1;
    private RadioButton button2;
    private EditText num_enseignant;
    private Boolean appel;
    private TreeMap<String, TreeMap> groupe;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.preference = getSharedPreferences(PREFS_NAME, 0);
        this.editor = preference.edit();

        if(Fichier.existe(Constants.identifiantFile, getBaseContext())){
            /*if (Build.VERSION.SDK_INT >= 23) {
                appel = true;
                permission();
            } else {*/
                main();
            //}
        }else {
            setContentView(R.layout.choix);
            appel = false;
            etudiant = (RelativeLayout) findViewById(R.id.etudiant);
            enseignant = (RelativeLayout) findViewById(R.id.enseignant);
            spinner = (Spinner) findViewById(R.id.spinner);
            spinner2 = (Spinner) findViewById(R.id.spinner2);
            button1 = (RadioButton) findViewById(R.id.radioButton);
            button2 = (RadioButton) findViewById(R.id.radioButton2);
            num_enseignant = (EditText) findViewById(R.id.numero);

            editor.putInt("rafraichissement", 7);
            editor.commit();
            if(Constants.CONNECTED(getBaseContext())){
                try {
                    new External(this, new URL(SITE)).execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }else{
                this.noConnect();
            }
        }
    }

    private void main(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        this.startActivity(intent);
        this.finish();
    }

    private void connect(){
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        spinner.setOnItemSelectedListener(this);

        List<String> list = new ArrayList<>(groupe.keySet());
        ArrayAdapter<String> departement = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(departement);

        suivant = (Button) findViewById(R.id.button);
        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }

    private void noConnect(){
        Toast.makeText(getApplicationContext(), "Vous devez être connecté à internet !", Toast.LENGTH_SHORT).show();
    }

    private void next(){
        Utilisateur e;
        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);
        suivant.setEnabled(false);
        if(is_etudiant){
            String departement =(String) spinner.getSelectedItem();
            String no_groupe =(String)spinner2.getSelectedItem();
            String numero =(String)groupe.get(departement).get(no_groupe);
            e = new Etudiant(numero, no_groupe);
        }else{
            e = new Enseignant(num_enseignant.getText().toString());
        }
        Fichier.ecrire(Constants.identifiantFile,getBaseContext(), e);
        ADE_recuperation load = new ADE_recuperation(Accueil.this, getBaseContext());
        load.execute();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(appel){
                        this.main();
                    }else{
                        this.next();
                    }
                } else {
                    Toast.makeText(this, "NON N'ACCEPTE", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    @Override
    public void onClick(View v) {
        RadioButton b = (RadioButton)v;
        switch (b.getId()) {
            case R.id.radioButton:
                this.is_etudiant = true;
                etudiant.setVisibility(View.VISIBLE);
                enseignant.setVisibility(View.GONE);
                break;
            case R.id.radioButton2:
                this.is_etudiant = false;
                etudiant.setVisibility(View.GONE);
                enseignant.setVisibility(View.VISIBLE);
                break;
            default:break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        List list = new ArrayList<>(groupe.get(parent.getItemAtPosition(position)).keySet());
        ArrayAdapter<String> info = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner2.setAdapter(info);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void retour(int value){
        if(value == ADE_recuperation.NO_ERREUR) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Echec de la récupération de ADE veuillez réssayer plus tard", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void affichage(String value) {
        groupe = new TreeMap<String, TreeMap>();
        String d ="";
        try {
            JSONArray json = new JSONArray(value);
            for(int i=0; i<json.length(); i++) {
                d = json.getJSONObject(i).getString("departement");
                if (!groupe.containsKey(d)){
                    groupe.put(json.getJSONObject(i).getString("departement"), new TreeMap<String, String>());
                }
                groupe.get(d).put(json.getJSONObject(i).getString("nom_groupe"), json.getJSONObject(i).getString("id_groupe"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        this.connect();
    }

}