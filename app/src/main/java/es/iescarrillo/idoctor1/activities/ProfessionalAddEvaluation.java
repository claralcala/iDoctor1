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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Evaluation;
import es.iescarrillo.idoctor1.models.EvaluationString;
import es.iescarrillo.idoctor1.services.EvaluationService;

public class ProfessionalAddEvaluation extends AppCompatActivity {

    Button btnSave, btnCancel;
    EditText etDescription, etExploration, etTreatment, etDate;



    EvaluationService evaluationService;

    Evaluation evaluation;

    EvaluationString evString;

    Appointment appointment;

    String appId;

    LocalDateTime currentDate;

    /**
     * @author Manu Rguez
     * Pantalla para Añadir evaluación
     */

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

        //Inicializacion de componentes
        etDescription = findViewById(R.id.etDescription);
        etExploration = findViewById(R.id.etExploration);
        etTreatment  = findViewById(R.id.etTreatment);
        etDate=findViewById(R.id.etDate);




        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        //Nos traemos la consulta en el intent
        Intent  intent1 = getIntent();
        appointment = new Appointment();
        if (intent1 != null) {
            appointment = (Appointment) intent1.getSerializableExtra("appointment");
        }

        //asignamos una id a una variable
        appId=appointment.getId();


        evaluationService = new EvaluationService(getApplicationContext());

        //Guardamos los datos en la base de datos
        btnSave.setOnClickListener(v -> {
            evaluation = new Evaluation();
            evaluation.setDescription(etDescription.getText().toString());
            evaluation.setExploration(etExploration.getText().toString());
            evaluation.setTreatment(etTreatment.getText().toString());
            evaluation.setAppointment_id(appId);







            // Convertir las cadenas a LocalDateTime (asumiendo un formato específico)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");





            // Establecer el LocalDateTime en tu entidad Evaluation
                evaluation.setEvaluationDateTime(LocalDateTime.parse(etDate.getText().toString(), formatter));



                evString=evaluation.convertToEvaluationString();


                evaluationService.insertEvaluationString(evString);

                // Si el guardado fue exitoso, mostrar el mensaje Toast
                Toast.makeText(getApplicationContext(), "Evaluación guardada correctamente", Toast.LENGTH_SHORT).show();

                // Crear Intent para navegar a la siguiente actividad (ajustar según sea necesario)
                Intent intent = new Intent(getApplicationContext(), ProfessionalMainActivity.class);
                // Puedes agregar datos adicionales al intent si es necesario
                // intent.putExtra("clave", valor);

                // Iniciar la actividad
                startActivity(intent);

        });

        //Le damos funcion al boton de cancelar
        btnCancel.setOnClickListener(v -> {
            Intent cancel = new Intent(this, ProfessionalMainActivity.class);
            startActivity(cancel);
        });


    }
}