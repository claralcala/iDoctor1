package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.PatientService;

public class Patient_View_Profile extends AppCompatActivity {
    PatientService patientService;
    Patient patient;
    Button btnEdit, btnBack;
    TextView tvName,tvSurname, tvUsername, tvDni, tvEmail, tvPhone;
    CheckBox cbHealthInsurance;
    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_profile);

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

        btnEdit = findViewById(R.id.btnEdit);
        btnBack = findViewById(R.id.btnBack);

        tvName = findViewById(R.id.tvName);
        tvSurname = findViewById(R.id.tvSurname);
        tvUsername = findViewById(R.id.tvUsername);
        tvDni = findViewById(R.id.tvDni);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);

        cbHealthInsurance = findViewById(R.id.cbHealthInsurance);

        ivPhoto=findViewById(R.id.ivPhoto);

        patientService = new PatientService(getApplicationContext());
        patientService.getPatientByID(id, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto professio
                    patient = dataSnapshot.getValue(Patient.class);
                    tvName.setText("Nombre: " +patient.getName());
                    tvSurname.setText("Apellidos: " +patient.getSurname());
                    tvUsername.setText("Nombre usuario: " +patient.getUsername());
                    tvDni.setText("DNI: " +patient.getDni());
                    tvEmail.setText("Email: " +patient.getEmail());
                    tvPhone.setText("Numero: " +patient.getPhone());

                    if(patient.isHealthInsurance()){
                        cbHealthInsurance.setChecked(true);
                    }else{
                        cbHealthInsurance.setChecked(false);
                    }

                    if (patient.getPhoto() != null && !patient.getPhoto().isEmpty()) {
                        Picasso.get().load(patient.getPhoto()).into(ivPhoto);
                    } else {
                        Picasso.get().load("https://img.freepik.com/vector-gratis/ilustracion-concepto-dolor-alimentos_114360-16553.jpg?w=826&t=st=1702917731~exp=1702918331~hmac=444109e17f3a8f166c50ae83ebd1e70458916c93b034d22f40e0ba26fb7af18a").into(ivPhoto);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnEdit.setOnClickListener(v -> {
            Intent edit = new Intent(this, Patient_Edit_Profile.class);
            edit.putExtra("patient", patient);

            startActivity(edit);
        });

    }
}