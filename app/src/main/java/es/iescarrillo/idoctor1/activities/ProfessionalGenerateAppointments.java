package es.iescarrillo.idoctor1.activities;

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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    EditText edDuration;
    Button btnGenerate;

    int totalDuration;

    TimetableString ttableString;

    List<LocalDateTime> appointmentDates;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_generate_appointments);

        edDuration = findViewById(R.id.etInter);
        btnGenerate = findViewById(R.id.btnGenerate);

        consultationService = new ConsultationService(this);

        // Obtener la consulta seleccionada
        Intent intent = getIntent();
        selectedConsultation = (Consultation) intent.getSerializableExtra("consultation");

        if (selectedConsultation != null) {
            consultationId = selectedConsultation.getId();
        } else {
            Toast.makeText(this, "Consulta no encontrada", Toast.LENGTH_SHORT).show();
            finish(); // Finalizar la actividad si no hay consulta seleccionada
        }

        btnGenerate.setOnClickListener(v -> {
            String durationText = edDuration.getText().toString();
            int duration = Integer.parseInt(durationText);

            // Calcular la duración total de las citas (en minutos) para 30 días
            totalDuration = duration * 30 * 24 * 60; // días * horas/día * minutos/hora

            generateAppointments(selectedConsultation, totalDuration);
        });
    }

    private void generateAppointments(Consultation consultation, int duration) {
        TimetableService timetableService = new TimetableService(this);
        timetableService.getTimetablesByConsultationID(consultation.getId(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Timetable> timetables = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    ttableString = snapshot.getValue(TimetableString.class);
                    Timetable timetable= ttableString.convertToTimetable();
                    timetables.add(timetable);
                }

                appointmentDates = new ArrayList<>();
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = startDate.plusDays(30);

                for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    for (Timetable timetable : timetables) {
                        if (timetable.getDayOfWeek().equalsIgnoreCase(dayOfWeek.toString())) {
                            LocalTime startTime = timetable.getStartTime();
                            LocalTime endTime = timetable.getEndTime();
                            LocalDateTime appointmentDateTime = LocalDateTime.of(date, startTime);

                            // Generar citas hasta que alcancemos la duración total
                            while (appointmentDateTime.isBefore(LocalDateTime.of(date, endTime)) &&
                                    totalDuration > 0) {
                                if (isTimeSlotAvailable(appointmentDateTime, duration)) {
                                    appointmentDates.add(appointmentDateTime);
                                    totalDuration -= duration;
                                }
                                appointmentDateTime = appointmentDateTime.plusMinutes(duration);
                            }
                        }
                    }
                }

                insertAppointments(appointmentDates);


            }

            // Verificar si el intervalo de tiempo está disponible
            private boolean isTimeSlotAvailable(LocalDateTime appointmentDateTime, int duration) {
                for (LocalDateTime existingAppointment : appointmentDates) {
                    if (existingAppointment.isEqual(appointmentDateTime) ||
                            existingAppointment.plusMinutes(duration).isAfter(appointmentDateTime) &&
                                    existingAppointment.isBefore(appointmentDateTime.plusMinutes(duration))) {
                        return false; // El intervalo está ocupado
                    }
                }
                return true; // El intervalo está disponible
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Error al leer la base de datos", databaseError.toException());
            }
        });
    }

    private void insertAppointments(List<LocalDateTime> appointmentDates) {
        appointmentService = new AppointmentService(this);

        for (LocalDateTime appointmentDateTime : appointmentDates) {
            try {
                Appointment appointment = new Appointment();
                appointment.setActive(false);
                appointment.setAppointmentDate(appointmentDateTime.toLocalDate());
                appointment.setAppointmentTime(LocalTime.parse(appointmentDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
                appointment.setConsultation_id(consultationId);

                appointmentService.insertAppointment(appointment);
            } catch (Exception e) {
                Log.e("MyApp", "Error al insertar la cita", e);
            }
        }

        Toast.makeText(this, "Citas generadas correctamente", Toast.LENGTH_SHORT).show();
    }


}
