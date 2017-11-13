package com.martinet.emplitude.Schelude;

import com.martinet.emplitude.Models.Lesson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleExtractor {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

    //Récupération toutes les cours par date
    public static List<Lesson> parse(String contenu) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/France"));
        String[] parts = contenu.split(Pattern.quote("BEGIN:VEVENT"));
        String oneLesson;
        List<Lesson> lessons = new ArrayList<>();
        for (String part : parts) {
            oneLesson = element(part, "DTSTART:(.)+", "DTSTART:");
            if (!"".equals(oneLesson)) {
                lessons.add(getLesson(part));
            }
        }
        return lessons.isEmpty() ? null : lessons;
    }

    //Récupere les informations d'un element
    private static String element(String chaine, String pattern, String split){
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
    private static String elementMulti(String chaine, String pattern, String split){
        String res= "";
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = p.matcher(chaine);
        if(m.find()) {
            res = m.group(0);
            res = res.split(split)[1];
        }
        return res;
    }

    private static Lesson getLesson(String contenu) throws ParseException {
        String  summary     = element(contenu, "SUMMARY:(.)+", "SUMMARY:"); //Récupère le sommaire du cours
        String  location    = element(contenu, "LOCATION:(.)+", "LOCATION:"); //Récupère la location du cours
        Date    endDate     = dateFormat.parse(element(contenu, "DTSTART:(.)+", "DTSTART:")); //Récuperation de la date du début du cours
        Date    beginDate   = dateFormat.parse(element(contenu, "DTEND:(.)+", "DTEND:"));     //Récuperation de la date de fin du cours
        String  description = elementMulti(contenu, "DESCRIPTION:(.)+", "DESCRIPTION:"); //Récupère la description du cours
        Lesson lesson = new Lesson(summary, null, endDate, beginDate, null, location);

        if(description.contains("Matière :") || description.contains("Salle :") || description.contains("Personnel :")) {
            return PatternWithField.getDescription(lesson, description);
        } else {
            return PatternWithoutField.getDescription(lesson, description);
        }

    }

    /**
     * Created by martinet on 21/08/16.
     */

    public static class PatternWithField {

        private final static String FIELD_DISCIPLINE = "Matière :";
        private final static String FIELD_PERSONNEL = "Personnel :";
        private final static String FIELD_NOTE = "Remarques :";
        private final static String FIELD_GROUP = "Groupe :";

        public static Lesson getDescription(Lesson lesson, String description) {
            String teacher = null, discipline = null;

            String[] splits = description.split("\\\\n");
            for(String split:splits){
                split=split.trim();
                if(split.contains(FIELD_DISCIPLINE)) {
                    discipline = split.replace(FIELD_DISCIPLINE, "").trim();
                }
                if(split.contains(FIELD_NOTE)) {
                    lesson.setNote(split.replace(FIELD_NOTE, "").trim());
                }
                if(split.contains(FIELD_GROUP)) {
                    lesson.setGroup(split.replace(FIELD_GROUP, "").trim());
                }
                if(split.contains(FIELD_PERSONNEL)) {
                    teacher = split.replace(FIELD_PERSONNEL, "").trim().replaceAll("\\\\", "");
                    String[] teachers = teacher.split(",");
                    teacher ="";
                    for(int i=0; i<teachers.length; i++) {
                        teacher += i%2 == 0 || i == teachers.length-1 ? teachers[i] : teachers[i] + ",";
                    }
                }
            }
            lesson.setPersonnel(teacher);
            lesson.setDiscipline(discipline);
            return lesson;
        }
    }

    /**
     * Created by martinet on 21/08/16.
     */

    public static class PatternWithoutField {

        public static Lesson getDescription(Lesson lesson, String description) {
            String teacher, discipline = null;

            //Nettoyage du champs description
            String[] splits = description.split("\n");
            description="";
            for(String split:splits){
                split=split.trim();
                description +=split;
            }

            description = description.split("\\\\n[(][Exporté]")[0];
            String[] fields = description.split("\\\\n");

            Pattern teacherPattern = Pattern.compile("[A-Z]{3,}");
            Matcher matcher;

            Boolean fini = false;
            int k = fields.length-2;
            teacher =fields[k+1]; //Récupère le premier prof
            while(k>1 && !fini){
                matcher=teacherPattern.matcher(fields[k]);
                if(matcher.find()) {
                    //Si il y a plusieurs prof pour le cours
                    teacher +=", "+fields[k];
                }else{
                    fini = true;
                    //Pattern de récuperation de la matière
                    teacherPattern = Pattern.compile("[A-Z]");
                    matcher=teacherPattern.matcher(fields[k]);
                    if(matcher.find()) {
                        discipline = fields[k];
                    }else{
                        discipline = fields[k-1];
                    }
                }
                k--;
            }
            lesson.setPersonnel(teacher);
            lesson.setDiscipline(discipline);
            return lesson;
        }
    }
}
