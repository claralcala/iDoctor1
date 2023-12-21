package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.AssessmentAdapter;
import es.iescarrillo.idoctor1.models.Assessment;
import es.iescarrillo.idoctor1.models.AssessmentString;
import es.iescarrillo.idoctor1.services.AssessmentService;

public class ProfessionalViewAssessment extends AppCompatActivity {

    ListView lvAssessment;

    ArrayList<Assessment> assessmentArrayList;

    AssessmentService assessmentService;

    Assessment assessment;

    AssessmentAdapter adapter;

    AssessmentString assessmentString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_view_assessment);

//Variables de sesión
        SharedPreferences sharedPreferences = getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");

        DatabaseReference dbSuperheros = FirebaseDatabase.getInstance().getReference().child("evaluation");

        if (!role.equals("PROFESSIONAL")) {

            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);


        }
        lvAssessment = findViewById(R.id.lvAssessment);

        assessmentService = new AssessmentService(getApplicationContext());

        assessmentArrayList = new ArrayList<>();

        adapter = new AssessmentAdapter(getApplicationContext(), assessmentArrayList);

        assessmentService.getAssessmentsByProfessionalID(id_, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assessmentArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    assessmentString = snapshot.getValue(AssessmentString.class);

                    assessment = assessmentString.convertToAssessment();
                    assessmentArrayList.add(assessment);
                }

                lvAssessment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvAssessment.setOnItemClickListener((parent, view, position, id) -> {
            assessment = (Assessment) parent.getItemAtPosition(position);

            Intent IntentAssessmentDetail = new Intent(this, ProfessionalAssessmentDetails.class);
            IntentAssessmentDetail.putExtra("assessment", assessment);
            startActivity(IntentAssessmentDetail);
        });


    }
}
