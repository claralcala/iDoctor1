package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.ProfessionalService;

public class ProfessionalProfileActivity extends AppCompatActivity {

    Button btnEdit, btnBack;

    ProfessionalService profService;

    Professional prof;

    String name, surname;

    ImageView ivPhoto;
    TextView tvName, tvSurname, tvUsername, tvCollegiate, tvSpeciality, tvDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_profile);

        tvName=findViewById(R.id.tvProfName);
        tvSurname=findViewById(R.id.tvProfSurname);
        tvUsername=findViewById(R.id.tvProfUsername);
        tvCollegiate=findViewById(R.id.tvprofCollegiate);
        tvSpeciality=findViewById(R.id.tvProfSpeciality);
        tvDescription=findViewById(R.id.tvProfDescription);
        ivPhoto=findViewById(R.id.prof_photo);

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

        btnEdit=findViewById(R.id.btnEdit);
        btnBack=findViewById(R.id.btnBack);

        Intent intent=getIntent();

        profService=new ProfessionalService(getApplicationContext());

        profService.getProfessionalByID(id, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto professio
                    prof = dataSnapshot.getValue(Professional.class);
                    tvName.setText("Nombre: " +prof.getName());
                    tvSurname.setText("Apellidos: " +prof.getSurname());
                    tvCollegiate.setText("Num. Colegiado: " +prof.getCollegiateNumber());
                    tvDescription.setText("Descripción: " +prof.getDescription());
                    tvSpeciality.setText("Especialidad: " +prof.getSpeciality());
                    tvUsername.setText("Nom. usuario: " +prof.getUsername());

                    if (prof.getPhoto() != null && !prof.getPhoto().isEmpty()) {
                        Picasso.get().load(prof.getPhoto()).into(ivPhoto);
                    } else {
                        Picasso.get().load("https://img.freepik.com/vector-gratis/fondo-personaje-doctor_1270-84.jpg?w=740&t=st=1702906621~exp=1702907221~hmac=ab4e750f9abbc3639d96cc11482c3e2d4e2884af78c387638bd8b2c6c2ade362").into(ivPhoto);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnEdit.setOnClickListener(v -> {
            Intent edit = new Intent (this, ProfessionalEditProfile.class);
            edit.putExtra("professional", prof);

            startActivity(edit);
        });


        btnBack.setOnClickListener(v -> {
            Intent back = new Intent (this, ProfessionalMainActivity.class);
            startActivity(back);
        });







    }
}