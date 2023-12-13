package es.iescarrillo.idoctor1.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.List;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Consultation;

public class ConsultationAdapter extends ArrayAdapter<Consultation> {

    public ConsultationAdapter(Context context, List<Consultation> consultations) {
        super(context, 0, consultations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtén el objeto Contact en la posición actual
        Consultation consultation = getItem(position);

        // Reutiliza una vista existente o crea una nueva si es necesario
        if (convertView == null) {
            // Indicamos la vista plantilla
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_consultation, parent, false);
        }

        // Accede al TextView en el diseño de cada elemento del ListView
        TextView tvAddress = convertView.findViewById(R.id.tvAddress);
        TextView tvCity = convertView.findViewById(R.id.tvCity);


        // Modificamos el texto a mostrar
        tvAddress.setText(consultation.getAddress());
        tvCity.setText(consultation.getCity());

        return convertView;
    }
}
