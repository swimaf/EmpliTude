package com.martinet.emplitude.Models;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.Tool.Fichier;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Lesson implements Serializable {

    private static int i = 0;

    private int id;
    private String discipline;
    private Date dateBegin;
    private Date dateEnd;
    private String personnel;
    private String location;
    private String summary;
    private String note;
    private String group;

    public Lesson(String summary, String discipline, Date dateBegin, Date dateEnd, String personnel, String location){
        this.summary = summary;
        this.discipline = discipline;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.personnel = personnel;
        this.location = location;
        this.id = i;
        i++;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPersonnel() {
        return personnel;
    }

    public void setPersonnel(String personnel) {
        this.personnel = personnel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    /**
     *     Récupération des cours
     */
    public static List<Lesson> getLessons(Global global) {
        return global.getLessons();
    }

    public static Lesson getNext(Global global){
        final Date date = new Date();

        Constants.Predicate<Lesson> predicateLesson = new Constants.Predicate<Lesson>() {
            public boolean apply(Lesson lesson) {
                return lesson.getDateBegin().after(date);
            }
        };
        if(global.getLessons() == null) {
            return null;
        }
        List<Lesson> lessons = Constants.filter(global.getLessons(), predicateLesson);
        return getFirstOfList(lessons);
    }

    public Boolean compareDay(Calendar date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateBegin);
        return calendar.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR);
    }

    //Récupération de tous les cours par date après la date du jour
    public static List<Lesson> getLessonsByDay(Global application, Date date){
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Constants.Predicate<Lesson> predicateLesson = new Constants.Predicate<Lesson>() {
            public boolean apply(Lesson lesson) {
                return lesson.compareDay(calendar);
            }
        };
        if(application.getLessons() == null) {
            return null;
        }
        return Constants.filter(application.getLessons(), predicateLesson);
    }


    /**     *     Récupération du premier cours d'une journée
     */
    public static Lesson getNextLessonForAlarm() {
        if(Global.global.getLessons() == null) {
            return null;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        Constants.Predicate<Lesson> predicateLesson = new Constants.Predicate<Lesson>() {
            public boolean apply(Lesson lesson) {
                return lesson.getDateBegin().after(calendar.getTime());
            }
        };
        List<Lesson> lessons = Constants.filter(Global.global.getLessons(), predicateLesson);
        return getFirstOfList(lessons);
    }

    public static Lesson getFirstOfList(List<Lesson> lessons) {
        Collections.sort(lessons, new Comparator<Lesson>() {
            public int compare(Lesson lesson, Lesson lesson2) {
                return lesson.getDateBegin().compareTo(lesson2.getDateBegin());
            }
        });
        return lessons.isEmpty() ? null : lessons.get(0);
    }

    public static Lesson getFirstDay() {
        List<Lesson> lessons = Global.global.getLessons();
        Collections.sort(lessons, new Comparator<Lesson>() {
            public int compare(Lesson lesson, Lesson lesson2) {
                return lesson.getDateBegin().compareTo(lesson2.getDateBegin());
            }
        });
        return lessons.isEmpty() ? null : lessons.get(0);
    }

    public static Lesson getLastDay() {
        List<Lesson> lessons = Global.global.getLessons();
        Collections.sort(lessons, new Comparator<Lesson>() {
            public int compare(Lesson lesson, Lesson lesson2) {
                return lesson.getDateBegin().compareTo(lesson2.getDateBegin());
            }
        });
        return lessons.isEmpty() ? null : lessons.get(lessons.size()-1);
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String toString() {
        return discipline;
    }

    public static void saveLesson(List<Lesson> lessons) {
        new Fichier<Lesson>().write(Global.global.getStudent().getDefaultGroup().getName()+".cours", lessons, Global.global);
    }

    public boolean equals(Object object) {
        Lesson lesson = (Lesson) object;
        return lesson.getId() == id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
