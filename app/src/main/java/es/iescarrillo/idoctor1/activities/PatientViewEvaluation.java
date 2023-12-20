package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.EvaluationAdapter;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Evaluation;
import es.iescarrillo.idoctor1.services.EvaluationService;

public class PatientViewEvaluation extends AppCompatActivity {
    EvaluationService evaluationService;
    Appointment appointment;
    Evaluation evaluation;
    String appointment_id;
    ArrayList<Evaluation> evaluations;
    EvaluationAdapter evaluationAdapter;
    ListView lvPatientEvaluation;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_evaluation);
        evaluations=new ArrayList<Evaluation>();
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");
        if(!role.equals("PATIENT")){
            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);
        }
        Intent intent=getIntent();
        if (intent!=null){
            appointment=(Appointment)intent.getSerializableExtra("appointment");
        }

        DatabaseReference dbAppointmentPatient= FirebaseDatabase.getInstance().getReference().child("evaluation");
        appointment_id=evaluation.getAppointment_id();
        evaluationService=new EvaluationService(getApplicationContext());

        evaluationService.getEvaluationByAppointmentID(appointment_id, new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}