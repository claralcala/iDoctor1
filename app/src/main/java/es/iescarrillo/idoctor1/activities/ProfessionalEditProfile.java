package es.iescarrillo.idoctor1.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Professional;
import es.iescarrillo.idoctor1.services.ProfessionalService;

public class ProfessionalEditProfile extends AppCompatActivity {


    ImageView ivPhoto;


    Button btnSave, btnCancel, btnPhoto, btnDeletePhoto;

    StorageReference storageReference;

    String storage_path="photo/*";

    private static final int COD_SEL_IMAGE =300;

    private Uri image_url;
    String photo ="photo";

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

        btnSave=findViewById(R.id.btnSaveProf);
        btnCancel=findViewById(R.id.btnCancel);
        etName=findViewById(R.id.etProfName);
        etSurname=findViewById(R.id.etProfSurname);
        etCollegiate=findViewById(R.id.etCollegiate);
        etDescription=findViewById(R.id.etProfDescription);
        etPassword=findViewById(R.id.etProfPassword);

        spSpeciality=findViewById(R.id.spnSpeciality);

        storageReference= FirebaseStorage.getInstance().getReference();

        btnPhoto=findViewById(R.id.btn_photo);
        btnDeletePhoto=findViewById(R.id.btn_remove_photo);



        Intent intent = getIntent();

        //Variables de sesión
        SharedPreferences sharedPreferences= getSharedPreferences("PreferencesDoctor", Context.MODE_PRIVATE);
        String username= sharedPreferences.getString("user", "");
        String role = sharedPreferences.getString("role", "");
        Boolean login = sharedPreferences.getBoolean("login", true);
        String id = sharedPreferences.getString("id", "");

        if(!role.equals("PROFESSIONAL")){


            sharedPreferences.edit().clear().apply();
            Intent backMain = new Intent(this, MainActivity.class);
            startActivity(backMain);

        }


        profService=new ProfessionalService(getApplicationContext());


        ArrayList<String> specialities= new ArrayList<>();
        specialities.add("general");
        specialities.add("fisioterapia");
        specialities.add("odontologia");

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


        prof = new Professional();
        if (intent != null) {
            prof = (Professional) intent.getSerializableExtra("professional");
        }


        etName.setText(prof.getName());
        etSurname.setText(prof.getSurname());
        etCollegiate.setText(prof.getCollegiateNumber());
        etDescription.setText(prof.getDescription());



        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();

            }
        });

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



            Intent back = new Intent (this, ProfessionalProfileActivity.class);
            startActivity(back);
        });


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if(requestCode==COD_SEL_IMAGE){
                image_url=data.getData();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upPhoto(Uri image_url){
        String rute_storage_photo = storage_path + "" + photo+ "" + prof.getId() + ""+ idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", download_uri);

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfessionalEditProfile.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}