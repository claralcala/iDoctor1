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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.ProfessionalService;

/**
 * @author clara
 * Pantalla de editar el perfil del profesional
 */
public class ProfessionalEditProfile extends AppCompatActivity {


    ImageView ivPhoto;




    Button btnSave, btnCancel;

    StorageReference storageReference;

    String storage_path="photo/*";

    private static final int COD_SEL_IMAGE =300;

    private Uri image_url;
    String photo ="photo";


    private String url;

    String idd;

    EditText etName, etSurname, etCollegiate, etDescription, etPassword;
    Spinner spSpeciality;
    ProfessionalService profService;
    Professional prof;

    String profSpeciality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_edit_profile);

        //Inicializacion de componentes
        btnSave=findViewById(R.id.btnSaveProf);
        btnCancel=findViewById(R.id.btnCancel);
        etName=findViewById(R.id.etProfName);
        etSurname=findViewById(R.id.etProfSurname);
        etCollegiate=findViewById(R.id.etCollegiate);
        etDescription=findViewById(R.id.etProfDescription);
        etPassword=findViewById(R.id.etProfPassword);

        ivPhoto=findViewById(R.id.prof_photo);
        spSpeciality=findViewById(R.id.spnSpeciality);

        storageReference= FirebaseStorage.getInstance().getReference().child("photo/");





        Intent intent = getIntent();

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id = sharedPreferences.getString("id", "");

        //Comprobacion de rol
        if(!role.equals("PROFESSIONAL")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }


        profService=new ProfessionalService(getApplicationContext());


        //Lista de las especialidades para el spinner
        ArrayList<String> specialities= new ArrayList<>();
        specialities.add("general");
        specialities.add("fisioterapia");
        specialities.add("odontologia");

        //Adaptador para el spinner de especialidades
        ArrayAdapter sAdapter= new ArrayAdapter(ProfessionalEditProfile.this, android.R.layout.simple_spinner_dropdown_item, specialities);
        spSpeciality.setAdapter(sAdapter);

        spSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                profSpeciality=spSpeciality.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Nos traemos el profesional del intent
        prof = new Professional();
        if (intent != null) {
            prof = (Professional) intent.getSerializableExtra("professional");
        }


        //Ponemos los valores en los campos
        etName.setText(prof.getName());
        etSurname.setText(prof.getSurname());
        etCollegiate.setText(prof.getCollegiateNumber());
        etDescription.setText(prof.getDescription());

        //Ponemos la foto (si no tiene se le asigna una por defecto)
        if (prof.getPhoto() != null && !prof.getPhoto().isEmpty()) {
            Picasso.get().load(prof.getPhoto()).into(ivPhoto);
        } else {
            Picasso.get().load("https://img.freepik.com/vector-gratis/fondo-personaje-doctor_1270-84.jpg?w=740&t=st=1702906621~exp=1702907221~hmac=ab4e750f9abbc3639d96cc11482c3e2d4e2884af78c387638bd8b2c6c2ade362").into(ivPhoto);
        }



        //Cuando pulsamos la foto abrimos la galeria del móvil
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();

            }
        });

        //Boton guardar: actualizamos el profesional en Firebase
        btnSave.setOnClickListener(v -> {
            prof.setName(etName.getText().toString());
            prof.setSurname(etSurname.getText().toString());
            prof.setCollegiateNumber(etCollegiate.getText().toString());
            prof.setSpeciality(profSpeciality);

            prof.setDescription(etDescription.getText().toString());
            if (!TextUtils.isEmpty(etPassword.getText().toString())) {
                String encryptPassword = BCrypt.hashpw(etPassword.getText().toString(), BCrypt.gensalt(5));
                prof.setPassword(encryptPassword);
            } else {
                Toast.makeText(this, "La contraseña no puede estar vacia", Toast.LENGTH_SHORT).show();
                return; // Detener la ejecución del método si el campo de fecha de nacimiento está vacío
            }

                profService.updateProfessional(prof);
                uploadImage(prof.getId());



            Intent back = new Intent (this, ProfessionalProfileActivity.class);
            startActivity(back);
        });


        //Boton cancelar
        btnCancel.setOnClickListener(v -> {
            Intent back = new Intent (this, ProfessionalProfileActivity.class);
            startActivity(back);

        });


    }


    private void uploadPhoto(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }


    //Codigo para subir la foto
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



    //Método para obtener la URI de la imagen de un ImageView
    private Uri getImageUri(Context context, ImageView imageView, String name) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, name, null);
        return Uri.parse(path);
    }

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

                           prof.setPhoto(url);
                           profService.updateProfessional(prof);
                        }
                    });
                }
                Toast.makeText(getApplicationContext(), "Guardando profesional", Toast.LENGTH_SHORT).show();
            }
        });
    }
}