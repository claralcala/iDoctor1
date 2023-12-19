package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.ConsultationService;

public class ProfessionalConsultationDetails extends AppCompatActivity {

    TextView tvAddress, tvCity, tvEmail, tvPhone, tvPhoneAux;

    Button btnEdit, btnDelete, btnViewTimetable, btnViewAppointments, btnBack, btnGenerateAppointments;


    Consultation cons;

    ConsultationService consService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_consultation_details);


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


        tvAddress=findViewById(R.id.tvAddress);
        tvCity=findViewById(R.id.tvCity);
        tvEmail=findViewById(R.id.tvEmail);
        tvPhone=findViewById(R.id.tvPhone);
        tvPhoneAux=findViewById(R.id.tvPhoneAux);

        btnBack=findViewById(R.id.btnCancel);
        btnDelete=findViewById(R.id.btnDelete);
        btnViewAppointments=findViewById(R.id.btnViewAppointments);
        btnViewTimetable=findViewById(R.id.btnViewTimetable);
        btnGenerateAppointments=findViewById(R.id.btnGenerateAppointments);

        btnEdit=findViewById(R.id.btnEdit);

        Intent intent = getIntent();

        consService= new ConsultationService(getApplicationContext());


        cons = new Consultation();
        if (intent != null) {
            cons = (Consultation) intent.getSerializableExtra("consultation");
        }

        tvAddress.setText("Direccion: " +cons.getAddress());
        tvCity.setText("Ciudad: " +cons.getCity());
        tvEmail.setText("Email: " +cons.getEmail());
        tvPhone.setText("Telefono: " +cons.getPhone());
        tvPhoneAux.setText("Telf. Auxiliar: " +cons.getPhoneAux());

        btnBack.setOnClickListener(v -> {
            Intent back = new Intent(this, ProfessionalViewConsultations.class);
            startActivity(back);
        });

        btnViewAppointments.setOnClickListener(v -> {
            Intent appointment = new Intent (this, ProfessionalViewAppointments.class);
            appointment.putExtra("consultation", cons);
            startActivity(appointment);
        });

        btnViewTimetable.setOnClickListener(v -> {
            Intent timetable = new Intent(this, ProfessionalViewTimetable.class);
            timetable.putExtra("consultation", cons);
            startActivity(timetable);
        });

        btnEdit.setOnClickListener(v -> {
            Intent edit = new Intent(this, ProfessionalEditConsultation.class);
            edit.putExtra("consultation", cons);
            startActivity(edit);
        });


        btnDelete.setOnClickListener(v -> {

            consService.deleteConsultation(cons.getId());
            Intent back = new Intent(this, ProfessionalViewConsultations.class);
            startActivity(back);

        });

        btnGenerateAppointments.setOnClickListener(v -> {
            //Meter el intent hacia la pantalla de generar citas
        });

    }
}