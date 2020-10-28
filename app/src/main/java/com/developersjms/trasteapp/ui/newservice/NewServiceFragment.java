package com.developersjms.trasteapp.ui.newservice;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.developersjms.trasteapp.R;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class NewServiceFragment extends Fragment {

    Spinner spCapacidad, spCiudadCargue, spCiudadDescargue;
    EditText etDireccionCargue, etDireccionDescargue, etFecha, etHora, etDescripcion;
    Button btnConfirmar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_service, container, false);
        conectar(view);

        etHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String capacidadVehiculo = spCapacidad.getSelectedItem().toString();
                String ciudadCargue = spCiudadCargue.getSelectedItem().toString();
                String ciudadDescargue = spCiudadDescargue.getSelectedItem().toString();
                String direccionCargue = etDireccionCargue.getText().toString();
                String direccionDescargue = etDireccionDescargue.getText().toString();
                String fecha = etFecha.getText().toString();
                String hora = etHora.getText().toString();
                String descripcion = etDescripcion.getText().toString();

                String msg = "Si los datos estan correctos, confirma, de lo contrario edita los datos.\n\n\n" + "Capacidad Vehiculo: " + capacidadVehiculo +
                        "\nCiudad Cargue: " + ciudadCargue + "\nCiudad Descargue: " + ciudadDescargue + "\nDireccion cargue: " +
                        direccionCargue + "\nDireccion descargue: " + direccionDescargue + "\nFecha: " + fecha +
                        "\nHora: " + hora + "\nDescripcion: " + descripcion;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Verifica los datos");
                builder.setMessage(msg);

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.success(getContext(),"Se ha solicitado el trasteo correctamente, podra consultarlo en su historial", Toasty.LENGTH_LONG).show();
                        limpiarCampos();
                    }
                });

                builder.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        });

        return view;
    }

    private void conectar(View view) {
        spCapacidad = view.findViewById(R.id.tr_spCapacidadVehiculo);
        spCiudadCargue = view.findViewById(R.id.tr_spCiudadCargue);
        spCiudadDescargue = view.findViewById(R.id.tr_spCiudadDescargue);
        etDireccionCargue = view.findViewById(R.id.tr_etDireccionCargue);
        etDireccionDescargue = view.findViewById(R.id.tr_etDireccionDescargue);
        etFecha = view.findViewById(R.id.tr_etFecha);
        etHora = view.findViewById(R.id.tr_etHora);
        etDescripcion = view.findViewById(R.id.tr_etDescripcion);
        btnConfirmar = view.findViewById(R.id.tr_btnConfirmar);
    }

    private void limpiarCampos() {
        spCapacidad.setSelection(0);
        spCiudadCargue.setSelection(0);
        spCiudadDescargue.setSelection(0);
        etDireccionCargue.setText("");
        etDireccionDescargue.setText("");
        etFecha.setText("");
        etHora.setText("");
        etDescripcion.setText("");
    }

    private void showTimePickerDialog() {
        Calendar calendario = Calendar.getInstance();
        int h = calendario.get(Calendar.HOUR_OF_DAY);
        int m = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hora = hourOfDay + ":" + minute;
                etHora.setText(hora);
            }
        },h,m,android.text.format.DateFormat.is24HourFormat(getContext()));

        timePicker.show();
    }

    private void showDatePickerDialog() {
        Calendar calendario = Calendar.getInstance();
        int dd = calendario.get(Calendar.DAY_OF_MONTH);
        int mm = calendario.get(Calendar.MONTH);
        int yy = calendario.get(Calendar.YEAR);

        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int dayOfMonth, int monthOfYear, int year) {

                String fecha = twoDigits(year) + "/" + twoDigits(monthOfYear + 1)
                        + "/" + twoDigits(dayOfMonth);
                etFecha.setText(fecha);
            }
        }, yy, mm, dd);

        datePicker.show();
    }

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

}