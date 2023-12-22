package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.ConsultationService;

/**
 * @author clara
 * Pantalla para editar consulta
 */
public class ProfessionalEditConsultation extends AppCompatActivity {


    EditText etAddress, etCity, etMail, etPhone, etPhoneAux;

    Button btnSave, btnCancel;

    Consultation cons;

    ConsultationService consService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_edit_consultation);

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


        //Inicializacion de componentes
        etAddress=findViewById(R.id.etConsAddress);
        etCity=findViewById(R.id.etConsCity);
        etMail=findViewById(R.id.etConsMail);
        etPhone=findViewById(R.id.etConsPhone);
        etPhoneAux=findViewById(R.id.etConsPhoneAux);
        btnSave=findViewById(R.id.btnSaveCons);
        btnCancel=findViewById(R.id.btnCancel);

        Intent intent= getIntent();

        consService=new ConsultationService(getApplicationContext());

        //Nos traemos la consulta en el intent
       cons = new Consultation();
        if (intent != null) {
            cons = (Consultation) intent.getSerializableExtra("consultation");
        }


        //Ponemos datos en los campos de textos
        etAddress.setText(cons.getAddress());
        etCity.setText(cons.getCity());
        etMail.setText(cons.getEmail());
        etPhone.setText(cons.getPhone());
        etPhoneAux.setText(cons.getPhoneAux());


        //Accion del boton guardar. Actualizamos la consulta en Firebase
        btnSave.setOnClickListener(v -> {
            cons.setAddress(etAddress.getText().toString());
            cons.setCity(etCity.getText().toString());
            cons.setEmail(etMail.getText().toString());
            cons.setPhone(etPhone.getText().toString());
            cons.setPhoneAux(etPhoneAux.getText().toString());

            consService.updateConsultation(cons);

            Intent back = new Intent(this, ProfessionalViewConsultations.class);
            startActivity(back);

        });

//Accion del boton cancelar
        btnCancel.setOnClickListener(v -> {
            onBackPressed();
        });



    }
}