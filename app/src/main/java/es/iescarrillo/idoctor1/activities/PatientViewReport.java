package es.iescarrillo.idoctor1.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Evaluation;
import es.iescarrillo.idoctor1.models.Report;
import es.iescarrillo.idoctor1.services.ReportService;
public class PatientViewReport extends AppCompatActivity {
    Button btnCancelReport;
    TextView tvTitleReport, tvLinkReport;

    ReportService reportService;

    Report report;

    Evaluation evaluation;
    String evaluationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_report);
        evaluationId = evaluation.getId();
        btnCancelReport = findViewById(R.id.btnCancelReport);
        tvTitleReport = findViewById(R.id.tvTitleReport);
        tvLinkReport = findViewById(R.id.tvLinkReport);
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
        DatabaseReference dbDoctor = FirebaseDatabase.getInstance().getReference().child("report");

        reportService = new ReportService(getApplicationContext());

        report = new Report();
        Intent intent = getIntent();

        evaluation = new Evaluation();
        if (intent != null) {
            evaluation = (Evaluation) intent.getSerializableExtra("evaluation");
        }
        evaluationId = evaluation.getId();
        reportService.getReportByEvaluationID(evaluationId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    report = snapshot.getValue(Report.class);
                    tvTitleReport.setText(report.getTitle().toString());
                    tvLinkReport.setText(report.getLink().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de la base de datos
            }
        });
        btnCancelReport.setOnClickListener(v -> {
            Intent back = new Intent(this, Patient_Main_Activity.class);
            startActivity(back);

        });
    }
}