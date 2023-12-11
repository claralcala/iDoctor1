package es.iescarrillo.idoctor1.services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.models.Report;

public class ReportService {

    private DatabaseReference database;

    public ReportService(Context context){

        database= FirebaseDatabase.getInstance().getReference().child("report");



    }


    public void insertReport(Report report) {
        // Utiliza push() para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        report.setId(newReference.getKey()); // Asigna el ID generado automáticamente

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(report);
    }

    public void updateReport(Report report) {
        database.child(report.getId()).setValue(report);
    }

    public void deleteReport(String id) {
        database.child(id).removeValue();
    }

    public void deleteReport(Report report){
        database.child(report.getId()).removeValue();
    }
}
