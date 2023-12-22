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


    //Declaracion de componentes
    Button btnAdd, btnCancel;
    TextView tvTitle, tvLink;

    ReportService reportService;

    Report report;

    Evaluation evaluation;
    String evaluationId;

    /**
     * @author Manu Rguez
     * Pantalla para Visualizar el informe
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_view_report);

        //Declaramos los componentes

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

        //Al entrar en la activity si el rol que tiene no es professional, lo saca hacia la MainActivity
        if (!role.equals("PROFESSIONAL")) {
            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);
        }

        DatabaseReference dbDoctor = FirebaseDatabase.getInstance().getReference().child("report");

        //Llamamos al servicio
        reportService = new ReportService(getApplicationContext());

        //Recuperamos datos del intent
        report = new Report();
        Intent intent = getIntent();

        evaluation = new Evaluation();
        if (intent != null) {
            evaluation = (Evaluation) intent.getSerializableExtra("evaluation");
        }

        //Asignamos a evaluationId la id de la evaluacion para posteriormente pasarlo por parametro
        evaluationId = evaluation.getId();



        // Obtener el informe por la ID de evaluación
        reportService.getReportByEvaluationID(evaluationId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    report = snapshot.getValue(Report.class);
                    tvTitle.setText(report.getTitle().toString());
                    tvLink.setText(report.getLink().toString());
                    btnAdd.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de la base de datos
            }
        });

        //Le damos funcionalidad al boton de añadir y pasamos en el intent el objeto evaluacion
        btnAdd.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, ProfessionalAddReport.class);
            intent2.putExtra("evaluation",evaluation);
            startActivity(intent2);
        });

        //Le damos funcionalidad al boton de cancelar para que cuando se haga click se dirija a la Main Activity de Profesional
        btnCancel.setOnClickListener(v -> {
            Intent back = new Intent(this, ProfessionalMainActivity.class);
            startActivity(back);

        });
    }

}
