package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Professional;
/**
 * @author Jesús
 *
 * Pantalla para ver el perfil de un profesional desde un paciente
 */
public class Patient_View_Professional_Profile extends AppCompatActivity {

    Button btnValoration, btnBack, btnListvaloratio, btnAddAppointment;
    Professional prof;
    ImageView ivPhoto;
    TextView tvName, tvSurname, tvCollegiate, tvSpeciality, tvDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_professional_profile);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");


        if(!role.equals("PATIENT")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        tvName=findViewById(R.id.tvProfName);
        tvSurname=findViewById(R.id.tvProfSurname);
        tvCollegiate=findViewById(R.id.tvprofCollegiate);
        tvSpeciality=findViewById(R.id.tvProfSpeciality);
        tvDescription=findViewById(R.id.tvProfDescription);
        ivPhoto=findViewById(R.id.ivPhoto);

        btnValoration=findViewById(R.id.btnValoration);
        btnBack=findViewById(R.id.btnBack);
        btnListvaloratio=findViewById(R.id.btnListvaloratio);
        btnAddAppointment=findViewById(R.id.btnAddAppointment);

        Intent intent=getIntent();

        prof = new Professional();
        if (intent != null) {
            prof = (Professional) intent.getSerializableExtra("professional");
        }

        tvName.setText("Nombre: " +prof.getName());
        tvSurname.setText("Apellidos: " +prof.getSurname());
        tvCollegiate.setText("Num. Colegiado: " +prof.getCollegiateNumber());
        tvDescription.setText("Descripción: " +prof.getDescription());
        tvSpeciality.setText("Especialidad: " +prof.getSpeciality());

        if (prof.getPhoto() != null && !prof.getPhoto().isEmpty()) {
            Picasso.get().load(prof.getPhoto()).into(ivPhoto);
        } else {
            Picasso.get().load("https://img.freepik.com/vector-gratis/fondo-personaje-doctor_1270-84.jpg?w=740&t=st=1702906621~exp=1702907221~hmac=ab4e750f9abbc3639d96cc11482c3e2d4e2884af78c387638bd8b2c6c2ade362").into(ivPhoto);
        }

        btnBack.setOnClickListener(v -> {
            Intent back = new Intent(this, Patient_View_Professional.class);
            startActivity(back);
        });

        btnAddAppointment.setOnClickListener(v -> {
            Intent consultation = new Intent(this, Patient_View_Consultation.class);
            consultation.putExtra("professional", prof);
            startActivity(consultation);
        });

        btnListvaloratio.setOnClickListener(v -> {
            Intent patientViewAssessment=new Intent(this,PatientViewAssessment.class);
            patientViewAssessment.putExtra("professional", prof);
            startActivity(patientViewAssessment);
        });

        btnValoration.setOnClickListener(v -> {
            Intent insertAppointment=new Intent(this,PatientInsertAssessment.class);
            insertAppointment.putExtra("professional", prof);
            startActivity(insertAppointment);
        });

    }
}