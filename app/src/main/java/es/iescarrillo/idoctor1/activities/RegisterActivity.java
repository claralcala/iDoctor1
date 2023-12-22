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
 *
 * Pantalla para seleccionar el registro
 */
public class RegisterActivity extends AppCompatActivity {

    Button btnRegisterProf, btnRegisterPatient, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //En esta pantalla podremos seleccionar si queremos registrarnos como profesional, paciente o volver
        btnRegisterPatient=findViewById(R.id.btnRegisterPatient);
        btnRegisterProf=findViewById(R.id.btnRegisterProfessional);
        btnBack=findViewById(R.id.btnBack);

        //Boton que lleva a registrarse como profesional
        btnRegisterProf.setOnClickListener(v -> {
            Intent reg = new Intent (this, RegisterProfessional.class);
            startActivity(reg);
        });


        //Boton que lleva a registrarse como paciente
        btnRegisterPatient.setOnClickListener(v -> {
            Intent regP = new Intent (this, RegisterPatient.class);
            startActivity(regP);
        });


        //Boton para volver
        btnBack.setOnClickListener(v -> {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        });
    }
}