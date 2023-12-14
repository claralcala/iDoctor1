package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ProfessionalEditProfile extends AppCompatActivity {


    Button btnSave, btnCancel;

    EditText etName, etSurname, etCollegiate, etDescription, etUsername, etPassword;
    Spinner spSpeciality;
    ProfessionalService profService;
    Professional prof;

    String profSpeciality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_edit_profile);

        btnSave=findViewById(R.id.btnSaveProf);
        btnCancel=findViewById(R.id.btnCancel);
        etName=findViewById(R.id.etProfName);
        etSurname=findViewById(R.id.etProfSurname);
        etCollegiate=findViewById(R.id.etCollegiate);
        etDescription=findViewById(R.id.etProfDescription);
        etUsername=findViewById(R.id.etProfUserName);
        etPassword=findViewById(R.id.etProfPassword);

        spSpeciality=findViewById(R.id.spnSpeciality);

        Intent intent = getIntent();

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


        profService=new ProfessionalService(getApplicationContext());


        ArrayList<String> specialities= new ArrayList<>();
        specialities.add("general");
        specialities.add("fisioterapia");
        specialities.add("odontologia");

        ArrayAdapter sAdapter= new ArrayAdapter(ProfessionalEditProfile.this, android.R.layout.simple_spinner_dropdown_item, specialities);
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


        prof = new Professional();
        if (intent != null) {
            prof = (Professional) intent.getSerializableExtra("professional");
        }


        etName.setText(prof.getName());
        etSurname.setText(prof.getSurname());
        etCollegiate.setText(prof.getCollegiateNumber());
        etDescription.setText(prof.getDescription());
        etUsername.setText(prof.getUsername());



        btnSave.setOnClickListener(v -> {
            prof.setName(etName.getText().toString());
            prof.setSurname(etSurname.getText().toString());
            prof.setCollegiateNumber(etCollegiate.getText().toString());
            prof.setSpeciality(profSpeciality);
            prof.setUsername(etUsername.getText().toString());
            prof.setDescription(etDescription.getText().toString());

            String encryptPassword = BCrypt.hashpw(etPassword.getText().toString(), BCrypt.gensalt(5));
            prof.setPassword(encryptPassword);

            profService.updateProfessional(prof);

            Intent back = new Intent (this, ProfessionalProfileActivity.class);
            startActivity(back);
        });


        btnCancel.setOnClickListener(v -> {
            Intent back = new Intent (this, ProfessionalProfileActivity.class);
            startActivity(back);

        });


    }
}