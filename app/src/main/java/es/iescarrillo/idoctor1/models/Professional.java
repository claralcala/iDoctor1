package es.iescarrillo.idoctor1.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Professional extends Person implements Serializable {

    private String collegiateNumber;


}
