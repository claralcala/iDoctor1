package es.iescarrillo.idoctor1.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serial;
import java.io.Serializable;

@IgnoreExtraProperties
public class Professional extends Person implements Serializable {

    private String collegiateNumber;
    private String speciality;

    private String description;

    private double stars;

    private Integer assessments;


    public Professional(){

    }

    public String getCollegiateNumber() {
        return collegiateNumber;
    }

    public void setCollegiateNumber(String collegiateNumber) {
        this.collegiateNumber = collegiateNumber;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public Integer getAssessments() {
        return assessments;
    }

    public void setAssessments(Integer assessments) {
        this.assessments = assessments;
    }


}
