package com.martinet.emplitude.Emploi;
/**
 * Created by martinet on 04/01/16.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe permettant le traitement du fichier iCal
 */

public class ADE_traitement {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

    private ADE_traitement(){}

    //Récupération toutes les cours par date
    public static Vector get(String contenu) throws ParseException{
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String[] parts = contenu.split(Pattern.quote("BEGIN:VEVENT"));
        String s;
        Vector<Cours> cours = new Vector<>();
        for(int i=0; i<parts.length; i++){
            s = element(parts[i], "DTSTART:(.)+", "DTSTART:");
            if(s != "") {
                cours.add(getCour(parts[i]));
            }
        }
        return cours;
    }

    //Récupere les informations d'un element
    public static String element(String chaine, String pattern, String split){
        String res= "";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(chaine);
        if(m.find()) {
            res = m.group(0);
            res = res.split(split)[1];
        }
        return res;
    }

    //Récupere les informations d'un element multiple ligne
    public static String elementMulti(String chaine, String pattern, String split){
        String res= "";
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = p.matcher(chaine);
        if(m.find()) {
            res = m.group(0);
            res = res.split(split)[1];
        }
        return res;
    }

    public static Cours getCour(String contenu) throws ParseException {
        String description, matiere = "", prof, resum, s, salle, resumer;
        Date d2, d;
        resum = element(contenu, "SUMMARY:(.)+", "SUMMARY:"); //Récupère le sommaire du cours
        resumer = resum;
        salle = element(contenu, "LOCATION:(.)+", "LOCATION:"); //Récupère la salle du cours

        description = elementMulti(contenu, "DESCRIPTION:(.)+UID", "DESCRIPTION:"); //Récupère la description du cours

        //Nettoyage du champs description
        String[] h2 = description.split("\n");
        description="";
        for(String sss:h2){
            sss=sss.trim();
            description +=sss;
        }

        description = description.split("\\\\n[(][Exporté]")[0];
        String[] v = description.split("\\\\n");

        //Pattern de recherche de l'enseignant
        Pattern p = Pattern.compile("[A-Z]{3,}");
        Matcher m;

        Boolean fini = false;
        int k = v.length-2;
        prof =v[k+1]; //Récupère le premier prof
        while(k>1 && !fini){
            m=p.matcher(v[k]);
            if(m.find()) {
                //Si il y a plusieurs prof pour le cours
                prof +=", "+v[k];
            }else{
                fini = true;
                //Pattern de récuperation de la matière
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
        s = element(contenu, "DTSTART:(.)+", "DTSTART:"); //Récuperation de la date du début du cours
        d = dateFormat.parse(s);                          //Formatage de la date
        s = element(contenu, "DTEND:(.)+", "DTEND:");     //Récuperation de la date de fin du cours
        d2 = dateFormat.parse(s);

        return new Cours(resumer, matiere, d, d2, prof, salle);

    }
}
