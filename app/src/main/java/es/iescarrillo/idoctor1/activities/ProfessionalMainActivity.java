package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import es.iescarrillo.idoctor1.R;



public class ProfessionalMainActivity extends AppCompatActivity {

    Button btnLogout, btnViewProfile, btnViewConsultations, btnViewVal;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_main);

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


        if(!role.equals("PROFESSIONAL")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }


        btnViewProfile.setOnClickListener(v -> {
            Intent profile = new Intent (this, ProfessionalProfileActivity.class);
            profile.putExtra("id", id);
            startActivity(profile);
        });

        btnViewConsultations.setOnClickListener(v -> {
            Intent consult = new Intent (this, ProfessionalViewConsultations.class);
            consult.putExtra("id", id);
            startActivity(consult);
        });

        btnViewVal.setOnClickListener(v -> {
            Intent evaluation = new Intent (this, ProfessionalViewEvaluation.class);
            startActivity(evaluation);
        });


        btnLogout.setOnClickListener(v -> {
                sharedPreferences.edit().clear().apply();
                Intent backMain = new Intent(this, MainActivity.class);
                startActivity(backMain);

        });
    }
}