package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;

public class PatientAppointmentDetails extends AppCompatActivity {
    TextView tvAppointmentDatePatient;
    TextView tvAppointmentTimePatient;
    TextView tvProfessionalPatient;
    Button btnCancelAppointmentPatient;
    Appointment appointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_details);
        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferenceDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");
//        if(!role.equals("PATIENT")){
//            sharedPreferences.edit().clear().apply();
//            Intent backMain = new Intent(this, MainActivity.class);
//            startActivity(backMain);
//        }
        Intent intent=getIntent();
        if (intent!=null){
            appointment=(Appointment)intent.getSerializableExtra("appointment");
        }

    }
}