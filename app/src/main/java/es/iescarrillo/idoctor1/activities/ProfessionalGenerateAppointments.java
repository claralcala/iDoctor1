package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Timetable;
import es.iescarrillo.idoctor1.models.TimetableString;
import es.iescarrillo.idoctor1.services.AppointmentService;
import es.iescarrillo.idoctor1.services.ConsultationService;
import es.iescarrillo.idoctor1.services.TimetableService;

public class ProfessionalGenerateAppointments extends AppCompatActivity {

    private ConsultationService consultationService;
    private String consultationId;
    private Consultation selectedConsultation;

    private AppointmentService appointmentService;

    EditText edDay, edInter;

    Button btnGenerate;
    private int intervaloConsulta;
    private int duracionConsulta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_generate_appointments);


        edDay = findViewById(R.id.etDay);
        edInter = findViewById(R.id.etInter);
        btnGenerate = findViewById(R.id.btnGenerate);

        consultationService = new ConsultationService(this);

        Intent intent = getIntent();

        selectedConsultation = (Consultation) intent.getSerializableExtra("consultation");


        btnGenerate.setOnClickListener(v -> {
            String dayText = edDay.getText().toString();
            String interText = edInter.getText().toString();

            if (selectedConsultation != null) {
                intervaloConsulta = Integer.parseInt(dayText);
                duracionConsulta = Integer.parseInt(interText);
                generateAppointments(LocalDate.now(), LocalDate.now().plusMonths(1), duracionConsulta);
            } else {
                Toast.makeText(this, "Consulta no encontrada o campos vacíos", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void generateAppointments(LocalDate startDate, LocalDate endDate, int duration) {
        TimetableService timetableService = new TimetableService(getApplicationContext());
        timetableService.getTimetablesByConsultationID(consultationId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Timetable> timetableList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TimetableString timetableString = snapshot.getValue(TimetableString.class);
                    timetableList.add(timetableString.convertToTimetable());
                }

                List<LocalDateTime> generatedAppointments = new ArrayList<>();

                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    for (Timetable timetable : timetableList) {
                        LocalTime startTime = timetable.getStartTime();
                        LocalTime endTime = timetable.getEndTime();

                        startTime = startTime.plusMinutes((duration - (startTime.getMinute() % duration)) % duration);

                        if (timetable.getDayOfWeek().equalsIgnoreCase(date.getDayOfWeek().toString())) {
                            while (startTime.plusMinutes(duration).isBefore(endTime) || (startTime.plusMinutes(duration).equals(endTime))) {
                                LocalDateTime appointmentDateTime = LocalDateTime.of(date, startTime);
                                generatedAppointments.add(appointmentDateTime);
                                startTime = startTime.plusMinutes(duration);
                            }
                        }
                    }
                }

                insertAppointments(consultationId, generatedAppointments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Error reading the database", error.toException());
            }
        });
    }

    private void insertAppointments(String consultationId, List<LocalDateTime> appointmentDates) {
        AppointmentService appointmentService = new AppointmentService(this);

        for (LocalDateTime appointmentDateTime : appointmentDates) {
            // Crea una nueva cita y establece los campos necesarios
            Appointment appointment = new Appointment();
            appointment.setActive(true);
            appointment.setAppointmentDate(LocalDate.from(appointmentDateTime.atZone(ZoneId.systemDefault()).toInstant()));
            appointment.setConsultation_id(consultationId);

            // Puedes establecer otros campos según sea necesario

            // Inserta la cita en la base de datos
            appointmentService.insertAppointment(appointment);
        }
    }
}