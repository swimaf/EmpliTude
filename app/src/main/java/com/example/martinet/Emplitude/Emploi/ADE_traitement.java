package com.example.martinet.Emplitude.Emploi;

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


public class ADE_traitement {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

    private ADE_traitement(){}

    //Récupération toutes les cours par date
    public static Vector get(String contenu) throws ParseException{
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        String[] parts = contenu.split(Pattern.quote("BEGIN:VEVENT"));
        String s;
        Vector<Cour> cours = new Vector<>();
        for(int i=0; i<parts.length; i++){
            s = element(parts[i], "DTSTART:(.)+", "DTSTART:");
            if(s != "") {
                cours.add(getCour(parts[i]));
            }
        }
        return cours;
    }


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

    //Récupération information de un emploi_cour
    public static Cour getCour(String contenu) throws ParseException {
        String description, matiere = "", prof, resum, s, salle, resumer;
        Date d2, d;
        resum = element(contenu, "SUMMARY:(.)+", "SUMMARY:");
        resumer = resum;
        salle = element(contenu, "LOCATION:(.)+", "LOCATION:");

        description = elementMulti(contenu, "DESCRIPTION:(.)+UID", "DESCRIPTION:");

        String[] h2 = description.split("\n");
        description="";
        for(String sss:h2){
            sss=sss.trim();
            description +=sss;
        }

        description = description.split("\\\\n[(][Exporté]")[0];
        String[] v = description.split("\\\\n");

        Pattern p = Pattern.compile("[A-Z]{3,}");
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
        s = element(contenu, "DTSTART:(.)+", "DTSTART:");
        d = dateFormat.parse(s);
        s = element(contenu, "DTEND:(.)+", "DTEND:");
        d2 = dateFormat.parse(s);

        return new Cour(resumer, matiere, d, d2, prof, salle);

    }
}
