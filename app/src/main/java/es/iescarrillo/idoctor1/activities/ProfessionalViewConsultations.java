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
import es.iescarrillo.idoctor1.adapters.ConsultationAdapter;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.services.ConsultationService;

public class ProfessionalViewConsultations extends AppCompatActivity {


    Button btnAdd, btnBack;

    ListView lvConsultations;

    ArrayList<Consultation> consultations;

    ConsultationService consService;

    Consultation cons;

    ConsultationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_view_consultations);

        btnAdd=findViewById(R.id.btnAddConsultation);
        btnBack=findViewById(R.id.btnBack);
        lvConsultations=findViewById(R.id.lvConsultations);

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

        DatabaseReference dbDoctor = FirebaseDatabase.getInstance().getReference().child("consultation");


        consService= new ConsultationService(getApplicationContext());

        consultations= new ArrayList<>();

        consService.getConsultationsByProfessionalID(id_, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    consultations.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto Superhero
                    cons = snapshot.getValue(Consultation.class);
                    consultations.add(cons);
                }

                // Una vez los datos añadidos a nuestra lista, se la pasamos al adaptador
                adapter = new ConsultationAdapter(getApplicationContext(), consultations);
                lvConsultations.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        lvConsultations.setOnItemClickListener((parent, view, position, id) -> {
            cons = (Consultation) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, ProfessionalConsultationDetails.class);
            intent.putExtra("consultation", cons);
            startActivity(intent);
        });


        btnAdd.setOnClickListener(v -> {
            Intent add = new Intent(this, ProfessionalAddConsultation.class);
            startActivity(add);
        });


        btnBack.setOnClickListener(v -> {
            Intent back = new Intent (this, ProfessionalMainActivity.class);
            startActivity(back);
        });



    }
}