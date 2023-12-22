package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.PatientService;

/**
 * @author clara
 * Pantalla para ver los detalles de una cita
 *
 */
public class ProfessionalAppointmentDetails extends AppCompatActivity {

    Appointment app;

    TextView tvDate, tvHour, tvPatient;
    CheckBox cbActive;

    Button btnEdit, btnDelete, btnEval, btnBack;

    PatientService patService;

    String patientUsername;

    AppointmentService appService;

    String patientId;
    Patient p;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_appointment_details);

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


        //Nos traemos el objeto appointment en el intent
        Intent intent = getIntent();

        app= new Appointment();
        if (intent != null) {
            app = (Appointment) intent.getSerializableExtra("appointment");
        }

        patientId=app.getPatient_id();

        //Inicializacion de componentes
        tvDate=findViewById(R.id.tvDate);
        tvHour=findViewById(R.id.tvHour);
        tvPatient=findViewById(R.id.tvPatient);
        cbActive=findViewById(R.id.checkBoxActive);
        btnEdit=findViewById(R.id.btnEdit);
        btnEval=findViewById(R.id.btnViewEval);
        btnDelete=findViewById(R.id.btnDelete);
        btnBack=findViewById(R.id.btnBack);

        //Servicios
        patService = new PatientService(getApplicationContext());
        appService= new AppointmentService(getApplicationContext());

        //Nos traemos el paciente por su id
        patService.getPatientByID(patientId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Convierte cada nodo de la base de datos a un objeto Paciente
                    p = snapshot.getValue(Patient.class);
                   patientUsername=p.getUsername();


                }

                //Si el nombre del paciente es nulo, le decimos que no se asigne
                if (patientUsername==null){
                    tvPatient.setText("Paciente: No asignado");
                }else {
                    tvPatient.setText("Paciente: " +patientUsername);
                }


                Log.d("ProfessionalViewAppointments", "Patient username: " + patientUsername);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        tvDate.setText("Fecha: "+app.getAppointmentDate().toString());
        tvHour.setText("Hora: " +app.getAppointmentTime().toString());

        if(app.isActive()){
            cbActive.setChecked(true);
        }else {
            cbActive.setChecked(false);
        }


        //Accion del boton editar
        btnEdit.setOnClickListener(v -> {
            Intent edit = new Intent (this, ProfessionalEditAppointment.class);
            edit.putExtra("appointment", app);
            startActivity(edit);
        });

        //Accion del boton de ver evaluaciones
        btnEval.setOnClickListener(v -> {

            Intent intent1 = new Intent(this, ProfessionalViewEvaluation.class);

            intent1.putExtra("appointment", app);

            startActivity(intent1);
        });

        //Accion del boton de volver
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        //Accion del boton de borrar
        btnDelete.setOnClickListener(v -> {
            appService.deleteAppointment(app.getId());
            Intent back = new Intent (ProfessionalAppointmentDetails.this, ProfessionalViewConsultations.class);
            startActivity(back);
        });
    }
}