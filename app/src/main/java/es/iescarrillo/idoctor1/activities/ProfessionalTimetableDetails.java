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
import es.iescarrillo.idoctor1.models.Timetable;
import es.iescarrillo.idoctor1.services.TimetableService;

/**
 * @author clara
 * Pantalla para ver los detalles del horario
 */
public class ProfessionalTimetableDetails extends AppCompatActivity {

    TextView tvDay, tvStart, tvEnd;
    Button btnEdit, btnBack, btnDelete;

    TimetableService timetableService;

    Timetable timetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_timetable_details);

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

        //Inicializamos componentes
        tvDay=findViewById(R.id.tvDay);
        tvStart=findViewById(R.id.tvStartTime);
        tvEnd=findViewById(R.id.tvendTime);
        btnEdit=findViewById(R.id.btnEdit);
        btnDelete=findViewById(R.id.btnDelete);
        btnBack=findViewById(R.id.btnCancel);

        Intent intent = getIntent();

        //Servicio
        timetableService=new TimetableService(getApplicationContext());

        //Nos traemos el objeto en el intent
        timetable = new Timetable();
        if (intent != null) {
           timetable = (Timetable) intent.getSerializableExtra("timetable");
        }


        tvDay.setText(timetable.getDayOfWeek());
        tvStart.setText(timetable.getStartTime().toString());
        tvEnd.setText(timetable.getEndTime().toString());

        //Accion del boton editar
        btnEdit.setOnClickListener(v -> {
            Intent edit = new Intent(this, ProfessionalEditTimetable.class);
            edit.putExtra("timetable", timetable);
            startActivity(edit);
        });

        //Accion del boton volver
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnDelete.setOnClickListener(v -> {

            timetableService.deleteTimetable(timetable.getId());
            onBackPressed();
        });







    }
}