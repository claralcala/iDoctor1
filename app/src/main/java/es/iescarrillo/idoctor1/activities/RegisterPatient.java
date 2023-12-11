package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.mindrot.jbcrypt.BCrypt;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.services.PatientService;

public class RegisterPatient extends AppCompatActivity {

    Button btnSave, btnBack;

    EditText etName, etSurname, etUsername, etPassword, etDNI, etEmail, etPhone;

    CheckBox cbHealthInsurance;

    PatientService pService;

    Patient pat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        btnSave=findViewById(R.id.btnSavePatient);
        btnBack=findViewById(R.id.btnCancel);

        etName=findViewById(R.id.etPatientName);
        etSurname=findViewById(R.id.etPatientName);
        etUsername=findViewById(R.id.etPatientUserName);
        etPassword=findViewById(R.id.etPatientPassword);
        etDNI=findViewById(R.id.etDNI);
        etEmail=findViewById(R.id.etPatientMail);
        etPhone=findViewById(R.id.etTelephone);

        cbHealthInsurance=findViewById(R.id.checkBoxInsurance);

        btnBack.setOnClickListener(v -> {
            Intent back = new Intent(this, RegisterActivity.class);
            startActivity(back);
        });

        pService= new PatientService(getApplicationContext());

        pat = new Patient();

        btnSave.setOnClickListener(v -> {
            pat.setName(etName.getText().toString());
            pat.setSurname(etSurname.getText().toString());
            pat.setDni(etDNI.getText().toString());
            pat.setEmail(etEmail.getText().toString());
            pat.setPhone(etPhone.getText().toString());

            if(cbHealthInsurance.isChecked()){
                pat.setHealthInsurance(true);
            }else{
                pat.setHealthInsurance(false);
            }
            pat.setUsername(etUsername.getText().toString());
            pat.setRole("PATIENT");
            String encryptPassword = BCrypt.hashpw(etPassword.getText().toString(), BCrypt.gensalt(5));
            pat.setPassword(encryptPassword);


            pService.insertPatient(pat);

            Intent intent = new Intent (this, MainActivity.class);
            startActivity(intent);
        });

    }
}