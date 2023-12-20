package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.EvaluationAdapter;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Evaluation;
import es.iescarrillo.idoctor1.models.EvaluationString;
import es.iescarrillo.idoctor1.services.EvaluationService;

public class ProfessionalViewEvaluation extends AppCompatActivity {

    Button btnAddEvaluation;

    ListView lvEvaluation;

    ArrayList<Evaluation> evaluationArrayList;

    EvaluationService evaluationService;

    Evaluation evaluation;

    EvaluationAdapter adapter;

    Appointment appointment;

    EvaluationString evString;

    String appId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_view_evaluation);


        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");

        DatabaseReference dbSuperheros = FirebaseDatabase.getInstance().getReference().child("evaluation");

        if(!role.equals("PROFESSIONAL")){

            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);


        }

        Intent  intent1 = getIntent();
        appointment = new Appointment();
        if (intent1 != null) {
            appointment = (Appointment) intent1.getSerializableExtra("appointment");
        }

        lvEvaluation=findViewById(R.id.lvEvaluation);

        appId = appointment.getId();
        evaluationService  = new EvaluationService(getApplicationContext());


        evaluationArrayList = new ArrayList<>();
        adapter = new EvaluationAdapter(getApplicationContext(),evaluationArrayList);





        evaluationService.getListEvaluation(appId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                evaluationArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    evString = snapshot.getValue(EvaluationString.class);

                   evaluation = evString.convertToEvaluation();
                    evaluationArrayList.add(evaluation);
                }

                lvEvaluation.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAddEvaluation = findViewById(R.id.btnAddEvaluation);

        btnAddEvaluation.setOnClickListener( v -> {
            Intent add = new Intent(this, ProfessionalAddEvaluation.class);
            add.putExtra("appointment", appointment);
            startActivity(add);
        });

        lvEvaluation.setOnItemClickListener((parent, view, position, id) -> {
        evaluation = (Evaluation)  parent.getItemAtPosition(position);

        Intent IntentEvaluationDetails = new Intent(this, ProfessionalViewEvaluationDetails.class);
            IntentEvaluationDetails.putExtra("evaluation",evaluation);
        startActivity(IntentEvaluationDetails);
        });

    }
}