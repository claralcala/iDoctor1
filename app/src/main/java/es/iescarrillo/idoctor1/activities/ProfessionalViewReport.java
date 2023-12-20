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

public class ProfessionalViewReport extends AppCompatActivity {

    Button btnAdd, btnCancel;
    TextView tvTitle, tvLink;

    ReportService reportService;

    Report report;

    Evaluation evaluation;
    String evaluationId;

    ArrayList<Report> arrayListReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_view_report);

        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        tvTitle = findViewById(R.id.tvTitle);
        tvLink = findViewById(R.id.tvLink);

        // Variables de sesión
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

        DatabaseReference dbDoctor = FirebaseDatabase.getInstance().getReference().child("report");

        reportService = new ReportService(getApplicationContext());

        report = new Report();
        Intent intent = getIntent();

        evaluation = new Evaluation();
        if (intent != null) {
            evaluation = (Evaluation) intent.getSerializableExtra("evaluation");
        }

        evaluationId = evaluation.getId();

        arrayListReport = new ArrayList<>();

        // Obtener el informe por la ID de evaluación
        reportService.getReportByEvaluationID(evaluationId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListReport.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    report = snapshot.getValue(Report.class);
                    arrayListReport.add(report);
                }

                // Muestra los informes de alguna manera (por ejemplo, solo el primero)
                if (!arrayListReport.isEmpty()) {
                    Report firstReport = arrayListReport.get(0);
                    tvTitle.setText(firstReport.getTitle());
                    tvLink.setText(firstReport.getLink());
                }

                btnAdd.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de la base de datos
            }
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, ProfessionalAddReport.class);
            startActivity(intent2);
        });
    }
}
