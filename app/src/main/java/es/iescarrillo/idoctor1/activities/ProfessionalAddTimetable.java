package es.iescarrillo.idoctor1.activities;

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
import android.widget.Spinner;


import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Timetable;
import es.iescarrillo.idoctor1.models.TimetableString;
import es.iescarrillo.idoctor1.services.TimetableService;

public class ProfessionalAddTimetable extends AppCompatActivity {

    Spinner spDay;
    EditText etStartHour, etEndHour;

    Button btnSave, btnCancel;

    String day;

    Timetable timetable;

    String consultationID;

    TimetableService timetableService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_add_timetable);

        spDay=findViewById(R.id.spinnerDay);
        etStartHour=findViewById(R.id.etStartTime);
        etEndHour=findViewById(R.id.etEndTime);
        btnSave=findViewById(R.id.btnSave);
        btnCancel=findViewById(R.id.btnCancel);

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
        consultationID=intent.getStringExtra("consultation_id");

        timetableService= new TimetableService(getApplicationContext());
        ArrayList<String>daysOfWeek= new ArrayList<>();

        daysOfWeek.add("lunes");
        daysOfWeek.add("martes");
        daysOfWeek.add("miercoles");
        daysOfWeek.add("jueves");
        daysOfWeek.add("viernes");
        daysOfWeek.add("sabado");
        daysOfWeek.add("domingo");

        ArrayAdapter sAdapter= new ArrayAdapter(ProfessionalAddTimetable.this, android.R.layout.simple_spinner_dropdown_item, daysOfWeek);
        spDay.setAdapter(sAdapter);

        spDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                day=spDay.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        btnSave.setOnClickListener(v -> {

            timetable = new Timetable();
            timetable.setConsultation_id(consultationID);
            timetable.setDayOfWeek(day);
            timetable.setStartTime(LocalTime.parse(etStartHour.getText().toString(), formatter));
            timetable.setEndTime(LocalTime.parse(etEndHour.getText().toString(), formatter));

            TimetableString timetableString = timetable.convertToTimetableString();
            timetableService.insertTimetableString(timetableString);

            onBackPressed();
        });

        btnCancel.setOnClickListener(v -> {
            onBackPressed();
        });
    }


}