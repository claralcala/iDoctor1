package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import es.iescarrillo.idoctor1.R;

public class ProfessionalAddEvaluation extends AppCompatActivity {

    Button btnDate, btnTime;
    EditText edDate, edTime;
    private int dia,mes,ano,hora,minutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_add_evaluation);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id = sharedPreferences.getString("id", "");

        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);

        edDate = findViewById(R.id.etDate);
        edTime = findViewById(R.id.etTime);

        btnDate.setOnClickListener( v -> {
            final Calendar c= Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }
            ,dia,mes,ano);
            datePickerDialog.show();
        });

        btnTime.setOnClickListener( v -> {

            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog  = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                edTime.setText(hourOfDay +":" +minute);

                }
            },hora,minutos,false);
            timePickerDialog.show();


        });
    }
}