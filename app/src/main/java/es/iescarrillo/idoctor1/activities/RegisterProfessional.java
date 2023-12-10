package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.ProfessionalService;

public class RegisterProfessional extends AppCompatActivity {

    EditText etName, etSurname, etCollegiate, etDescription, etUsername, etPassword;

    Spinner spSpeciality;
    Button btnSave, btnCancel;

    ProfessionalService profService;

    Professional prof;

    String profSpeciality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_professional);


        etName=findViewById(R.id.etProfName);
        etSurname=findViewById(R.id.etProfSurname);
        etCollegiate=findViewById(R.id.etCollegiate);
        spSpeciality=findViewById(R.id.spnSpeciality);
        etDescription=findViewById(R.id.etProfDescription);
        etUsername=findViewById(R.id.etProfUserName);
        etPassword=findViewById(R.id.etProfPassword);


        btnSave=findViewById(R.id.btnSaveProf);
        btnCancel=findViewById(R.id.btnCancel);

        ArrayList<String> specialities= new ArrayList<>();
        specialities.add("general");
        specialities.add("fisioterapia");
        specialities.add("odontologia");

        ArrayAdapter sAdapter= new ArrayAdapter(RegisterProfessional.this, android.R.layout.simple_spinner_dropdown_item, specialities);
        spSpeciality.setAdapter(sAdapter);

        spSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                profSpeciality=spSpeciality.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        profService = new ProfessionalService(getApplicationContext());
        prof = new Professional();

        btnSave.setOnClickListener(v -> {

            prof.setName(etName.getText().toString());
            prof.setSurname(etSurname.getText().toString());
            prof.setCollegiateNumber((etCollegiate.getText().toString()));
            prof.setDescription(etDescription.getText().toString());
            prof.setSpeciality(profSpeciality);
            prof.setUsername(etUsername.getText().toString());
            prof.setRole("PROFESSIONAL");
            String encryptPassword = BCrypt.hashpw(etPassword.getText().toString(), BCrypt.gensalt(5));
            prof.setPassword(encryptPassword);

            profService.insertProfessional(prof);

            Intent intent = new Intent (this, MainActivity.class);
            startActivity(intent);
        });


        btnCancel.setOnClickListener(v -> {

            Intent intent = new Intent (this, MainActivity.class);
            startActivity(intent);

        });


    }
}