package com.example.martinet.Emplitude.Emploi;

/**
 * Created by martinet on 04/01/16.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ADE_traitement {
    final String store = System.getenv("EXTERNAL_STORAGE");
    final File file = new File(store+"/c.ical");
    private Date date;
    private Boolean vide;
    private Vector<Hashtable> cours;
    private Hashtable h;
    private SimpleDateFormat dateFormat;
    private String fichier;


    public ADE_traitement(Date date) throws ParseException {
        this.date = date;
        this.init();
    }

    public ADE_traitement() throws ParseException{
        this.init();
    }

    public void init(){
        String line;
        cours = new Vector<>();
        dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                fichier += line+"\n";
            }
            this.vide = false;
        } catch (IOException e1) {
            e1.printStackTrace();
            this.vide =true;
        }

    }

    //Recupération des cours
    public Vector<Hashtable> getCours() throws ParseException {
        this.get();
        if(this.vide){
            return null;
        }
        return cours;
    }

    //Récupération dernier cour
    public Hashtable getLast() throws ParseException{
        String[] parts = fichier.split(Pattern.quote("BEGIN:VEVENT"));
        String s;
        Date d,now = new Date();
        for(int i=0; i<parts.length; i++){
            h = new Hashtable();
            s = this.element(parts[i], "DTSTART:(.)+", "DTSTART:");
            if(s != "") {
                d = dateFormat.parse(s);
                if(d.after(now)) {
                    this.cours.add(this.getCour(parts[i]));
                }
            }
        }
        Collections.sort(this.cours, new Comparator<Hashtable>() {
            public int compare(Hashtable m1, Hashtable m2) {
                Date d = (Date) m1.get("dateD");
                Date d2 = (Date) m2.get("dateD");
                return d.compareTo(d2);
            }
        });
        return this.cours.get(0);
    }

    //Récupération toutes les cours par date
    public void get() throws ParseException{
        String[] parts = fichier.split(Pattern.quote("BEGIN:VEVENT"));
        String s;
        Date d;
        for(int i=0; i<parts.length; i++){
            h = new Hashtable();
            s = this.element(parts[i], "DTSTART:(.)+", "DTSTART:");
            if(s != "") {
                d = dateFormat.parse(s);
                if(this.date.equals(d)) {
                    this.cours.add(this.getCour(parts[i]));
                }
            }
        }
    }

    public Hashtable getFirstBYDate(Date date) throws ParseException{
        String[] parts = fichier.split(Pattern.quote("BEGIN:VEVENT"));
        String s;
        Date d;
        for(int i=0; i<parts.length; i++){
            h = new Hashtable();
            s = this.element(parts[i], "DTSTART:(.)+", "DTSTART:");
            if(s != "") {
                d = dateFormat.parse(s);
                if(date.equals(d)) {
                    this.cours.add(this.getCour(parts[i]));
                }
            }
        }
        Collections.sort(this.cours, new Comparator<Hashtable>() {
            public int compare(Hashtable m1, Hashtable m2) {
                Date d = (Date) m1.get("dateD");
                Date d2 = (Date) m2.get("dateD");
                return d.compareTo(d2);
            }
        });
        return this.cours.get(0);
    }


    public String element(String chaine, String pattern, String split){
        String res= "";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(chaine);
        if(m.find()) {
            res = m.group(0);
            res = res.split(split)[1];
        }
        return res;
    }

    public String elementMulti(String chaine, String pattern, String split){
        String res= "";
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = p.matcher(chaine);
        if(m.find()) {
            res = m.group(0);
            res = res.split(split)[1];
        }
        return res;
    }

    //Récupération information de un cour
    public Hashtable getCour(String contenu) throws ParseException {
        String description, matiere = "", prof, resum, s;
        Date d2, d;
        resum = this.element(contenu, "SUMMARY:(.)+", "SUMMARY:");

        h.put("resumer", resum);
        h.put("salle", this.element(contenu, "LOCATION:(.)+", "LOCATION:"));

        description = this.elementMulti(contenu, "DESCRIPTION:(.)+UID", "DESCRIPTION:");
        Pattern p = Pattern.compile("\n");

        String[] h2 = description.split("\n");
        description="";
        for(String sss:h2){
            sss=sss.trim();
            description +=sss;
        }

        description = description.split("\\\\n[(][Exporté]")[0];
        String[] v = description.split("\\\\n");

        p = Pattern.compile("[A-Z]{3,}");
        Matcher m;

        Boolean fini = false;
        int k = v.length-2;
        prof =v[k+1];
        while(k>1 && !fini){
            m=p.matcher(v[k]);
            if(m.find()) {
                prof +=", "+v[k];
            }else{
                fini = true;
                p = Pattern.compile("[A-Z]{1}");
                m=p.matcher(v[k]);
                if(m.find()) {
                    matiere = v[k];
                }else{
                    matiere = v[k-1];
                }
            }
            k--;
        }
        s = this.element(contenu, "DTSTART:(.)+", "DTSTART:");
        d = dateFormat.parse(s);
        s = this.element(contenu, "DTEND:(.)+", "DTEND:");
        d2 = dateFormat.parse(s);

        h.put("matiere", matiere);
        h.put("prof", prof);
        h.put("dateD", d);
        h.put("dateF", d2);

        return h;

    }
}
