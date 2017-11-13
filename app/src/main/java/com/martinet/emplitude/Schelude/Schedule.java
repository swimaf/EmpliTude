package com.martinet.emplitude.Schelude;

import android.content.SharedPreferences;

import com.martinet.emplitude.Alarm.ProgrammAlarm;
import com.martinet.emplitude.Alarm.ReveilActivity;
import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.Models.Lesson;
import com.martinet.emplitude.Models.Student;
import com.martinet.emplitude.Models.Task;
import com.martinet.emplitude.Tool.Resource;
import com.martinet.emplitude.Tool.Response;
import com.martinet.emplitude.Tool.Fichier;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * Created by martinet on 16/08/16.
 */

public class Schedule implements Response<String, String> {

    private final static int NEW_YEAR = 196;
    private Response response;

    public void load(Response response) {
        load(response, Global.global.getStudent());
    }

    public void load(Response response, Student student) {
        try {
            this.response = response;
            URL url = new URL(String.format(student.getDefaultGroup().getSchool().getUrl(), student.getDefaultGroup().getIdentifiant()));
            new Resource(this, student.getDefaultGroup().getPseudo(), student.getDefaultGroup().getPassword()).execute(url);
        } catch (MissingFormatArgumentException e) {
            URL url;
            try {
                Calendar calendar = Calendar.getInstance();
                if(calendar.get(Calendar.DAY_OF_YEAR) < NEW_YEAR ) {
                    calendar.add(Calendar.YEAR, 1);
                }
                url = new URL(String.format(student.getDefaultGroup().getSchool().getUrl(), student.getDefaultGroup().getIdentifiant(), (calendar.get(Calendar.YEAR))+"-08-01", (calendar.get(Calendar.YEAR)+1)+"-07-01"));
                new Resource(this, student.getDefaultGroup().getPseudo(), student.getDefaultGroup().getPassword()).execute(url);
            }catch (Exception e2){
                response.onError("URL mal formée.");
            }
        } catch (MalformedURLException e) {
            response.onError("URL mal formée.");
        }
    }

    public List<Lesson> get(Response response, Student student) {
        return null;
    }

    @Override
    public void onSuccess(String value) {
        try {
            List lessons = ScheduleExtractor.parse(value);
            Lesson.saveLesson(lessons);
            Global.global.setLessons(lessons);

            ProgrammAlarm.setAlarm(Global.global.getApplicationContext());

            response.onSuccess("Mise à jour terminée.");
        } catch (Exception e) {
            response.onError("Echec de la mise à jour.");
        }
    }

    @Override
    public void onError(String message) {
        response.onError(message);
    }

}
