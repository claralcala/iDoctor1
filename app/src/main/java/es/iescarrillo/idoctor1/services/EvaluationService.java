package es.iescarrillo.idoctor1.services;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.iescarrillo.idoctor1.models.Evaluation;
import es.iescarrillo.idoctor1.models.Patient;

public class EvaluationService {

    private DatabaseReference database;

    public EvaluationService(Context context){

        database= FirebaseDatabase.getInstance().getReference().child("evaluation");



    }


    public void insertEvaluation(Evaluation evaluation) {
        // Utiliza push() para obtener una clave única y agregar el mensaje
        DatabaseReference newReference = database.push();
        evaluation.setId(newReference.getKey()); // Asigna el ID generado automáticamente

        // Ahora, utiliza setValue() en la nueva referencia para agregar el nuevo mensaje
        newReference.setValue(evaluation);
    }

    public void updateEvaluation(Evaluation evaluation) {
        database.child(evaluation.getId()).setValue(evaluation);
    }

    public void deleteEvaluation(String id) {
        database.child(id).removeValue();
    }

    public void deleteEvaluation(Evaluation evaluation){
        database.child(evaluation.getId()).removeValue();
    }
}
