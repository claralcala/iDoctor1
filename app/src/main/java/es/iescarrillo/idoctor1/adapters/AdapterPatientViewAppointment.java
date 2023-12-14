package es.iescarrillo.idoctor1.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import es.iescarrillo.idoctor1.models.Appointment;

public class AdapterPatientViewAppointment extends ArrayAdapter<Appointment> {
    public AdapterPatientViewAppointment(Context context, List<Appointment> patientAppointment) {
        super(context, 0, patientAppointment);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
