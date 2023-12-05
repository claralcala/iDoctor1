package es.iescarrillo.idoctor1.services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.iescarrillo.idoctor1.models.Patient;

public class PatientService {

    private DatabaseReference database;

    public PatientService(Context context){

        database= FirebaseDatabase.getInstance().getReference().child("patient");



    }


    public void insertPatient(Patient patient) {
        // Utiliza push() para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        patient.setId(newReference.getKey()); // Asigna el ID generado automáticamente

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(patient);
    }

    public void updatePatient(Patient patient) {
        database.child(patient.getId()).setValue(patient);
    }

    public void deletePatient(String id) {
        database.child(id).removeValue();
    }

    public void deletePatient(Patient patient){
        database.child(patient.getId()).removeValue();
    }
    
}
