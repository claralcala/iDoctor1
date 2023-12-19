package es.iescarrillo.idoctor1.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@IgnoreExtraProperties
public class EvaluationString implements Serializable {

    private String id;

    private String description;
    private String exploration;

    private String treatment;

    private String evaluationDateTime;

    private String appointment_id;

    public EvaluationString(){

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

    public String getEvaluationDateTime() {
        return evaluationDateTime;
    }

    public void setEvaluationDateTime(String evaluationDateTime) {
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

    public Evaluation convertToEvaluation() {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        Evaluation evaluation = new Evaluation();
        evaluation.setId(this.id);
        evaluation.setDescription(this.description);
        evaluation.setExploration(this.exploration);
        evaluation.setTreatment(this.treatment);
        evaluation.setEvaluationDateTime(LocalDateTime.parse(this.evaluationDateTime, formatter));
        evaluation.setAppointment_id(this.appointment_id);

        return evaluation;
    }
}
