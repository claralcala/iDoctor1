package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Assessment;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.AssessmentService;
import es.iescarrillo.idoctor1.services.ConsultationService;

public class PatientAssessmentDetails extends AppCompatActivity {
    TextView tvUsernameAssessmentDetails;
    TextView tvTitleAssessmentDetails;
    TextView tvDescriptionAssessmentDetails;
    TextView tvAssessmentRatingDetails;
    TextView tvAssessmentDateTimeDetails;
    Button btnBackToViewAssessment;
    AssessmentService assessmentService;
    Professional professional;
    Patient patient;
    Assessment assessment;
    String professionalId;
    String UsernameDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_assessment_details);
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
        tvUsernameAssessmentDetails=findViewById(R.id.tvUsernameAssessmentDetails);
        tvTitleAssessmentDetails=findViewById(R.id.tvTitleAssessmentDetails);
        tvDescriptionAssessmentDetails=findViewById(R.id.tvDescriptionAssessmentDetails);
        tvAssessmentRatingDetails=findViewById(R.id.tvAssessmentRatingDetails);
        tvAssessmentDateTimeDetails=findViewById(R.id.tvAssessmentDateTimeDetails);
        btnBackToViewAssessment=findViewById(R.id.btnBackToViewAssessment);
        assessmentService=new AssessmentService(getApplicationContext());
        Intent intent=getIntent();
        professional = new Professional();

        if (intent != null) {
            professional = (Professional) intent.getSerializableExtra("professional");
        }
        professionalId=professional.getId();
        assessmentService=new AssessmentService(getApplicationContext());
        assessmentService.getAssessmentsByProfessionalID(professionalId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assessment= snapshot.getValue(Assessment.class);
                UsernameDetails = patient.getUsername();
                tvUsernameAssessmentDetails.setText("Usuario" + UsernameDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tvTitleAssessmentDetails.setText("Titulo:  " +assessment.getTitle().toString());
        tvDescriptionAssessmentDetails.setText("Descripcion: " + assessment.getDescription().toString());
        tvAssessmentRatingDetails.setText("Estrellas: " + assessment.getStars().toString());
        tvAssessmentDateTimeDetails.setText("Fecha y hora: " + assessment.getAssessmentDateTime().toString());
        btnBackToViewAssessment.setOnClickListener(v -> {
            Intent BackToViewAssessment=new Intent(this, PatientViewAssessment.class);
            BackToViewAssessment.putExtra("professional",professional);
            startActivity(BackToViewAssessment);
        });
    }
}