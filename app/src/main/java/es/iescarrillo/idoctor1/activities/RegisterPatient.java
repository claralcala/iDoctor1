package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.services.PatientService;
import es.iescarrillo.idoctor1.services.ProfessionalService;

/**
 * @author clara
 * Pantalla para registrarse como paciente
 */
public class RegisterPatient extends AppCompatActivity{

        Button btnSave,btnBack;

        EditText etName,etSurname,etUsername,etPassword,etDNI,etEmail,etPhone;

        CheckBox cbHealthInsurance;

        PatientService pService;

        ProfessionalService profService;

        Patient pat;

        TextView tvError;


@Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        //Inicializamos componentes
        btnSave=findViewById(R.id.btnSavePatient);
        btnBack=findViewById(R.id.btnCancel);

        etName=findViewById(R.id.etPatientName);
        etSurname=findViewById(R.id.etPatientName);
        etUsername=findViewById(R.id.etPatientUserName);
        etPassword=findViewById(R.id.etPatientPassword);
        etDNI=findViewById(R.id.etDNI);
        etEmail=findViewById(R.id.etPatientMail);
        etPhone=findViewById(R.id.etTelephone);



        cbHealthInsurance=findViewById(R.id.checkBoxInsurance);

        //Accion del botón volver
        btnBack.setOnClickListener(v->{
        Intent back=new Intent(this,RegisterActivity.class);
        startActivity(back);
        });

        //Inicializamos los servicios
        pService=new PatientService(getApplicationContext());
        profService=new ProfessionalService(getApplicationContext());

        pat=new Patient();

        btnSave.setOnClickListener(v->{

        String username=etUsername.getText().toString();

        pService.getPatientByUsername(username,new ValueEventListener(){
@Override
public void onDataChange(@NonNull DataSnapshot snapshot){
        if(snapshot.exists()){
        //El nombre de usuario ya está en uso por un paciente, muestra un Toast y no registra al paciente

        Toast.makeText(RegisterPatient.this,"El nombre de usuario ya está en uso",Toast.LENGTH_SHORT).show();


        }else{
        //El nombre de usuario no existe en pacientes, verifica en profesionales
        checkProfessionalUsername(username);
        }
        }

@Override
public void onCancelled(@NonNull DatabaseError error){

        }
        });

        });

        }

private void checkProfessionalUsername(String username){
        //Comprobamos si el nombre de usuario ya existe en profesionales
        profService.getProfessionalByUsername(username,new ValueEventListener(){
@Override
public void onDataChange(@NonNull DataSnapshot profSnapshot){
        if(profSnapshot.exists()){


        //El nombre de usuario ya está en uso por un profesional, muestra un Toast y no registres al paciente

        Toast.makeText(RegisterPatient.this,"El nombre de usuario ya está en uso",Toast.LENGTH_SHORT).show();


        }else{
        //El nombre de usuario no existe en profesionales ni en pacientes, permite el registro del paciente
        registerPatient();
        }
        }

@Override
public void onCancelled(@NonNull DatabaseError error){

        }
        });
        }

private void registerPatient(){
        //Finalmente registramos al paciente
        pat.setName(etName.getText().toString());
        pat.setSurname(etSurname.getText().toString());
        pat.setDni(etDNI.getText().toString());
        pat.setEmail(etEmail.getText().toString());
        pat.setPhone(etPhone.getText().toString());

        if(cbHealthInsurance.isChecked()){
        pat.setHealthInsurance(true);
        }else{
        pat.setHealthInsurance(false);
        }
        pat.setUsername(etUsername.getText().toString());
        pat.setRole("PATIENT");
        String encryptPassword=BCrypt.hashpw(etPassword.getText().toString(),BCrypt.gensalt(5));
        pat.setPassword(encryptPassword);


        pService.insertPatient(pat);


        Toast.makeText(RegisterPatient.this,"Registro correcto",Toast.LENGTH_SHORT).show();


        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

        }

        }
