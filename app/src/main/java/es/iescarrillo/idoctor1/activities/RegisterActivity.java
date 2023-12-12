package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import es.iescarrillo.idoctor1.R;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegisterProf, btnRegisterPatient, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegisterPatient=findViewById(R.id.btnRegisterPatient);
        btnRegisterProf=findViewById(R.id.btnRegisterProfessional);
        btnBack=findViewById(R.id.btnBack);

        btnRegisterProf.setOnClickListener(v -> {
            Intent reg = new Intent (this, RegisterProfessional.class);
            startActivity(reg);
        });


        btnRegisterPatient.setOnClickListener(v -> {
            Intent regP = new Intent (this, RegisterPatient.class);
            startActivity(regP);
        });


        btnBack.setOnClickListener(v -> {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        });
    }
}