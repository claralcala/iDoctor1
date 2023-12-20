package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Assessment;
import es.iescarrillo.idoctor1.models.AssessmentString;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.AssessmentService;
import es.iescarrillo.idoctor1.services.PatientService;

public class PatientInsertAssessment extends AppCompatActivity {
    TextView tvUserAssessment;
    EditText etTitleAssessment;
    EditText etAssessmentDescription;
    Spinner spStars;
    EditText etDateTimeAssessment;
    Button btnAddAssessment;
    Button btnBackToDetails;
    AssessmentService assessmentService;
    String professionalId;
    Professional professional;
    Assessment assessment;
    PatientService patientService;
    String patientUsername;
    Patient p;
    AssessmentString assessmentString;
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

        professionalId=professional.getId();
        tvUserAssessment=findViewById(R.id.tvUserAssessment);
        etTitleAssessment=findViewById(R.id.etTitleAssessment);
        etAssessmentDescription=findViewById(R.id.etAssessmentDescription);
        spStars=findViewById(R.id.spStars);

        etDateTimeAssessment=findViewById(R.id.etDateTimeAssessment);
        btnAddAssessment=findViewById(R.id.btnAddAssessment);
        btnBackToDetails=findViewById(R.id.btnBackToDetails);
        ArrayAdapter<CharSequence> adapterSpinnerRating= ArrayAdapter.createFromResource(this,R.array.starsValues, android.R.layout.simple_spinner_item);
        adapterSpinnerRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStars.setAdapter(adapterSpinnerRating);
        patientService.getPatientByID(id_, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto
                    p = snapshot.getValue(Patient.class);
                    patientUsername=p.getUsername();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnAddAssessment.setOnClickListener(v -> {
            String titleAssessment=etTitleAssessment.getText().toString();
            String assessmentDescription=etAssessmentDescription.getText().toString();
            Double stars= Double.valueOf(spStars.getSelectedItem().toString());
            String date=etDateTimeAssessment.getText().toString();
            // Convertir las cadenas a LocalDateTime (asumiendo un formato específico)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/YY HH:mm");
            // Establecer el LocalDateTime en tu entidad Evaluation
            assessment=new Assessment();
            assessment.setDescription(assessmentDescription);
            assessment.setTitle(titleAssessment);
            assessment.setStars(stars);
            assessment.setAssessmentDateTime(LocalDateTime.parse(date, formatter));
            assessment.setProfessional_id(professionalId);
            assessment.setUsername(patientUsername);
            assessmentString=new AssessmentString();
            assessmentString=assessment.convertToAssessmentString();
            assessmentService.insertAssessmentString(assessmentString);
            Intent back = new Intent(this, PatientMainActivity.class);
            startActivity(back);
        });

    }
}