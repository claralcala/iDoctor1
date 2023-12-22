package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.AppointmentString;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.ConsultationService;

/**
 * @author damian
 *
 * Pantalla para ver detalles de la cita
 */
public class PatientAppointmentDetails extends AppCompatActivity {
    TextView tvAppointmentDatePatient;
    TextView tvAppointmentTimePatient;
    TextView tvConsultationPatient;
    Button btnCancelAppointmentPatient;
    Button btnBackToAppointment;
    Button btnViewEvaluation;

    Appointment appointment;
    Consultation consultation;
    String consultation_id;
    String consultationAddress;
    AppointmentString appointmentString;
    AppointmentService appointmentService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_details);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");


       //Comprobacion de roles
        if(!role.equals("PATIENT")){
            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);
        }

        //Inicializacion de componentes
        tvAppointmentDatePatient=findViewById(R.id.tvAppointmentDatePatient);
        tvAppointmentTimePatient=findViewById(R.id.tvAppointmentTimePatient);
        tvConsultationPatient=findViewById(R.id.tvConsultationPatient);
        appointmentService=new AppointmentService(getApplicationContext());

       //En el intent nos traemos el objeto
        Intent intent=getIntent();
        if (intent!=null){
            appointment=(Appointment)intent.getSerializableExtra("appointment");
        }

        consultation_id=appointment.getConsultation_id();

        //Servicio
        ConsultationService consultationService=new ConsultationService(getApplicationContext());

        //Nos traemos las consultas
        consultationService.getConsultationByID(consultation_id, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    consultation = snapshot.getValue(Consultation.class);
                    consultationAddress = consultation.getAddress();
                    tvConsultationPatient.setText("Direccion: " + consultationAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Ponemos los datos en los campos de texto
        tvAppointmentDatePatient.setText("Fecha: " +appointment.getAppointmentDate().toString());
        tvAppointmentTimePatient.setText("Hora: " + appointment.getAppointmentTime().toString());
        btnCancelAppointmentPatient=findViewById(R.id.btnCancelAppointmentPatient);
        LocalDate currentDate=LocalDate.now();
        LocalDate DateAppointment=appointment.getAppointmentDate();

        //Permitimos cancelar si la fecha no ha pasado
        if (currentDate.isAfter(DateAppointment)){
            btnCancelAppointmentPatient.setEnabled(false);
        }else{
            btnCancelAppointmentPatient.setOnClickListener(v -> {
                appointment.setPatient_id("");
                appointmentString=appointment.convertToAppointmentString();
                appointmentService.updateAppointmentString(appointmentString);
                Intent back=new Intent(this,Patient_Main_Activity.class);
                startActivity(back);
            });

        }

        //Boton para ver la evaluacion
        btnViewEvaluation=findViewById(R.id.btnViewEvaluation);
        btnViewEvaluation.setOnClickListener(v -> {
            Intent goToEvaluation=new Intent(this,PatientViewEvaluation.class);
            goToEvaluation.putExtra("appointment",appointment);
            startActivity(goToEvaluation);
        });

        //Boton de vuelta
        btnBackToAppointment=findViewById(R.id.btnBackToAppointment);
        btnBackToAppointment.setOnClickListener(v -> {
            Intent backToAppointment=new Intent(this,PatientAppointments.class);
            startActivity(backToAppointment);
        });

    }
}
