package es.iescarrillo.idoctor1.models;


import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.time.LocalDateTime;

@IgnoreExtraProperties
public class Assessment implements Serializable {

    private String id;
    private String username;

    private String title;

    private String description;

    private Double stars;

    private LocalDateTime assessmentDateTime;


    private String professional_id;


    public Assessment(){


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

    public LocalDateTime getAssessmentDateTime() {
        return assessmentDateTime;
    }

    public void setAssessmentDateTime(LocalDateTime assessmentDateTime) {
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
}
