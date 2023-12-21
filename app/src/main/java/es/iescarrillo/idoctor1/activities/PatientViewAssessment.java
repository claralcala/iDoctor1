package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.AppointmentAdapter;
import es.iescarrillo.idoctor1.adapters.AssessmentAdapter;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.AppointmentString;
import es.iescarrillo.idoctor1.models.Assessment;
import es.iescarrillo.idoctor1.models.AssessmentString;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.AssessmentService;

public class PatientViewAssessment extends AppCompatActivity {
    ListView lvAssessment;
    Button btnBackToPatientProfile;
    AssessmentAdapter assessmentAdapter;
    AssessmentService assessmentService;
    Professional professional;
    Assessment assessment;
    AssessmentString assessmentString;
    String professionalId;
    ArrayList <Assessment> assessmentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_assessment);
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
        assessmentsList=new ArrayList<Assessment>();
        Intent intent=getIntent();

        professional = new Professional();
        if (intent != null) {
            professional = (Professional) intent.getSerializableExtra("professional");
        }
        professionalId=professional.getId();
        lvAssessment=findViewById(R.id.lvAssessment);
        assessmentsList=new ArrayList<Assessment>();
        assessmentService=new AssessmentService(getApplicationContext());
        DatabaseReference dbAssessmentProfessional= FirebaseDatabase.getInstance().getReference().child("assessment");
        assessmentService.getAssessmentsByProfessionalID(professionalId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assessmentsList.clear();
                for (DataSnapshot data:snapshot.getChildren()){
                    assessmentString=data.getValue(AssessmentString.class);
                    assessment=assessmentString.convertToAssessment();
                    assessmentsList.add(assessment);
                }
                for (Assessment assessment : assessmentsList) {
                    Log.d("AssessmentData", "Username: " + assessment.getUsername() + ", Title: " + assessment.getTitle());
                }
                assessmentAdapter=new AssessmentAdapter(getApplicationContext(),assessmentsList);
                lvAssessment.setAdapter(assessmentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        lvAssessment.setOnItemClickListener((parent, view, position, id) -> {
            assessment=(Assessment) parent.getItemAtPosition(position);
            Intent patientAssessmentDetails=new Intent(this, PatientAssessmentDetails.class);
            patientAssessmentDetails.putExtra("assessment",assessment);
            startActivity(patientAssessmentDetails);
        });
        btnBackToPatientProfile=findViewById(R.id.btnBackToPatientProfile);
        btnBackToPatientProfile.setOnClickListener(v -> {
            Intent backToPatientProfile=new Intent(this, Patient_Main_Activity.class);
            backToPatientProfile.putExtra("professional",professional);
            startActivity(backToPatientProfile);
        });

    }
}