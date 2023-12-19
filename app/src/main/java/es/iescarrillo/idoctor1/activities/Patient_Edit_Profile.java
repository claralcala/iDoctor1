package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.services.PatientService;

public class Patient_Edit_Profile extends AppCompatActivity {

    EditText etName, etSurname, etPassword, etDNI, etEmail, etNumber;
    CheckBox cbHealthInsurance;
    Button btnAccept, btnBack;

    ImageView ivPhoto;

    PatientService patientService;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_profile);

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

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etPassword = findViewById(R.id.etPassword);
        etDNI = findViewById(R.id.etDNI);
        etEmail = findViewById(R.id.etEmail);
        etNumber = findViewById(R.id.etNumber);

        cbHealthInsurance = findViewById(R.id.cbHealthInsurance);

        btnAccept = findViewById(R.id.btnAccept);
        btnBack = findViewById(R.id.btnBack);

        ivPhoto = findViewById(R.id.ivPhoto);

        patientService = new PatientService(getApplicationContext());

        Intent intent = getIntent();

        patient = new Patient();
        if(intent != null){
            patient = (Patient) intent.getSerializableExtra("patient");
        }

        
    }
}