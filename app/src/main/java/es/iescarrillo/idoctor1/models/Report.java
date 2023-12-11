package es.iescarrillo.idoctor1.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Report implements Serializable {


    private String id;

    private String title;

    private String link;

    private String evaluation_id;

    public Report(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEvaluation_id() {
        return evaluation_id;
    }

    public void setEvaluation_id(String evaluation_id) {
        this.evaluation_id = evaluation_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
