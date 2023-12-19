package es.iescarrillo.idoctor1.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@IgnoreExtraProperties
public class Evaluation implements Serializable {


    private String id;

    private String description;
    private String exploration;

    private String treatment;

    private LocalDateTime evaluationDateTime;

    private String appointment_id;

    public Evaluation(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExploration() {
        return exploration;
    }

    public void setExploration(String exploration) {
        this.exploration = exploration;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public LocalDateTime getEvaluationDateTime() {
        return evaluationDateTime;
    }

    public void setEvaluationDateTime(LocalDateTime evaluationDateTime) {
        this.evaluationDateTime = evaluationDateTime;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public EvaluationString convertToEvaluationString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        EvaluationString evaluationString = new EvaluationString();
        evaluationString.setId(this.id);
        evaluationString.setDescription(this.description);
        evaluationString.setExploration(this.exploration);
        evaluationString.setTreatment(this.treatment);
        evaluationString.setEvaluationDateTime(this.evaluationDateTime.format(formatter));
        evaluationString.setAppointment_id(this.appointment_id);

        return evaluationString;
    }
}