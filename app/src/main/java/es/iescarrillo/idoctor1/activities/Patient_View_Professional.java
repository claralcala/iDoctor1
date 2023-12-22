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
import java.util.Locale;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.ProfessionalAdapter;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.ConsultationService;
import es.iescarrillo.idoctor1.services.ProfessionalService;
/**
 * @author Jesús
 *
 * Pantalla para ver los profesionales
 */
public class Patient_View_Professional extends AppCompatActivity {

    ListView lvProfessional;
    Spinner spFilter;
    EditText etFilter;
    Button btnFilter, btnBack;
    ArrayList<Professional> professionals;
    ArrayList<String> options;
    ArrayList<String> profesionalID;
    ArrayList<Consultation> consultations;
    String option;
    ProfessionalAdapter adapter;
    ProfessionalService professionalService;
    Professional professional;
    ConsultationService consultationService;
    Consultation consultation;


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

        //Comprobamos que el rol del usuario sea paciente
        if(!role.equals("PATIENT")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        //Inicializamos los componentes
        lvProfessional = findViewById(R.id.lvProfessional);
        spFilter = findViewById(R.id.spFilter);
        etFilter = findViewById(R.id.etFilter);
        btnFilter = findViewById(R.id.btnFilter);
        btnBack = findViewById(R.id.btnBack);

        professionalService = new ProfessionalService(getApplicationContext());
        consultationService = new ConsultationService(getApplicationContext());

        professionals = new ArrayList<>();
        consultations = new ArrayList<>();
        profesionalID = new ArrayList<>();

        consultation = new Consultation();
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

        //Creamos un array y volvamos su contenido en el Spinner
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

        //en caso de darle a algun profesional de la lista nos llevara a ver su perfil
        lvProfessional.setOnItemClickListener((parent, view, position, id) -> {
            professional = (Professional) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, Patient_View_Professional_Profile.class);
            intent.putExtra("professional", professional);
            startActivity(intent);
        });

        //Boton para volver a la main activity
        btnBack.setOnClickListener(v -> {
            Intent back = new Intent(this, Patient_Main_Activity.class);
            startActivity(back);
        });

        //Boton para filtrar por nombre, especialidad o ciudad
        btnFilter.setOnClickListener(v -> {

            //En caso de en el Spinner seleccionemos nombre buscaremos los profesionales por nombre
            if(option.equalsIgnoreCase("nombre")){
                professionalService.getProfesionalByName(etFilter.getText().toString(), new ValueEventListener() {
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
                //En caso de en el Spinner seleccionemos Especialidad buscaremos los profesionales por Especialidad
            } else if (option.equalsIgnoreCase("Especialidad")) {
                professionalService.getProfesionalBySpeciality(etFilter.getText().toString(), new ValueEventListener() {
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
                //En caso de en el Spinner seleccionemos Ciudad buscaremos los profesionales por Ciudad
            } else if (option.equalsIgnoreCase("Ciudad")) {
                consultationService.getConsultationByCity(etFilter.getText().toString(), new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        consultations.clear();
                        professionals.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Convierte cada nodo de la base de datos a un objeto Superhero
                            consultation = snapshot.getValue(Consultation.class);
                            consultations.add(consultation);
                        }

                        for(Consultation cons: consultations){
                            professionalService.getProfessionalByID(cons.getProfessional_id(), new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                                        // Convierte cada nodo de la base de datos a un objeto Superhero
                                        professional = snapshot.getValue(Professional.class);

                                        if(!professionals.contains(professional)){
                                            professionals.add(professional);
                                        }

                                    }

                                    // Una vez los datos añadidos a nuestra lista, se la pasamos al adaptador
                                    adapter = new ProfessionalAdapter(getApplicationContext(), professionals);
                                    lvProfessional.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

    }
}