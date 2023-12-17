package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import es.iescarrillo.idoctor1.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;

public class PatientMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
//        //Variables de sesión
        SharedPreferences sharedPreferences = getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");
        if (!role.equals("PATIENT")) {
            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);
//
//
//        }
            Button btnPatientAppointment = findViewById(R.id.btnPatientAppointment);
            btnPatientAppointment.setOnClickListener(v -> {
                Intent intent = new Intent(this, PatientViewAppointment.class);
                startActivity(intent);
            });


        }
    }
}