package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.PatientService;
import es.iescarrillo.idoctor1.services.ProfessionalService;

public class RegisterProfessional extends AppCompatActivity {

    EditText etName, etSurname, etCollegiate, etDescription, etUsername, etPassword;

    Spinner spSpeciality;
    Button btnSave, btnCancel, btnPhoto, btnDeletePhoto;

    ImageView ivPhoto;

    StorageReference storageRef;
    String storage_path = "photo/*";

    private static final int COD_SEL_IMAGE=300;

    private Uri image_url;
    String photo = "photo";
    String idd;


    ProfessionalService profService;

    Professional prof;

    String profSpeciality;

    PatientService patService;


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
        btnPhoto=findViewById(R.id.btn_photo);
        btnDeletePhoto=findViewById(R.id.btn_remove_photo);

        btnSave=findViewById(R.id.btnSaveProf);
        btnCancel=findViewById(R.id.btnCancel);

        ArrayList<String> specialities= new ArrayList<>();
        specialities.add("general");
        specialities.add("fisioterapia");
        specialities.add("odontologia");

        patService= new PatientService(getApplicationContext());

        storageRef= FirebaseStorage.getInstance().getReference();


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


        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        profService = new ProfessionalService(getApplicationContext());
        prof = new Professional();


        btnSave.setOnClickListener(v -> {

            String username=etUsername.getText().toString();

            profService.getProfessionalByUsername(username, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        // El nombre de usuario ya está en uso por un profesional, muestra un Toast y no registres al profesional
                        Toast.makeText(RegisterProfessional.this, "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
                    } else {
                        // El nombre de usuario no existe en profesionales, verifica en pacientes
                        checkPatientUsername(username);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        });


        btnCancel.setOnClickListener(v -> {

            Intent intent = new Intent (this, MainActivity.class);
            startActivity(intent);

        });


    }

    private void checkPatientUsername(String username) {
        // Verificar si el nombre de usuario ya existe en pacientes
        patService.getPatientByUsername(username, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot patSnapshot) {

                if (patSnapshot.exists()) {
                    // El nombre de usuario ya está en uso por un paciente, muestra un Toast y no registres al profesional
                    Toast.makeText(RegisterProfessional.this, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                } else {
                    registerProfessional();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de cancelación si es necesario
            }
        });
    }


    private void registerProfessional() {
        // El nombre de usuario no existe en pacientes ni en profesionales, permite el registro del profesional
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

        Toast.makeText(RegisterProfessional.this, "Registro correcto", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent (RegisterProfessional.this, MainActivity.class);
        startActivity(intent);
    }


    private void uploadPhoto(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
    }
}