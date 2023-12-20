package es.iescarrillo.idoctor1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import es.iescarrillo.idoctor1.R;

public class Patient_View_Professional extends AppCompatActivity {

    ListView lvProfessional;
    Spinner spFilter;
    EditText etFilter;
    Button btnFilter, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_professional);
    }
}