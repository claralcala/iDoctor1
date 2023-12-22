package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.services.PatientService;
/**
 * @author Jesús
 *
 * Pantalla para editar los detalles del paciente
 */
public class Patient_Edit_Profile extends AppCompatActivity {

    EditText etName, etSurname, etPassword, etDNI, etEmail, etNumber;
    CheckBox cbHealthInsurance;
    Button btnAccept, btnBack;
    ImageView ivPhoto;
    PatientService patientService;
    Patient patient;
    StorageReference storageReference;
    private static final int COD_SEL_IMAGE =300;
    private Uri image_url;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_profile);

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id = sharedPreferences.getString("id", "");

        //Comprobamos que el rol del usuario sea paciente
        if(!role.equals("PATIENT")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }

        //Inicializamos componentes
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etPassword = findViewById(R.id.etPassword);
        etDNI = findViewById(R.id.etDNI);
        etEmail = findViewById(R.id.etEmail);
        etNumber = findViewById(R.id.etNumber);

        cbHealthInsurance = findViewById(R.id.cbHealthInsurance);

        btnAccept = findViewById(R.id.btnAccept);
        btnBack = findViewById(R.id.btnBack);

        ivPhoto = findViewById(R.id.ivPhoto);

        storageReference= FirebaseStorage.getInstance().getReference().child("photo/");

        patientService = new PatientService(getApplicationContext());

        //Recuperamos los datos de un objeto paciente
        Intent intent = getIntent();

        patient = new Patient();
        if(intent != null){
            patient = (Patient) intent.getSerializableExtra("patient");
        }

        etName.setText(patient.getName());
        etSurname.setText(patient.getSurname());
        etDNI.setText(patient.getDni());
        etEmail.setText(patient.getEmail());
        etNumber.setText(patient.getPhone());

        if(patient.isHealthInsurance()){
            cbHealthInsurance.setChecked(true);
        }else{
            cbHealthInsurance.setChecked(false);
        }

        //Cargamos una foto por defecto en el paciente si no tiene foto subida
        if (patient.getPhoto() != null && !patient.getPhoto().isEmpty()) {
            Picasso.get().load(patient.getPhoto()).into(ivPhoto);
        } else {
            Picasso.get().load("https://img.freepik.com/vector-gratis/ilustracion-concepto-dolor-alimentos_114360-16553.jpg?w=826&t=st=1702917731~exp=1702918331~hmac=444109e17f3a8f166c50ae83ebd1e70458916c93b034d22f40e0ba26fb7af18a").into(ivPhoto);
        }

        //Al hacer click sobre la foto nos permite cambiarla
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();

            }
        });

        //Boton para aceptar la edicion del perfil
        btnAccept.setOnClickListener(v -> {
            patient.setName(etName.getText().toString());
            patient.setSurname(etSurname.getText().toString());
            patient.setDni(etDNI.getText().toString());
            patient.setEmail(etEmail.getText().toString());
            patient.setPhone(etNumber.getText().toString());

            if (!TextUtils.isEmpty(etPassword.getText().toString())) {
                String encryptPassword = BCrypt.hashpw(etPassword.getText().toString(), BCrypt.gensalt(5));
                patient.setPassword(encryptPassword);
            } else {
                Toast.makeText(this, "La contraseña no puede estar vacia", Toast.LENGTH_SHORT).show();
                return; // Detener la ejecución del método si el campo de fecha de nacimiento está vacío
            }

            patientService.updatePatient(patient);
            uploadImage(patient.getId());

            Intent accept = new Intent(this, Patient_View_Profile.class);
            startActivity(accept);

        });
        //Boton para volver a la activity anterior
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    //Metodo para cargar una nueva foto
    private void uploadPhoto(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if(requestCode==COD_SEL_IMAGE){
                image_url=data.getData();
                ivPhoto.setImageURI(image_url);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //Metodo par aconseguir la URI de la imagen
    private Uri getImageUri(Context context, ImageView imageView, String name) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, name, null);
        return Uri.parse(path);
    }

    //Metodo para actualizar la foto del usuario
    private void uploadImage(String idProf){
        Uri file = getImageUri(this, ivPhoto, idProf);
        StorageReference storageRefProfessional = storageReference.child(idProf);
        storageRefProfessional.putFile(file).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                if (uriTask.isSuccessful()) {
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url= uri.toString();

                            patient.setPhoto(url);
                            patientService.updatePatient(patient);
                        }
                    });
                }
                Toast.makeText(getApplicationContext(), "Guardando profesional", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
