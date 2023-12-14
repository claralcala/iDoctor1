package es.iescarrillo.idoctor1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import es.iescarrillo.idoctor1.R;
import es.iescarrillo.idoctor1.models.Appointment;
import es.iescarrillo.idoctor1.models.Consultation;
import es.iescarrillo.idoctor1.models.Patient;
import es.iescarrillo.idoctor1.services.PatientService;


public class AppointmentAdapter extends ArrayAdapter<Appointment> {




    public AppointmentAdapter(Context context, List<Appointment> appointments) {
        super(context, 0, appointments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtén el objeto Contact en la posición actual
        Appointment app = getItem(position);

        // Reutiliza una vista existente o crea una nueva si es necesario
        if (convertView == null) {
            // Indicamos la vista plantilla
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_appointment, parent, false);
        }

        // Accede al TextView en el diseño de cada elemento del ListView
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvHour = convertView.findViewById(R.id.tvHour);



        tvDate.setText("Fecha: " +app.getAppointmentDate());
        tvHour.setText("Hora: " +app.getAppointmentTime());


        return convertView;
    }


}
