package es.iescarrillo.idoctor1.services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.iescarrillo.idoctor1.models.Assessment;
import es.iescarrillo.idoctor1.models.AssessmentString;
import es.iescarrillo.idoctor1.models.Patient;

public class AssessmentService {

    private DatabaseReference database;

    public AssessmentService(Context context){

        database= FirebaseDatabase.getInstance().getReference().child("assessment");



    }


    public void insertAssessment(Assessment assessment) {
        // Utiliza push() para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        assessment.setId(newReference.getKey()); // Asigna el ID generado automáticamente

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(assessment);
    }

    public void updateAssessment(Assessment assessment) {
        database.child(assessment.getId()).setValue(assessment);
    }

    public void deleteAssessment(String id) {
        database.child(id).removeValue();
    }

    public void deleteAssessment(Assessment assessment){
        database.child(assessment.getId()).removeValue();
    }
    public void getAssessmentsByProfessionalID(String id, ValueEventListener listener){
        Query query = database.orderByChild("professional_id").equalTo(id);
        query.addValueEventListener(listener);
    }
    public void insertAssessmentString(AssessmentString assessment) {
        // Utiliza push() para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        assessment.setId(newReference.getKey()); // Asigna el ID generado automáticamente

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(assessment);
    }


}
