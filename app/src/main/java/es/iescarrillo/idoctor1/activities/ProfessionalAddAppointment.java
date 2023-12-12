package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.PatientService;

public class ProfessionalAddAppointment extends AppCompatActivity {

    Appointment app;


    AppointmentService appService;

    String consultationID;

    EditText etDate, etHour;

    CheckBox cbActive;

    Spinner spPatient;

    Button btnAdd, btnBack;

    List<Patient> patients;

    String patientId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_add_appointment);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");

        etDate=findViewById(R.id.etDate);
        etHour=findViewById(R.id.etTime);
        cbActive=findViewById(R.id.checkBoxActive);
        spPatient=findViewById(R.id.spPatient);

        btnAdd=findViewById(R.id.btnSaveAppo);
        btnBack=findViewById(R.id.btnCancel);

        appService = new AppointmentService(getApplicationContext());

        patients = new ArrayList<>();


        DatabaseReference dbPatients = FirebaseDatabase.getInstance().getReference().child("patient");


        dbPatients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                patients.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto Superhero
                    Patient p = snapshot.getValue(Patient.class);
                    patients.add(p);
                }

                //Creamos otro arraylist para meter los nombres de usuario de los profesores
                ArrayList<String>patientUsername= new ArrayList<String>();
                for (Patient pa : patients){
                    String tName=pa.getUsername();
                    patientUsername.add(tName);
                }

                //Adaptador para el spinner
                ArrayAdapter tAdapter = new ArrayAdapter(ProfessionalAddAppointment.this, android.R.layout.simple_spinner_dropdown_item, patientUsername);
                spPatient.setAdapter(tAdapter);

                //Cuando seleccionamos un item
                spPatient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Metemos el id del profesor en la variable que hemos creado
                        patientId= patients.get(position).getId();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        btnAdd.setOnClickListener(v -> {
            app = new Appointment();

            app.setConsultation_id(consultationID);
            app.setPatient_id(patientId);
            if (cbActive.isChecked()){
                app.setActive(true);
            }else {
                app.setActive(false);
            }

            appService.insertAppointment(app);
        });



    btnBack.setOnClickListener(v -> {
        onBackPressed();
    });




    }
}