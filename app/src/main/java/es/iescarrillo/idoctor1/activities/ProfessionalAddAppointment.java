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
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.PatientService;

public class ProfessionalAddAppointment extends AppCompatActivity {

    Appointment app;


    AppointmentService appService;

    String consultationID;

    EditText etDate, etHour;

    CheckBox cbActive;



    Button btnAdd, btnBack;



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

        if(!role.equals("PROFESSIONAL")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }


        etDate=findViewById(R.id.etDate);
        etHour=findViewById(R.id.etTime);
        cbActive=findViewById(R.id.checkBoxActive);


        btnAdd=findViewById(R.id.btnSaveAppo);
        btnBack=findViewById(R.id.btnCancel);

        appService = new AppointmentService(getApplicationContext());

        Intent intent = getIntent();
        consultationID=intent.getStringExtra("consultation_id");




        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");

        btnAdd.setOnClickListener(v -> {
            app = new Appointment();

            app.setConsultation_id(consultationID);

            app.setAppointmentDate(LocalDate.parse(etDate.getText().toString(), formatter));
            app.setAppointmentTime(LocalTime.parse(etHour.getText().toString(), formatterHour));
            if (cbActive.isChecked()){
                app.setActive(true);
            }else {
                app.setActive(false);
            }

            AppointmentString appString = new AppointmentString();
            appString=app.convertToAppointmentString();

            appService.insertAppointmentString(appString);
            Intent back = new Intent(this, ProfessionalMainActivity.class);
            startActivity(back);

        });



        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });




    }
}