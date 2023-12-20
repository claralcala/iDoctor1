package es.iescarrillo.idoctor1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Professional;

public class ProfessionalAdapter extends ArrayAdapter<Professional> {

    public ProfessionalAdapter(Context context, List<Professional> professionals) {
        super(context, 0, professionals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtén el objeto Contact en la posición actual
        Professional professional = getItem(position);

        // Reutiliza una vista existente o crea una nueva si es necesario
        if (convertView == null) {
            // Indicamos la vista plantilla
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_professional, parent, false);
        }

        // Accede al TextView en el diseño de cada elemento del ListView
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvSurname = convertView.findViewById(R.id.tvSurname);


        // Modificamos el texto a mostrar
        tvName.setText(professional.getName());
        tvSurname.setText(professional.getSurname());

        return convertView;
    }
}