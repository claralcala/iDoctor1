package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import es.iescarrillo.idoctor1.R;
/**
 * @author Jesús
 *
 * Main del paciente
 */
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

        //Comprobamos que el rol del usuario sea paciente
        if(!role.equals("PATIENT")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }
        //inicializamos componentes
        btnViewProfile = findViewById(R.id.btnViewProfile);
        btnViewProfesionals = findViewById(R.id.btnViewProfesionals);
        btnViewCites = findViewById(R.id.btnViewCites);
        btnLogout = findViewById(R.id.btnLogout);

        //Boton para desloguearnos
        btnLogout.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);


        });

        //Boton para ir a ver nuestro perfil
        btnViewProfile.setOnClickListener(v -> {
            Intent profile = new Intent(this, Patient_View_Profile.class);
            profile.putExtra("id", id);
            startActivity(profile);
        });

        //Boton para ver los profesionales
        btnViewProfesionals.setOnClickListener(v -> {
            Intent prof = new Intent(this, Patient_View_Professional.class);
            startActivity(prof);
        });



        //Boton para ver las citas disponibles
        btnViewCites.setOnClickListener(v->{
            Intent viewAppointment=new Intent(this, PatientAppointments.class);
            startActivity(viewAppointment);
        });
    }
}