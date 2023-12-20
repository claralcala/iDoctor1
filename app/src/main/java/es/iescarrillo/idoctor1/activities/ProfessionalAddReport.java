package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Report;
import es.iescarrillo.idoctor1.services.ReportService;

public class ProfessionalAddReport extends AppCompatActivity {

    Report report;

    ReportService reportService;
    String evaluationID;


    EditText etAddTitle, etAddLink;

    Button btnCancelAddReport, btnAddReport;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_add_report);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");

        if(!role.equals("PROFESSIONAL")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        etAddTitle = findViewById(R.id.etAddTitle);
        etAddLink = findViewById(R.id.etAddLink);

        btnAddReport  = findViewById(R.id.btnAddReport);
        btnCancelAddReport = findViewById(R.id.btnCancelAddReport);

        reportService  =new ReportService(getApplicationContext());

        Intent intent = getIntent();
        evaluationID = intent.getStringExtra("evaluation_id");

        btnAddReport.setOnClickListener(v -> {

            report  =new Report();
            String title = etAddTitle.getText().toString();
            String link = etAddLink.getText().toString();

            if (!title.isEmpty() && !link.isEmpty()) {

                Report report = new Report();
                report.setEvaluation_id(evaluationID);
                report.setTitle(title);
                report.setLink(link);

                // Insertar el informe
                reportService.insertReport(report);

                Toast.makeText(ProfessionalAddReport.this, "Informe insertado correctamente.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfessionalAddReport.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            }

            Intent inserInfor = new Intent(this,ProfessionalMainActivity.class);
            startActivity(inserInfor);
        });




    }
}