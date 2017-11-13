package com.martinet.emplitude.Models;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Flo on 04/01/2016.
 */
public class Task implements Serializable{

    private static int SERIAL = 0;
    private int id;
    private String nom, matiere;
    private Date date;

    public Task(String nom, String matiere, Date date){
        this.nom = nom;
        this.date = date;
        this.matiere = matiere;
        this.id = SERIAL;
        SERIAL++;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMatiere() { return matiere; }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId(){ return this.id;}


    public static List<Task> getTasksByLesson(final String matiere, final Date date){
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Constants.Predicate<Task> predicateLesson = new Constants.Predicate<Task>() {
            public boolean apply(Task task) {
                Calendar calendarTask = Calendar.getInstance();
                calendarTask.setTime(task.getDate());
                return task.getMatiere().equals(matiere) && calendar.get(Calendar.DAY_OF_YEAR) == calendarTask.get(Calendar.DAY_OF_YEAR);
            }
        };
        List<Task> tasks = Constants.filter(Global.global.getTasks(), predicateLesson);
        Collections.sort(tasks, new Comparator<Object>() {
            public int compare(Object m1, Object m2) {
                Date d = ((Task) m1).getDate();
                Date d2 = ((Task) m2).getDate();
                return d.compareTo(d2);
            }
        });
        return tasks;
    }
}