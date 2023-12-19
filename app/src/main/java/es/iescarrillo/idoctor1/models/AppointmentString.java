package es.iescarrillo.idoctor1.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@IgnoreExtraProperties
public class AppointmentString implements Serializable {

    private String appointmentDate;

    private String appointmentTime;

    private boolean active;

    private String id;

    private String patient_id;

    private String consultation_id;


    public AppointmentString(){

    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getConsultation_id() {
        return consultation_id;
    }

    public void setConsultation_id(String consultation_id) {
        this.consultation_id = consultation_id;
    }

    public Appointment convertToAppointment() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.parse(this.appointmentDate, dateFormatter));
        appointment.setAppointmentTime(LocalTime.parse(this.appointmentTime, timeFormatter));
        appointment.setActive(this.active);
        appointment.setId(this.id);
        appointment.setPatient_id(this.patient_id);
        appointment.setConsultation_id(this.consultation_id);

        return appointment;
    }
}