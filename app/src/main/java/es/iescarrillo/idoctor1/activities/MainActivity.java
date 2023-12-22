package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.PatientService;
import es.iescarrillo.idoctor1.services.ProfessionalService;

/**
 * @author clara
 * Pantalla principal - de login
 *
 */
public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    TextView tvError;

    ProfessionalService profService;

    Button btnLogin, btnRegister;

    Professional prof;
    PatientService patService;

    Patient pat;

    boolean professionalFound;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializamos componentes
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);
        etUsername= findViewById(R.id.etUserName);
        etPassword=findViewById(R.id.etPassword);
        tvError=findViewById(R.id.tvError);

        //Inicializamos servicios
        profService= new ProfessionalService(getApplicationContext());
        patService = new PatientService(getApplicationContext());

        //Acción del botón registrar
        btnRegister.setOnClickListener(v -> {
            Intent register= new Intent(this, RegisterActivity.class);
            startActivity(register);
        });

        //Accion del botón login
        btnLogin.setOnClickListener(v -> {

            login();



        });



    }

    //Método para loguearse
    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        //Comprobamos que los campos no estén vacíos. Si no lo están, comprobamos que ese profesional exista
        if (!username.isEmpty() && !password.isEmpty()) {
            checkProfessional(username, password);
        } else {
            tvError.setText("Por favor, introduce un nombre de usuario y una contraseña.");
        }
    }

    //Método para comprobar que exista ese profesional
    private void checkProfessional(String username, String password) {
        profService.getProfessionalByUsername(username, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Professional prof = dataSnapshot.getValue(Professional.class);
                        if (prof != null) {
                            if (BCrypt.checkpw(password, prof.getPassword())) {
                                loginUser(prof.getUsername(), prof.getId(), prof.getRole());
                                return;
                            } else {
                                tvError.setText("Contraseña no válida para el profesional.");
                                return;
                            }
                        }
                    }
                }

                //Si no se encuentra en profesionales, verifica en pacientes
                checkPatient(username, password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvError.setText("Error al acceder a la base de datos.");
            }
        });
    }

    //Método para comprobar que existe paciente
    private void checkPatient(String username, String password) {
        patService.getPatientByUsername(username, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Patient pat = dataSnapshot.getValue(Patient.class);
                        if (pat != null) {
                            if (BCrypt.checkpw(password, pat.getPassword())) {
                                loginUser(pat.getUsername(), pat.getId(), pat.getRole());
                                return;
                            } else {
                                tvError.setText("Contraseña no válida para el paciente.");
                                return;
                            }
                        }
                    }
                }
                //Si tampoco lo encuentra en pacientes, saca un mensaje de usuario no válido
                tvError.setText("Usuario no válido");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvError.setText("Error al acceder a la base de datos");
            }
        });
    }


    private void loginUser(String username, String id, String role) {
        sharedPreferences = getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Al loguearnos, creamos las variables de sesión y llevamos a las pantallas adecuadas
        editor.putBoolean("login", true);
        editor.putString("user", username);
        editor.putString("id", id);
        editor.putString("role", role);
        editor.apply();

        tvError.setText("Login correcto");

        Intent intent;
        if ("PROFESSIONAL".equals(role)) {
            intent = new Intent(MainActivity.this, ProfessionalMainActivity.class);
        } else {
            intent = new Intent(MainActivity.this, Patient_Main_Activity.class);
        }
        startActivity(intent);
    }

}