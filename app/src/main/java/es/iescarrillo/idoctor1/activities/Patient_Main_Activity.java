package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import es.iescarrillo.idoctor1.R;

public class Patient_Main_Activity extends AppCompatActivity {

    Button btnViewProfile, btnViewProfesionals, btnViewCites, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id = sharedPreferences.getString("id", "");


        if(!role.equals("PATIENT")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        btnViewProfile = findViewById(R.id.btnViewProfile);
        btnViewProfesionals = findViewById(R.id.btnViewProfesionals);
        btnViewCites = findViewById(R.id.btnViewCites);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);


        });

        btnViewProfile.setOnClickListener(v -> {
            Intent profile = new Intent(this, Patient_View_Profile.class);
            profile.putExtra("id", id);
            startActivity(profile);
        });

        btnViewProfesionals.setOnClickListener(v -> {
            Intent prof = new Intent(this, Patient_View_Professional.class);
            startActivity(prof);
        });

        btnViewCites.setOnClickListener(v -> {

        });

    }
}