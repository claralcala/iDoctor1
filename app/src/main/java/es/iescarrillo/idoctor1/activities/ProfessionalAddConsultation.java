package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.services.ConsultationService;

/**
 * @author clara
 * Pantalla para añadir consultas
 */
public class ProfessionalAddConsultation extends AppCompatActivity {

    EditText etAddress, etCity, etEmail, etPhone, etPhoneAux;

    Button btnSave, btnCancel;


    ConsultationService consService;

    Consultation cons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_add_consultation);


        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id = sharedPreferences.getString("id", "");

        //Comprobacion de roles
        if(!role.equals("PROFESSIONAL")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        //Inicializamos componentes
        etAddress=findViewById(R.id.etConsAddress);
        etCity=findViewById(R.id.etConsCity);
        etEmail=findViewById(R.id.etConsMail);
        etPhone=findViewById(R.id.etConsPhone);
        etPhoneAux=findViewById(R.id.etConsPhoneAux);

        btnSave=findViewById(R.id.btnSaveCons);
        btnCancel=findViewById(R.id.btnCancel);


        consService=new ConsultationService(getApplicationContext());

        //Accion del boton guardar, inserta una consulta
        btnSave.setOnClickListener(v -> {
            cons= new Consultation();
            cons.setAddress(etAddress.getText().toString());
            cons.setCity(etCity.getText().toString());
            cons.setEmail(etEmail.getText().toString());
            cons.setPhone(etPhone.getText().toString());
            cons.setPhoneAux(etPhoneAux.getText().toString());
            cons.setProfessional_id(id);
            consService.insertConsultation(cons);

            Intent back = new Intent(this, ProfessionalViewConsultations.class);
            startActivity(back);
        });


        //Accion del boton volver
        btnCancel.setOnClickListener(v -> {
            Intent back = new Intent(this, ProfessionalViewConsultations.class);
            startActivity(back);
        });





    }
}