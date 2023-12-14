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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.AppointmentString;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.services.AppointmentService;


public class ProfessionalEditAppointment extends AppCompatActivity {

    Appointment app;

    AppointmentString appString;



    AppointmentService appService;

    String consultationID;

    EditText etDate, etHour;

    CheckBox cbActive;

    Spinner spPatient;

    Button btnSave, btnBack;

    List<Patient> patients;

    String patientId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_edit_appointment);

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

        Intent intent = getIntent();

        app= new Appointment();
        if (intent != null) {
            app = (Appointment) intent.getSerializableExtra("appointment");
        }


        etDate=findViewById(R.id.etDate);
        etHour=findViewById(R.id.etTime);
        cbActive=findViewById(R.id.checkBoxActive);
        spPatient=findViewById(R.id.spPatient);

        btnSave=findViewById(R.id.btnSaveAppo);
        btnBack=findViewById(R.id.btnCancel);

        appService = new AppointmentService(getApplicationContext());


        patients = new ArrayList<>();


        DatabaseReference dbPatients = FirebaseDatabase.getInstance().getReference().child("patient");


        dbPatients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                patients.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto
                    Patient p = snapshot.getValue(Patient.class);
                    patients.add(p);
                }

                //Creamos otro arraylist para meter los nombres de usuario
                ArrayList<String>patientUsername= new ArrayList<String>();
                for (Patient pa : patients){
                    String tName=pa.getUsername();
                    patientUsername.add(tName);
                }

                //Adaptador para el spinner
                ArrayAdapter tAdapter = new ArrayAdapter(ProfessionalEditAppointment.this, android.R.layout.simple_spinner_dropdown_item, patientUsername);
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

        etDate.setText(app.getAppointmentDate().toString());
        etHour.setText(app.getAppointmentTime().toString());
        if(app.isActive()){
            cbActive.setChecked(true);
        }else {
            cbActive.setChecked(false);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");

        btnSave.setOnClickListener(v -> {



            app.setPatient_id(patientId);
            app.setAppointmentDate(LocalDate.parse(etDate.getText().toString(), formatter));
            app.setAppointmentTime(LocalTime.parse(etHour.getText().toString(), formatterHour));
            if (cbActive.isChecked()){
                app.setActive(true);
            }else {
                app.setActive(false);
            }

            AppointmentString appString = new AppointmentString();
            appString=app.convertToAppointmentString();

            appService.updateAppointmentString(appString);
            Intent back = new Intent(this, ProfessionalMainActivity.class);
            startActivity(back);

        });



        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });


    }
}