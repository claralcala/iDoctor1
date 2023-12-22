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

public class ProfessionalViewEvaluationDetails extends AppCompatActivity {


    //Declaracion de componentnes
    Evaluation evaluation;

    TextView tvDescriptionDetails, tvExplorationDetails, tvTreatmentDetails, tvDateDetails;

    Button btnCancelDetails, btnViewInform;

    String eval;

    EvaluationService evaluationService;

    String evaluationId;

    /**
     * @author Manu Rguez
     * Pantalla para vissualizar los Detalles de Evaluacion
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_view_evaluation_details);

        //Variables de sesión
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

        //Declaracion de componentnes
        btnCancelDetails = findViewById(R.id.btnCancelDetails);
        btnViewInform = findViewById(R.id.btnViewInform);

        //Recuperamos datos del intent
        Intent intent = getIntent();

        evaluation = new Evaluation();
        if (intent != null) {
            evaluation = (Evaluation) intent.getSerializableExtra("evaluation");
        }
        //Asignamos a evaluationId la id de la evaluacion para posteriormente pasarlo por parametro
        evaluationId = evaluation.getId();

        //Le asignamos a los componentes su respectivo campo
        tvDescriptionDetails = findViewById(R.id.tvDescriptionDetails);
        tvExplorationDetails = findViewById(R.id.tvExplorationDetails);
        tvTreatmentDetails = findViewById(R.id.tvTreatmentDetails);
        tvDateDetails = findViewById(R.id.tvDateDetails);

        //Llamamos al service
        evaluationService = new EvaluationService(getApplicationContext());

        evaluation = (Evaluation) intent.getSerializableExtra("evaluation");

        //Guardamos en los componentes la informacion que le pertenece
        tvDescriptionDetails.setText(evaluation.getDescription());
        tvExplorationDetails.setText(evaluation.getExploration());
        tvTreatmentDetails.setText(evaluation.getTreatment());

        //comprobamos la fecha y la hora no es nulla y formateamos  y lo añadimos a su campo
        if (evaluation.getEvaluationDateTime() != null) {
            LocalDateTime dateTime = evaluation.getEvaluationDateTime();

           //Formateamos fecha y hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDateTime = dateTime.format(formatter);

            tvDateDetails.setText(formattedDateTime);
        }

        //Le damos funcionalidad al boton de cancelar para que cuando se haga click se dirija a la Main Activity de Profesional
        btnCancelDetails.setOnClickListener(v -> {
            Intent back = new Intent(this, ProfessionalMainActivity.class);

            startActivity(back);

        });

        //Le damos funcion al boton de ver informe y nos llevamos en el intent el objeto de evaluacion
        btnViewInform.setOnClickListener(v -> {
            Intent inform = new Intent(this, ProfessionalViewReport.class);
            inform.putExtra("evaluation",evaluation);
            startActivity(inform);
        });




    }
}







