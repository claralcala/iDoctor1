package es.iescarrillo.idoctor1.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import es.iescarrillo.idoctor1.R;

public class PatientAddRating extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_insert_rating);
        Spinner spinnerStars=findViewById(R.id.spinnerStars);
        ArrayAdapter<CharSequence> adapterSpinnerStars=ArrayAdapter.createFromResource(this,R.array.starsValues, android.R.layout.simple_spinner_item);
        adapterSpinnerStars.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStars.setAdapter(adapterSpinnerStars);
        Button btnRatingSave=findViewById(R.id.btnRatingSave);
        Button btnRatingCancel=findViewById(R.id.btnRatingCancel);
    }
}
