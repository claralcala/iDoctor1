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

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.EvaluationAdapter;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Evaluation;
import es.iescarrillo.idoctor1.models.EvaluationString;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.EvaluationService;
public class PatientViewEvaluation extends AppCompatActivity {
    Button btnBackMainPatient;
    ListView lvPatientEvaluation;

    ArrayList<Evaluation> evaluationArrayList;

    AppointmentService appointmentService;

    Evaluation evaluation;

    EvaluationAdapter adapter;

    Appointment appointment;

    EvaluationString evString;

    String appId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_evaluation);
        //Variables de sesión
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
        Intent  intent1 = getIntent();
        appointment = new Appointment();
        if (intent1 != null) {
            appointment = (Appointment) intent1.getSerializableExtra("appointment");
        }

        lvPatientEvaluation=findViewById(R.id.lvPatientEvaluation);

        appId = appointment.getId();
        appointmentService  = new AppointmentService(getApplicationContext());
        evaluationArrayList = new ArrayList<>();
        adapter = new EvaluationAdapter(getApplicationContext(),evaluationArrayList);
        appointmentService.getListEvaluation(appId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                evaluationArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    evString = snapshot.getValue(EvaluationString.class);

                    evaluation = evString.convertToEvaluation();
                    evaluationArrayList.add(evaluation);
                }
                lvPatientEvaluation.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        lvPatientEvaluation.setOnItemClickListener((parent, view, position, id) -> {
            evaluation = (Evaluation)  parent.getItemAtPosition(position);

            Intent IntentEvaluationDetails = new Intent(this, PatientViewEvaluationDetails.class);
            IntentEvaluationDetails.putExtra("evaluation",evaluation);
            startActivity(IntentEvaluationDetails);
        });
        btnBackMainPatient.findViewById(R.id.btnBackPatientMain);
        btnBackMainPatient.setOnClickListener(v -> {
            Intent BackToPatientActivity=new Intent(this, Patient_Main_Activity.class);
            startActivity(BackToPatientActivity);
        });
    }
}