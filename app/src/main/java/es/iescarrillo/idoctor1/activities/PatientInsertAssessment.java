package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Assessment;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.AssessmentService;

public class PatientInsertAssessment extends AppCompatActivity {
    EditText etUserAssessment;
    EditText etTitleAssessment;
    EditText etAssessmentDescription;
    Spinner spStars;
    Button btnAddAssessment;
    Button btnBackToDetails;
    AssessmentService assessmentService;
    String professional_id;
    Professional professional;
    Assessment assessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_insert_assessment);
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
        assessmentService=new AssessmentService(getApplicationContext());

        if (intent!=null){
            professional=(Professional) intent.getSerializableExtra("professional");
        }
        professional_id=assessment.getProfessional_id();
º       etUserAssessment=findViewById(R.id.etUserAssessment);
        etTitleAssessment=findViewById(R.id.etTitleAssessment);
        etAssessmentDescription=findViewById(R.id.etAssessmentDescription);
        spStars=findViewById(R.id.spStars);
    }
}