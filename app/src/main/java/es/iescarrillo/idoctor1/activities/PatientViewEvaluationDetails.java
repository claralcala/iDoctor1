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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Evaluation;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.EvaluationService;
import es.iescarrillo.idoctor1.services.ProfessionalService;


public class PatientViewEvaluationDetails extends AppCompatActivity {
    Evaluation evaluation;

    TextView tvDescriptionDetailsEvaluation, tvExplorationDetailsEvaluation, tvTreatmentDetailsEvaluation, tvDateDetailsEvaluation;

    Button btnCancelDetailsEvaluation, btnViewInformPatient;

    String eval;

    EvaluationService evaluationService;

    String evaluationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_evaluation_details);
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
        btnCancelDetailsEvaluation = findViewById(R.id.btnCancelDetailsEvaluation);
        btnViewInformPatient = findViewById(R.id.btnViewInformPatient);

        Intent intent = getIntent();

        evaluation = new Evaluation();
        if (intent != null) {
            evaluation = (Evaluation) intent.getSerializableExtra("evaluation");
        }

        evaluationId = evaluation.getId();

        tvDescriptionDetailsEvaluation = findViewById(R.id.tvDescriptionDetailsEvaluation);
        tvExplorationDetailsEvaluation = findViewById(R.id.tvExplorationDetailsEvaluation);
        tvTreatmentDetailsEvaluation = findViewById(R.id.tvTreatmentDetailsEvaluation);
        tvDateDetailsEvaluation = findViewById(R.id.tvDateDetailsEvaluation);

        evaluationService = new EvaluationService(getApplicationContext());

        evaluation = (Evaluation) intent.getSerializableExtra("evaluation");


        tvDescriptionDetailsEvaluation.setText(evaluation.getDescription());
        tvExplorationDetailsEvaluation.setText(evaluation.getExploration());
        tvTreatmentDetailsEvaluation.setText(evaluation.getTreatment());

        if (evaluation.getEvaluationDateTime() != null) {
            LocalDateTime dateTime = evaluation.getEvaluationDateTime();

            //Formateamos fecha y hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDateTime = dateTime.format(formatter);

            tvDateDetailsEvaluation.setText(formattedDateTime);
        }
        btnCancelDetailsEvaluation.setOnClickListener(v -> {
            Intent back = new Intent(this, Patient_Main_Activity.class);
            startActivity(back);

        });
        btnViewInformPatient.setOnClickListener(v -> {
            Intent inform = new Intent(this, PatientViewReport.class);
            inform.putExtra("evaluation",evaluation);
            startActivity(inform);
        });
    }
}