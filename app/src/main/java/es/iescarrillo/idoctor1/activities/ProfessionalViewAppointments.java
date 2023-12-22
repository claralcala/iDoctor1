package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.AppointmentAdapter;
import es.iescarrillo.idoctor1.adapters.TimetableAdapter;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.AppointmentString;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Timetable;
import es.iescarrillo.idoctor1.services.AppointmentService;

/**
 * @author clara
 * Pantalla para ver las citas asignadas a una consulta
 */
public class ProfessionalViewAppointments extends AppCompatActivity {

    ListView lvAppointments;

    Button btnBack, btnAdd;

    ArrayList<Appointment> appointments;

    AppointmentService appService;

    Consultation consul;

    Appointment app;

    AppointmentAdapter adapter;


    String consultationID;

    AppointmentString appString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_view_appointments);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");


        //Comprobacion de roles
        if(!role.equals("PROFESSIONAL")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        //Inicializacion de componentes
        lvAppointments=findViewById(R.id.lvAppointments);
        btnBack=findViewById(R.id.btnBack);
        btnAdd=findViewById(R.id.btnAddAppointment);

        //Servicio
        appService = new AppointmentService(getApplicationContext());

        Intent intent=getIntent();


        appointments= new ArrayList<>();

        //Nos traemos el objeto en el intent
        consul= new Consultation();
        if (intent != null) {
            consul = (Consultation) intent.getSerializableExtra("consultation");
        }


       consultationID=consul.getId();

        Log.d("ProfessionalViewAppointments", "Consultation id " + consultationID);
        app = new Appointment();


        //Nos traemos las citas a través del id de la consulta
        appService.getAppointmentsByConsultation(consultationID, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                appointments.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto Appointment
                    appString= snapshot.getValue(AppointmentString.class);
                    app=appString.convertToAppointment();
                    appointments.add(app);
                }

                Log.d("ProfessionalViewAppointments", "Appointments size: " + appointments.size());

                // Una vez los datos añadidos a nuestra lista, se la pasamos al adaptador
                adapter = new AppointmentAdapter(getApplicationContext(), appointments);
                lvAppointments.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Cuando clicamos en un item nos llevamos el objeto en el intent
        lvAppointments.setOnItemClickListener((parent, view, position, id) -> {
            app = (Appointment) parent.getItemAtPosition(position);
            Intent details = new Intent(this, ProfessionalAppointmentDetails.class);
            details.putExtra("appointment", app);
            startActivity(details);
        });


        //Accion boton añadir
        btnAdd.setOnClickListener(v -> {
            Intent add = new Intent(this, ProfessionalAddAppointment.class);
            add.putExtra("consultation_id", consultationID);
            startActivity(add);
        });

        //Accion boton volver
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}