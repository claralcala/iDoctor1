package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Evaluation;
import es.iescarrillo.idoctor1.services.EvaluationService;

public class ProfessionalAddEvaluation extends AppCompatActivity {

    Button btnDate, btnTime, btnSave, btnCancel;
    EditText etDate, etTime,etDescription, etExploration, etTreatment;

    private int dia,mes,ano,hora,minutos;

    EvaluationService evaluationService;

    Evaluation evaluation;

    @SuppressLint("MissingInflatedId")
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



        if(!role.equals("PROFESSIONAL")){




            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);


        }

        etDescription = findViewById(R.id.etDescription);
        etExploration = findViewById(R.id.etExploration);
        etTreatment  = findViewById(R.id.etTreatment);


        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);

        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnDate.setOnClickListener( v -> {
            final Calendar c= Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
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
                    etTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                }
            }, hora, minutos, true);
            timePickerDialog.show();
        });

        evaluationService = new EvaluationService(getApplicationContext());

        btnSave.setOnClickListener(v -> {
            evaluation = new Evaluation();
            evaluation.setDescription(etDescription.getText().toString());
            evaluation.setExploration(etExploration.getText().toString());
            evaluation.setTreatment(etTreatment.getText().toString());


            // Obtener las cadenas de fecha y hora
            String dateStr = etDate.getText().toString().trim();
            String timeStr = etTime.getText().toString().trim();

            try {
                // Convertir las cadenas a LocalDateTime (asumiendo un formato específico)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(dateStr + " " + timeStr, formatter);


                // Establecer el LocalDateTime en tu entidad Evaluation
                evaluation.setEvaluationDateTime(dateTime);


                evaluationService.insertEvaluation(evaluation);

                // Si el guardado fue exitoso, mostrar el mensaje Toast
                Toast.makeText(getApplicationContext(), "Evaluación guardada correctamente", Toast.LENGTH_SHORT).show();

                // Crear Intent para navegar a la siguiente actividad (ajustar según sea necesario)
                Intent intent = new Intent(getApplicationContext(), ProfessionalAddReport.class);
                // Puedes agregar datos adicionales al intent si es necesario
                // intent.putExtra("clave", valor);

                // Iniciar la actividad
                startActivity(intent);
            } catch (DateTimeParseException e) {
                // Manejar errores de análisis de fecha y hora
                Log.e("Error", "Error al analizar la fecha y hora", e);
                Toast.makeText(getApplicationContext(), "Error al analizar la fecha y hora", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Manejar cualquier otra excepción durante el proceso de guardado
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error al guardar la evaluación", Toast.LENGTH_SHORT).show();
            }
        });


        btnCancel.setOnClickListener(v -> {
            Intent cancel = new Intent(this, ProfessionalMainActivity.class);
            startActivity(cancel);
        });


    }
}