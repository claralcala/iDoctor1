package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.ProfessionalAdapter;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.ProfessionalService;

public class Patient_View_Professional extends AppCompatActivity {

    ListView lvProfessional;
    Spinner spFilter;
    EditText etFilter;
    Button btnFilter, btnBack;
    ArrayList<Professional> professionals;
    ArrayList<String> options;
    String option;
    ProfessionalAdapter adapter;
    Professional professional;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_professional);

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

        lvProfessional = findViewById(R.id.lvProfessional);
        spFilter = findViewById(R.id.spFilter);
        etFilter = findViewById(R.id.etFilter);
        btnFilter = findViewById(R.id.btnFilter);
        btnBack = findViewById(R.id.btnBack);
        professionals = new ArrayList<>();
        // Obtenemos la referencial al nodo "superheros"
        DatabaseReference dbDoctor = FirebaseDatabase.getInstance().getReference().child("professional");

        dbDoctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                professionals.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto Superhero
                    professional = snapshot.getValue(Professional.class);
                    professionals.add(professional);
                }

                // Una vez los datos añadidos a nuestra lista, se la pasamos al adaptador
                adapter = new ProfessionalAdapter(getApplicationContext(), professionals);
                lvProfessional.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        options = new ArrayList<>();
        options.add("Nombre");
        options.add("Ciudad");
        options.add("Especialidad");

        ArrayAdapter sAdapter= new ArrayAdapter(Patient_View_Professional.this, android.R.layout.simple_spinner_dropdown_item, options);
        spFilter.setAdapter(sAdapter);

        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                option=spFilter.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lvProfessional.setOnItemClickListener((parent, view, position, id) -> {
            professional = (Professional) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, Patient_View_Professional_Profile.class);
            intent.putExtra("professional", professional);
            startActivity(intent);
        });



    }
}