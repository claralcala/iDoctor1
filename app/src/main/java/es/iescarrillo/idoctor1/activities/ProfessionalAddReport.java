package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Evaluation;
import es.iescarrillo.idoctor1.models.Report;
import es.iescarrillo.idoctor1.services.ReportService;

public class ProfessionalAddReport extends AppCompatActivity {


    //Declaracion de los componentes
    Report report;

    ReportService reportService;
    EditText etAddTitle, etAddLink;

    Button btnCancelAddReport, btnAddReport;
    Evaluation evaluation;
    String evaluationId;

    /**
     * @author Manu Rguez
     * Pantalla para Visualizar el informe
     */


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

        //Comprobacion de roles
        if(!role.equals("PROFESSIONAL")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }
        //Inicializacion de componentes
        etAddTitle = findViewById(R.id.etAddTitle);
        etAddLink = findViewById(R.id.etAddLink);

        btnAddReport  = findViewById(R.id.btnAddReport);
        btnCancelAddReport = findViewById(R.id.btnCancelAddReport);


        //Declaracion del service y recuperacion de datos del intent
        reportService  =new ReportService(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            evaluation = (Evaluation) intent.getSerializableExtra("evaluation");
        }

        //Asignamos un Id a una variable
        evaluationId = evaluation.getId();
        Log.d("ProfessionalViewReport", "Evaluation ID" + evaluationId);


        //añadimos el report a la base de datos
        btnAddReport.setOnClickListener(v -> {

            report  =new Report();
            String title = etAddTitle.getText().toString();
            String link = etAddLink.getText().toString();




                report.setEvaluation_id(evaluationId);
                report.setTitle(title);
                report.setLink(link);

                // Insertar el informe
                reportService.insertReport(report);

                Toast.makeText(ProfessionalAddReport.this, "Informe insertado correctamente.", Toast.LENGTH_SHORT).show();


            Intent inserInfor = new Intent(this,ProfessionalMainActivity.class);
            startActivity(inserInfor);
        });




    }
}