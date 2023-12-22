package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import es.iescarrillo.idoctor1.R;

/**
 * @author clara
 * Pantalla principal del profesional
 */

public class ProfessionalMainActivity extends AppCompatActivity {

    Button btnLogout, btnViewProfile, btnViewConsultations, btnViewVal;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_main);

        //Inicializamos componentes
        btnLogout=findViewById(R.id.btnLogout);
        btnViewProfile=findViewById(R.id.btnViewProfile);
        btnViewConsultations=findViewById(R.id.btnViewConsultations);
        btnViewVal=findViewById(R.id.btnValor);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id = sharedPreferences.getString("id", "");

        //Comprobación de rol, si no es un profesional le echamos
        if(!role.equals("PROFESSIONAL")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        //Boton que lleva al perfil del profesional
        btnViewProfile.setOnClickListener(v -> {
            Intent profile = new Intent (this, ProfessionalProfileActivity.class);
            profile.putExtra("id", id);
            startActivity(profile);
        });

        //Boton para ver las consultas del profesional
        btnViewConsultations.setOnClickListener(v -> {
            Intent consult = new Intent (this, ProfessionalViewConsultations.class);
            consult.putExtra("id", id);
            startActivity(consult);
        });

        //Boton para ver las valoraciones del profesional
        btnViewVal.setOnClickListener(v -> {

            Intent viewVal  =new Intent(this, ProfessionalViewAssessment.class);
            startActivity(viewVal);

        });


        //Botón para desloguearse
        btnLogout.setOnClickListener(v -> {
                sharedPreferences.edit().clear().apply();
                Intent backMain = new Intent(this, MainActivity.class);
                startActivity(backMain);

        });
    }
}