package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.AppointmentString;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.PatientService;
/**
 * @author Jesús
 *
 * Pantalla para coger cita
 */
public class Patient_Get_Appointment extends AppCompatActivity {

    Appointment app;
    TextView tvDate, tvHour;
    CheckBox cbActive;
    Button btnReserve, btnBack;
    AppointmentService appService;
    AppointmentString appointmentString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_get_appointment);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id = sharedPreferences.getString("id", "");

        //Comprobamos que el rol del usuario sea paciente
        if(!role.equals("PATIENT")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        //Nos traemos los datos de un objeto appointment
        Intent intent = getIntent();

        app= new Appointment();
        if (intent != null) {
            app = (Appointment) intent.getSerializableExtra("appointment");
        }


        //Inicializamos componentes
        tvDate=findViewById(R.id.tvDate);
        tvHour=findViewById(R.id.tvHour);
        cbActive=findViewById(R.id.checkBoxActive);

        btnReserve=findViewById(R.id.btnReserve);
        btnBack = findViewById(R.id.btnBack);

        appointmentString = new AppointmentString();
        appService= new AppointmentService(getApplicationContext());

        tvDate.setText("Fecha: "+app.getAppointmentDate().toString());
        tvHour.setText("Hora: " +app.getAppointmentTime().toString());

        if(app.isActive()){
            cbActive.setChecked(true);
        }else{

        }
        //Comprobamos que la cita este activa para habilitar el boton de reservar o no
        if(cbActive.isChecked()){
            btnReserve.setEnabled(true);
        }else{
            btnReserve.setEnabled(false);
        }
        //Boton para reservar cita
        btnReserve.setOnClickListener(v -> {

            app.setPatient_id(id);
            app.setActive(false);
            appointmentString = app.convertToAppointmentString();

            appService.updateAppointmentString(appointmentString);

            Intent reserve = new Intent(this, Patient_Main_Activity.class);
            startActivity(reserve);
        });
        //Boton para volver atrás
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }
}