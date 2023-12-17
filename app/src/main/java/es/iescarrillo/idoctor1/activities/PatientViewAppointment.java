package es.iescarrillo.idoctor1.activities;

import static com.google.firebase.database.core.RepoManager.clear;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.AppointmentAdapter;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.services.AppointmentService;

public class PatientViewAppointment extends AppCompatActivity {
    ListView lvAppointment;
    Button btnBackPatientMain;
    Appointment appointment;
    AppointmentAdapter apAdapter;
    AppointmentService apService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_appointment);
        ArrayList <Appointment> appointmentsList=new ArrayList<Appointment>();
        apService = new AppointmentService(getApplicationContext());
        lvAppointment=findViewById(R.id.lvPatientAppointment);
        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferenceDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");
//        if(!role.equals("PATIENT")){
//            sharedPreferences.edit().clear().apply();
//            Intent backMain = new Intent(this, MainActivity.class);
//            startActivity(backMain);
//        }
        DatabaseReference dbAppointmentPatient= FirebaseDatabase.getInstance().getReference().child("appointment");
        apService.getAppointmentsByPatientID(id_, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //appointmentsList.clear();
                for (DataSnapshot data:datasnapshot.getChildren()){
                    appointment=data.getValue(Appointment.class);
                    appointmentsList.add(appointment);
                }
                Log.d("PatientViewAppointment", "Appointments count: " + appointmentsList.size());

                apAdapter=new AppointmentAdapter(getApplicationContext(),appointmentsList);
                lvAppointment.setAdapter(apAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PatientViewAppointment", "Error getting appointments: " + error.getMessage());

            }
        });
        lvAppointment.setOnItemClickListener((parent, view, position, id) -> {
            appointment=(Appointment) parent.getItemAtPosition(position);
            Log.d("PatientViewAppointment", "Selected appointment: " + appointment.getId());
            Intent intent=new Intent(this, PatientAppointmentDetails.class);
            intent.putExtra("appointment",appointment);
            startActivity(intent);
        });

        btnBackPatientMain=findViewById(R.id.btnBackPatientMain);
        btnBackPatientMain.setOnClickListener(v -> {
            Intent intent=new Intent(this, PatientMainActivity.class);
            startActivity(intent);
        });
    }
}