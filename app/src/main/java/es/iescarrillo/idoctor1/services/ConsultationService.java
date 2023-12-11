package es.iescarrillo.idoctor1.services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.iescarrillo.idoctor1.models.Consultation;

public class ConsultationService {


    private DatabaseReference database;

    public ConsultationService(Context context){
        database = FirebaseDatabase.getInstance().getReference().child("consultation");
    }

    public void insertConsultation (Consultation consultation){
        //Utiliza push () para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        consultation.setId(newReference.getKey()); // Asigna el ID generado automáticamente

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(consultation);
    }

    public void updateConsultation (Consultation consultation){
        database.child(consultation.getId()).setValue(consultation);
    }

    public void deleteConsultation( String id){
        database.child(id).removeValue();
    }

    public void deleteConsultation (Consultation consultation){
        database.child(consultation.getId()).removeValue();
    }


}