package es.iescarrillo.idoctor1.services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.models.Professional;

public class ProfessionalService {

    private DatabaseReference database;

    public ProfessionalService(Context context){

        database= FirebaseDatabase.getInstance().getReference().child("professional");



    }


    public void insertProfessional(Professional prof) {
        // Utiliza push() para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        prof.setId(newReference.getKey()); // Asigna el ID generado automáticamente

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(prof);
    }

    public void updateProfessional(Professional prof) {
        database.child(prof.getId()).setValue(prof);
    }

    public void deleteProfessional(String id) {
        database.child(id).removeValue();
    }

    public void deleteProfessional(Professional prof){
        database.child(prof.getId()).removeValue();
    }

    public void getProfessionalByUsername(String username, ValueEventListener listener){
        Query query= database.orderByChild("username").equalTo(username);
        query.addValueEventListener(listener);
    }

    public void getProfessionalByID(String id, ValueEventListener listener){
        Query query = database.orderByChild("id").equalTo(id);
        query.addValueEventListener(listener);
    }
}
