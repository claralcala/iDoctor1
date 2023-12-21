package es.iescarrillo.idoctor1.services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.AppointmentString;

public class AppointmentService {
    private DatabaseReference database;

    public AppointmentService(Context context){
        database = FirebaseDatabase.getInstance().getReference().child("appointment");
    }

    public void insertAppointment (Appointment appointment){
        //Utiliza push () para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        appointment.setId(newReference.getKey()); // Asigna el ID generado automáticamente

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(appointment);
    }

    public void insertAppointmentString (AppointmentString appointment){
        //Utiliza push () para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        appointment.setId(newReference.getKey()); // Asigna el ID generado automáticamente

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(appointment);
    }

    public void updateAppointment (Appointment appointment){
        database.child(appointment.getId()).setValue(appointment);
    }

    public void updateAppointmentString (AppointmentString appointment){
        database.child(appointment.getId()).setValue(appointment);
    }

    public void deleteAppointment ( String id){
        database.child(id).removeValue();
    }

    public void deleteAppointment (Appointment appointment){
        database.child(appointment.getId()).removeValue();
    }


    public void getAppointmentsByConsultation(String id, ValueEventListener listener){
        Query query = database.orderByChild("consultation_id").equalTo(id);
        query.addValueEventListener(listener);
    }

    public void getAppointmentsByPatientID(String id, ValueEventListener listener){
        Query query = database.orderByChild("patient_id").equalTo(id);
        query.addValueEventListener(listener);
    }

    public void getListEvaluation(String Id , ValueEventListener listener) {
        Query query = database.orderByChild("appointment_id").equalTo(Id);
        query.addValueEventListener(listener);
    }


}
