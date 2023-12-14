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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Timetable;
import es.iescarrillo.idoctor1.models.TimetableString;
import es.iescarrillo.idoctor1.services.TimetableService;

public class ProfessionalEditTimetable extends AppCompatActivity {

    EditText etStart, etEnd;
    Spinner spDays;

    Button btnSave, btnCancel;

    TimetableService timetableService;

    Timetable timetable;

    TimetableString timetableString;

    String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_edit_timetable);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id = sharedPreferences.getString("id", "");

        if(!role.equals("PROFESSIONAL")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        etStart=findViewById(R.id.etStartTime);
        etEnd=findViewById(R.id.etEndTime);
        spDays=findViewById(R.id.spinner);
        btnSave=findViewById(R.id.btnSave);
        btnCancel=findViewById(R.id.btnCancel);

        Intent intent =getIntent();


        timetable = new Timetable();
        if (intent != null) {
            timetable = (Timetable) intent.getSerializableExtra("timetable");
        }


        timetableService= new TimetableService(getApplicationContext());

        ArrayList<String> daysOfWeek= new ArrayList<>();

        daysOfWeek.add("lunes");
        daysOfWeek.add("martes");
        daysOfWeek.add("miercoles");
        daysOfWeek.add("jueves");
        daysOfWeek.add("viernes");
        daysOfWeek.add("sabado");
        daysOfWeek.add("domingo");

        ArrayAdapter sAdapter= new ArrayAdapter(ProfessionalEditTimetable.this, android.R.layout.simple_spinner_dropdown_item, daysOfWeek);
        spDays.setAdapter(sAdapter);

        spDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                day=spDays.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etStart.setText(timetable.getStartTime().toString());
        etEnd.setText(timetable.getEndTime().toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        btnSave.setOnClickListener(v -> {


            timetable.setDayOfWeek(day);
            timetable.setStartTime(LocalTime.parse(etStart.getText().toString(), formatter));
            timetable.setEndTime(LocalTime.parse(etEnd.getText().toString(), formatter));


            timetableString=timetable.convertToTimetableString();
            timetableService.updateTimetableString(timetableString);

            Intent back = new Intent (this, ProfessionalMainActivity.class);
            startActivity(back);
        });


        btnCancel.setOnClickListener(v -> {
            onBackPressed();
        });

    }
}