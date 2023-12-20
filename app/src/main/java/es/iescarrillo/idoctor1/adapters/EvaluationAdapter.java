package es.iescarrillo.idoctor1.adapters;


import android.content.Context;
import android.widget.ArrayAdapter;
import es.iescarrillo.idoctor1.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.List;

import es.iescarrillo.idoctor1.models.Evaluation;

public class EvaluationAdapter extends ArrayAdapter<Evaluation>{

    public EvaluationAdapter (Context context, List<Evaluation> evaluations){
        super(context,0,evaluations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Evaluation evaluation = getItem(position);

        // Reutiliza una vista existente o crea una nueva si es necesario
        if (convertView == null) {
            // Indicamos la vista plantilla
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_evaluation, parent, false);
        }

        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvExploration = convertView.findViewById(R.id.tvExploration);


        tvDescription.setText("Descripcion: " + evaluation.getDescription());
        tvExploration.setText("Exploracion: " + evaluation.getExploration());



        return convertView;
    }
}
