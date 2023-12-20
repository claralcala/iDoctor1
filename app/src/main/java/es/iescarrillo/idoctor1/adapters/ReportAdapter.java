package es.iescarrillo.idoctor1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Report;

public class ReportAdapter extends ArrayAdapter<Report> {

    public ReportAdapter (Context context, List<Report> reports){
        super (context,0,reports);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Report report = getItem(position);

        // Reutiliza una vista existente o crea una nueva si es necesario
        if (convertView == null) {
            // Indicamos la vista plantilla
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_report, parent, false);
        }

        TextView tvAddTitle  = convertView.findViewById(R.id.tvAddTitle);
        TextView tvAddLink = convertView.findViewById(R.id.tvAddLink);

        tvAddTitle.setText(report.getTitle());
        tvAddLink.setText(report.getLink());

        return convertView;
    }
}
