package es.iescarrillo.idoctor1.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@IgnoreExtraProperties
public class AssessmentString implements Serializable {


    private String id;
    private String username;

    private String title;

    private String description;

    private Double stars;

    private String assessmentDateTime;


    private String professional_id;


    public AssessmentString(){


    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getStars() {
        return stars;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    public String getAssessmentDateTime() {
        return assessmentDateTime;
    }

    public void setAssessmentDateTime(String assessmentDateTime) {
        this.assessmentDateTime = assessmentDateTime;
    }

    public String getProfessional_id() {
        return professional_id;
    }

    public void setProfessional_id(String professional_id) {
        this.professional_id = professional_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Assessment convertToAssessment() {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        Assessment assessment = new Assessment();
        assessment.setId(this.getId());
        assessment.setUsername(this.getUsername());
        assessment.setTitle(this.getTitle());
        assessment.setDescription(this.getDescription());
        assessment.setStars(this.getStars());
        assessment.setAssessmentDateTime(LocalDateTime.parse(this.getAssessmentDateTime(), formatter));
        assessment.setProfessional_id(this.getProfessional_id());

        return assessment;
    }
}

