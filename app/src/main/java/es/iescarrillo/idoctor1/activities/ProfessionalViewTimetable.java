package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.adapters.ConsultationAdapter;
import es.iescarrillo.idoctor1.adapters.TimetableAdapter;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Timetable;
import es.iescarrillo.idoctor1.models.TimetableString;
import es.iescarrillo.idoctor1.services.TimetableService;

public class ProfessionalViewTimetable extends AppCompatActivity {

    ListView lvTimetable;

    Button btnAdd, btnBack;

    TimetableService timetableService;

    Timetable timetable;

    TimetableAdapter timetableAdapter;

    ArrayList<Timetable> timetables;

    String consultationID;

    TimetableString timetableString;

   Consultation consul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_view_timetable);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id_ = sharedPreferences.getString("id", "");


        lvTimetable=findViewById(R.id.lvTimetables);

        btnBack=findViewById(R.id.btnBack);
        btnAdd=findViewById(R.id.btnAddTimetable);

        DatabaseReference dbDoctor = FirebaseDatabase.getInstance().getReference().child("timetable");

        timetableService = new TimetableService(getApplicationContext());

        timetables = new ArrayList<>();

        Intent intent = getIntent();

        consul= new Consultation();
        if (intent != null) {
            consul = (Consultation) intent.getSerializableExtra("consultation");
        }


        consultationID=consul.getId();

        timetable = new Timetable();

        timetableService.getTimetablesByConsultationID(consultationID, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timetables.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Convierte cada nodo de la base de datos a un objeto
                    timetableString= snapshot.getValue(TimetableString.class);
                    timetable=timetableString.convertToTimetable();
                    timetables.add(timetable);
                }

                // Una vez los datos añadidos a nuestra lista, se la pasamos al adaptador
                timetableAdapter = new TimetableAdapter(getApplicationContext(), timetables);
                lvTimetable.setAdapter(timetableAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvTimetable.setOnItemClickListener((parent, view, position, id) -> {
            timetable = (Timetable) parent.getItemAtPosition(position);
            Intent details = new Intent(this, ProfessionalTimetableDetails.class);
            details.putExtra("timetable", timetable);
            startActivity(details);
        });


        btnAdd.setOnClickListener(v -> {
            Intent add = new Intent (this, ProfessionalAddTimetable.class);
            add.putExtra("consultation_id", consultationID);
            startActivity(add);

        });


        btnBack.setOnClickListener(v -> {
            Intent back = new Intent(this, ProfessionalViewConsultations.class);
            startActivity(back);
        });



    }
}