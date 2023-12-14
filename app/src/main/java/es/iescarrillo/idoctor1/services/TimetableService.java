package es.iescarrillo.idoctor1.services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.iescarrillo.idoctor1.models.Timetable;
import es.iescarrillo.idoctor1.models.TimetableString;

public class TimetableService {


        private DatabaseReference database;

        public TimetableService(Context context){
            database = FirebaseDatabase.getInstance().getReference().child("timetable");
        }

        public void insertTimetable (Timetable timetable){
            //Utiliza push () para obtener una clave única y agregar el mensaje
            DatabaseReference newReference = database.push();
            timetable.setId(newReference.getKey()); // Asigna el ID generado automáticamente

            // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
            newReference.setValue(timetable);
        }

        public void insertTimetableString (TimetableString timetableString){

            //Utiliza push () para obtener una clave única y agregar el mensaje
            DatabaseReference newReference = database.push();
            timetableString.setId(newReference.getKey()); // Asigna el ID generado automáticamente

            // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
            newReference.setValue(timetableString);

        }

        public void updateTimetable (Timetable timetable){
            database.child(timetable.getId()).setValue(timetable);
        }


    public void updateTimetableString (TimetableString timetable){
        database.child(timetable.getId()).setValue(timetable);
    }
        public void deleteTimetable ( String id){
            database.child(id).removeValue();
        }



        public void deleteTimetable (Timetable timetable){
            database.child(timetable.getId()).removeValue();
        }

        public void getTimetablesByConsultationID(String id, ValueEventListener listener){
        Query query = database.orderByChild("consultation_id").equalTo(id);
        query.addValueEventListener(listener);
    }


    }



