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



    Button btnSave, btnBack;



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


        btnSave=findViewById(R.id.btnSaveAppo);
        btnBack=findViewById(R.id.btnCancel);

        appService = new AppointmentService(getApplicationContext());




        etDate.setText(app.getAppointmentDate().toString());
        etHour.setText(app.getAppointmentTime().toString());
        if(app.isActive()){
            cbActive.setChecked(true);
        }else {
            cbActive.setChecked(false);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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