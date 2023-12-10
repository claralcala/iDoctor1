package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Professional;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            Intent register= new Intent(this, RegisterActivity.class);
            startActivity(register);
        });

    }
}