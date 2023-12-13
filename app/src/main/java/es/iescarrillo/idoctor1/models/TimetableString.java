package es.iescarrillo.idoctor1.models;


import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.time.LocalTime;


import java.time.format.DateTimeFormatter;


@IgnoreExtraProperties
public class TimetableString implements Serializable {

    private String dayOfWeek;

    private String startTime;

    private String endTime;

    private String id;

    private String consultation_id;


    public TimetableString(){

    }


    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsultation_id() {
        return consultation_id;
    }

    public void setConsultation_id(String consultation_id) {
        this.consultation_id = consultation_id;
    }


    //Método para convertir TimetableString a Timetable
    public Timetable convertToTimetable() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        Timetable timetable = new Timetable();
        timetable.setDayOfWeek(this.dayOfWeek);
        timetable.setId(this.id);
        timetable.setConsultation_id(this.consultation_id);

        //Convertimos String a LocalTime
        timetable.setStartTime(LocalTime.parse(this.startTime, formatter));
        timetable.setEndTime(LocalTime.parse(this.endTime, formatter));

        return timetable;
    }
}

