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
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.AppointmentString;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.services.AppointmentService;
/**
 * @author Jesús
 *
 * Pantalla para ver las citas de un profesional
 */
public class Patient_View_Appointment extends AppCompatActivity {

    ListView lvAppointments;
    Button btnBack;
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
        setContentView(R.layout.activity_patient_view_appointment);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");

        //Comprobamos que el rol del usuario sea paciente
        if(!role.equals("PATIENT")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        //Inicializamos componentes
        lvAppointments=findViewById(R.id.lvAppointments);
        btnBack=findViewById(R.id.btnBack);


        appService = new AppointmentService(getApplicationContext());

        //Recuperamos los datos de la consulta
        Intent intent=getIntent();

        appointments= new ArrayList<>();

        consul= new Consultation();
        if (intent != null) {
            consul = (Consultation) intent.getSerializableExtra("consultation");
        }


        consultationID=consul.getId();

        Log.d("ProfessionalViewAppointments", "Consultation id " + consultationID);
        app = new Appointment();

        //Con el siguiente metodo volvamos las citas de una consulta en una listView
        appService.getAppointmentsByConsultation(consultationID, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                appointments.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto Superhero
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

        //Al hacer click en algun dato de la ListView nos lleva a sus detalles
        lvAppointments.setOnItemClickListener((parent, view, position, id) -> {
            app = (Appointment) parent.getItemAtPosition(position);
            Intent details = new Intent(this, Patient_Get_Appointment.class);
            details.putExtra("appointment", app);
            startActivity(details);
        });

        //Boton para volver a la activity anterior
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }
}