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
        tvAppointmentDatePatient=findViewById(R.id.tvAppointmentDatePatient);
        tvAppointmentTimePatient=findViewById(R.id.tvAppointmentTimePatient);
        tvConsultationPatient=findViewById(R.id.tvConsultationPatient);
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
        appointmentService=new AppointmentService(getApplicationContext());
        Intent intent=getIntent();
        if (intent!=null){
            appointment=(Appointment)intent.getSerializableExtra("appointment");
        }
        consultation_id=appointment.getConsultation_id();
        ConsultationService consultationService=new ConsultationService(getApplicationContext());
        consultationService.getConsultationByID(consultation_id, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    consultation = snapshot.getValue(Consultation.class);
                    consultationAddress = consultation.getAddress();
                    tvConsultationPatient.setText("Direccion" + consultationAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tvAppointmentDatePatient.setText("Fecha:  " +appointment.getAppointmentDate().toString());
        tvAppointmentTimePatient.setText("Hora: " + appointment.getAppointmentTime().toString());
        btnCancelAppointmentPatient=findViewById(R.id.btnCancelAppointmentPatient);
        LocalDate currentDate=LocalDate.now();
        LocalDate DateAppointment=appointment.getAppointmentDate();
        if (currentDate.isAfter(DateAppointment)){
            btnCancelAppointmentPatient.setEnabled(false);
        }else{
            btnCancelAppointmentPatient.setOnClickListener(v -> {
                appointment.setPatient_id("");
                appointmentString=appointment.convertToAppointmentString();
                appointmentService.updateAppointmentString(appointmentString);
                Intent back=new Intent(this,PatientMainActivity.class);
                startActivity(back);
            });

        }
        btnViewEvaluation=findViewById(R.id.btnViewEvaluation);
        btnViewEvaluation.setOnClickListener(v -> {
            Intent goToEvaluation=new Intent(this,PatientViewEvaluation.class);
            appointment=(Appointment)intent.getSerializableExtra("appointment");
            goToEvaluation.putExtra("appointment",appointment);
            startActivity(goToEvaluation);
        });
        btnBackToAppointment=findViewById(R.id.btnBackToAppointment);
        btnBackToAppointment.setOnClickListener(v -> {
            Intent backToAppointment=new Intent(this,PatientViewAppointment.class);
            startActivity(backToAppointment);
        });

    }
}
