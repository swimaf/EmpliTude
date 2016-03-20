package com.martinet.emplitude;

import android.content.Intent;
import android.content.SharedPreferences;
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
 * Classe permettant de choisir son groupe ou de renseigner son n° d'enseignant
 */
public class Accueil extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ADE_retour, External_retour {

    private Boolean is_etudiant = true;
    private RelativeLayout etudiant;
    private RelativeLayout enseignant;
    private Spinner spinner;
    private Spinner spinner2;
    private Button suivant;
    private RadioButton button1;
    private RadioButton button2;
    private EditText num_enseignant;
    private TreeMap<String, TreeMap> groupe;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.preference = getSharedPreferences(Constants.PREFERENCE_ADE, 0);
        this.editor = preference.edit();

        if(Fichier.existe(Constants.IDENTIFIANT_FILE, getBaseContext())){
            main();
        }else {
            setContentView(R.layout.choix);
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
                    new External(this, new URL(Constants.SITE)).execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }else{
                this.noConnect();
            }
        }
    }

    /*Redirection vers la classe d'principal*/
    private void main(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        this.startActivity(intent);
        this.finish();
    }

    /*Si la requete de récuperation de groupe à fonctionné on charge les liste déroulante*/
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

    /*Affiche une barre de chargement avant la requete asynchrone qui va aller chercher l'emploi du temps*/
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
        Fichier.ecrire(Constants.IDENTIFIANT_FILE,getBaseContext(), e);
        ADE_recuperation load = new ADE_recuperation(Accueil.this, getBaseContext());
        load.execute();
    }

    /*Lors du clic sur un bouton : choix entre étudiant et enseignant*/
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

    /*Lorsque l'utilisateur clic sur un département*/
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<String> list = new ArrayList<>(groupe.get(parent.getItemAtPosition(position)).keySet());
        ArrayAdapter<String> info = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner2.setAdapter(info);
    }

    public void onNothingSelected(AdapterView<?> parent) {}


    /*Lorsque la requete asynchrone est fini on change de fenetre */
    public void retour(int value){
        if(value == ADE_recuperation.NO_ERREUR) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Echec de la récupération de ADE veuillez réssayer plus tard", Toast.LENGTH_LONG).show();
        }
    }

    /*Lorsque la requete asynchrone est finis on charge les listes déroulante */
    public void affichage(String value) {
        groupe = new TreeMap<>();
        String d;
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