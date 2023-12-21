package es.iescarrillo.idoctor1.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Assessment;

public class AssessmentAdapter extends ArrayAdapter<Assessment> {
    public AssessmentAdapter(Context context, List<Assessment> assessments) {
        super(context, 0, assessments);
    }



    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        Assessment assessment = getItem(position);

        // Reutiliza una vista existente o crea una nueva si es necesario
        if (convertView == null) {
            // Indicamos la vista plantilla
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_assessment, parent, false);
        }

        // Accede al TextView en el diseño de cada elemento del ListView
        TextView tvUsernameAssessment = convertView.findViewById(R.id.tvUsernameAssessment);
        TextView tvTitleAssessmentPatient = convertView.findViewById(R.id.tvTitleAssessmentPatient);



        tvUsernameAssessment.setText("Usuario: " +assessment.getUsername());
        tvTitleAssessmentPatient.setText("Titulo: " +assessment.getTitle());


        return convertView;
    }
}
