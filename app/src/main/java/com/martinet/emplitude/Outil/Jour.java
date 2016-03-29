package com.martinet.emplitude.Outil;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Jour {
    private Calendar calendar;
    final private String[] semaine ={"DIM.", "LUN.", "MAR.", "MER.", "JEU.", "VEN.", "SAM."};
    final private String[] mois ={"JANVIER.", "FEVRIER.", "MARS", "AVRIL", "MAI", "JUIN", "JUILLET", "AOUT", "SEPTEMBRE", "OCTOBRE", "NOVEMBRE", "DECEMBRE"};
    public Jour(Date d){
        this.calendar = Calendar.getInstance(Locale.FRANCE);
        this.calendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        this.calendar.setTime(d);
    }
    public Boolean est_inferieur(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return calendar.before(c);
    }
    public String getJour(){
        return semaine[this.calendar.get(Calendar.DAY_OF_WEEK)-1]+" "+this.calendar.get(Calendar.DATE)+" "+mois[this.calendar.get(Calendar.MONTH)];
    }
    public Date getDate(){
        return this.calendar.getTime();
    }
    public String getUrl(){
        return this.calendar.get(Calendar.YEAR)+"-"+(this.calendar.get(Calendar.MONTH)+1)+"-"+this.calendar.get(Calendar.DATE);
    }
    public String getDateJour(){
        return this.calendar.get(Calendar.DATE)+"/"+(this.calendar.get(Calendar.MONTH)+1);
    }
    public void ajouterJour(int i){
        this.calendar.add(Calendar.DATE, i);
    }
}
