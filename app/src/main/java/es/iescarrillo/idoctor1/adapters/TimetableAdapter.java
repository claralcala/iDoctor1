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
import es.iescarrillo.idoctor1.models.Timetable;

public class TimetableAdapter extends ArrayAdapter<Timetable> {

    public TimetableAdapter(Context context, List<Timetable> timetables) {
        super(context, 0, timetables);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtén el objeto Contact en la posición actual
        Timetable timetable = getItem(position);

        // Reutiliza una vista existente o crea una nueva si es necesario
        if (convertView == null) {
            // Indicamos la vista plantilla
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_timetable, parent, false);
        }

        TextView tvDay=convertView.findViewById(R.id.tvDayOfWeek);


        tvDay.setText(timetable.getDayOfWeek());

        return convertView;
    }

}
