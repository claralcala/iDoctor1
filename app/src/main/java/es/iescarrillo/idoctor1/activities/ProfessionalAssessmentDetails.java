package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Assessment;
import es.iescarrillo.idoctor1.services.AssessmentService;

public class ProfessionalAssessmentDetails extends AppCompatActivity {


    Assessment assessment;

    TextView tvUsername, tvTitle, tvDescription,tvStars,tvDateDetails;

    Button btnCancelDetails;

    AssessmentService assessmentService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_assessment_details);
        //Variables de sesión
        SharedPreferences sharedPreferences = getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");


        if (!role.equals("PROFESSIONAL")) {


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        btnCancelDetails = findViewById(R.id.btnCancelDetails);

        Intent intent = getIntent();

        assessment = new Assessment();
        if(intent != null){
            assessment = (Assessment) intent.getSerializableExtra("assessment");
        }

        assessmentService = new AssessmentService(getApplicationContext());

        assessment = (Assessment) intent.getSerializableExtra("assessment");
        tvUsername = findViewById(R.id.tvUsername);
        tvDescription = findViewById(R.id.tvDescription);
        tvTitle = findViewById(R.id.tvTitle);
        tvStars = findViewById(R.id.tvStarts);
        tvDateDetails = findViewById(R.id.tvDateDetails);



        tvUsername.setText(assessment.getUsername().toString());
        tvDescription.setText(assessment.getDescription().toString());
        tvTitle.setText(assessment.getTitle().toString());
        tvStars.setText(assessment.getStars().toString());

        if (assessment.getAssessmentDateTime() != null){
            LocalDateTime dateTime  = assessment.getAssessmentDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDateTime = dateTime.format(formatter);

            tvDateDetails.setText(formattedDateTime);
        }

        btnCancelDetails.setOnClickListener( v -> {
            Intent intentback = new Intent(this, ProfessionalMainActivity.class);
            startActivity(intentback);

        });


    }
}