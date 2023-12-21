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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.ConsultationAdapter;
import es.iescarrillo.idoctor1.adapters.ProfessionalAdapter;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.ConsultationService;
import es.iescarrillo.idoctor1.services.ProfessionalService;

public class Patient_View_Consultation extends AppCompatActivity {

    ListView lvConsultation;
    Button btnBack;
    Professional prof;
    ArrayList<Consultation>consultations;
    ConsultationService consultationService;
    ConsultationAdapter adapter;
    Consultation consultation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_consultation);

        //Variables de sesión
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

        lvConsultation = findViewById(R.id.lvConsultations);
        btnBack = findViewById(R.id.btnBack);

        Intent intent=getIntent();

        prof = new Professional();
        if (intent != null) {
            prof = (Professional) intent.getSerializableExtra("professional");
        }

        consultationService= new ConsultationService(getApplicationContext());

        consultations= new ArrayList<>();

        consultationService.getConsultationsByProfessionalID(prof.getId(), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                consultations.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto Superhero
                    consultation = snapshot.getValue(Consultation.class);
                    consultations.add(consultation);
                }

                // Una vez los datos añadidos a nuestra lista, se la pasamos al adaptador
                adapter = new ConsultationAdapter(getApplicationContext(), consultations);
                lvConsultation.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        lvConsultation.setOnItemClickListener((parent, view, position, id) -> {
            consultation = (Consultation) parent.getItemAtPosition(position);
            Intent appointment = new Intent(this, Patient_View_Appointment.class);
            appointment.putExtra("consultation", consultation);
            startActivity(appointment);
        });

        btnBack.setOnClickListener(v -> {
            Intent back = new Intent(this, Patient_Main_Activity.class);
            startActivity(back);
        });

    }
}