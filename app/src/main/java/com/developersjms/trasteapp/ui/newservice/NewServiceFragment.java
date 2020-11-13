package com.developersjms.trasteapp.ui.newservice;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developersjms.trasteapp.Ciudad;
import com.developersjms.trasteapp.Mudanza;
import com.developersjms.trasteapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class NewServiceFragment extends Fragment {

    Spinner spCapacidad, spCiudadPartida, spCiudadDestino;
    EditText etDireccionCargue, etDireccionDescargue, etFecha, etHora, etDescripcion;
    int idUsuario, idCiudadPartida, idCiudadDestino;
    String capacidadVehiculo, direccionPartida, direccionDestino, fecha, hora, fechaHora, descripcionObjetos;
    Button btnConfirmar;
    ArrayList<Ciudad> ciudades;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_service, container, false);
        conectar(view);
        recuperarPreferencias();
        requestQueue = Volley.newRequestQueue(getContext());
        leerCiudades();

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
                capacidadVehiculo = spCapacidad.getSelectedItem().toString();
                idCiudadPartida = spCiudadPartida.getSelectedItemPosition();
                idCiudadDestino = spCiudadDestino.getSelectedItemPosition();
                direccionPartida= etDireccionCargue.getText().toString();
                direccionDestino = etDireccionDescargue.getText().toString();
                fecha = etFecha.getText().toString();
                hora = etHora.getText().toString();
                fechaHora = fecha + " " + hora;
                descripcionObjetos = etDescripcion.getText().toString();

                if(spCapacidad.getSelectedItemPosition() != 0 && spCiudadPartida.getSelectedItemPosition() != 0 && spCiudadDestino.getSelectedItemPosition() != 0
                    && !direccionPartida.isEmpty() && !direccionDestino.isEmpty() && !fecha.isEmpty() && !hora.isEmpty() && !descripcionObjetos.isEmpty()) {
                    String msg = "Si los datos estan correctos, confirma, de lo contrario edita los datos.\n\n\n" + "Capacidad Vehiculo: " + capacidadVehiculo +
                            "\nCiudad Cargue: " + spCiudadPartida.getItemAtPosition(idCiudadPartida)  + "\nCiudad Descargue: " + spCiudadPartida.getItemAtPosition(idCiudadDestino) + "\nDireccion cargue: " +
                            direccionPartida + "\nDireccion descargue: " + direccionDestino + "\nFecha: " + fecha +
                            "\nHora: " + hora + "\nDescripcion: " + descripcionObjetos;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Verifica los datos");
                    builder.setMessage(msg);

                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            insertarMudanza();
                        }
                    });

                    builder.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                } else {
                    Toasty.warning(getContext(),"Por favor seleccione y llene todos los datos del formulario",Toasty.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void recuperarPreferencias() {
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        idUsuario = preferences.getInt("idUsuario",0);
    }

    private void leerCiudades() {
        ciudades = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( "http://192.168.0.108//trasteapp/leer_ciudades.php", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int id;
                String nombre;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getInt("idCiudad");
                        nombre = jsonObject.getString("nombre");
                        ciudades.add(new Ciudad(id,nombre));
                    }
                    //spCiudadDestino.setAdapter(new ArrayAdapter<Ciudad>(getContext(), android.R.layout.simple_spinner_item, ciudades));
                    cargarSpinnerCiudades();
                } catch (JSONException e) {
                    Toasty.warning(getContext(),"Error --> " + e.getMessage(), Toasty.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"ERROR -> " + error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    private void cargarSpinnerCiudades() {
        String[] array_ciudades = new String[ciudades.size()+1];
        array_ciudades[0] = "Seleccione la ciudad";
        for (int i = 0; i < ciudades.size(); i++) {
            array_ciudades[i+1] = String.valueOf(ciudades.get(i).getNombre());
        }
        spCiudadPartida.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,array_ciudades));
        spCiudadDestino.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,array_ciudades));
    }

    private void insertarMudanza() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Solicitando...");
        progressDialog.show();
        insertarMudanza("http://192.168.0.108//trasteapp/insertar_mudanza.php");
    }

    private void insertarMudanza(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Correct")) {
                    Toasty.success(getContext(), "Se ha solicitado el trasteo correctamente, podra consultarlo en su historial", Toast.LENGTH_LONG).show();
                    limpiarCampos();
                } else {
                    Toasty.error(getContext(), "Se ha producido un error al intentar solicitar el servicio", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(),"HP ERROR -> " + error.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("idUsuario", String.valueOf(idUsuario));
                parametros.put("idCiudadPartida", String.valueOf(idCiudadPartida));
                parametros.put("direccionPartida", direccionPartida);
                parametros.put("idCiudadDestino", String.valueOf(idCiudadDestino));
                parametros.put("direccionDestino", direccionDestino);
                parametros.put("fechaHora", fechaHora);
                parametros.put("descripcionObjetos", descripcionObjetos);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void conectar(View view) {
        spCapacidad = view.findViewById(R.id.tr_spCapacidadVehiculo);
        spCiudadPartida = view.findViewById(R.id.tr_spCiudadCargue);
        spCiudadDestino = view.findViewById(R.id.tr_spCiudadDescargue);
        etDireccionCargue = view.findViewById(R.id.tr_etDireccionCargue);
        etDireccionDescargue = view.findViewById(R.id.tr_etDireccionDescargue);
        etFecha = view.findViewById(R.id.tr_etFecha);
        etHora = view.findViewById(R.id.tr_etHora);
        etDescripcion = view.findViewById(R.id.tr_etDescripcion);
        btnConfirmar = view.findViewById(R.id.tr_btnConfirmar);
    }

    private void limpiarCampos() {
        spCapacidad.setSelection(0);
        spCiudadPartida.setSelection(0);
        spCiudadDestino.setSelection(0);
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
                String hora = hourOfDay + ":" + twoDigits(minute);
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

                String fecha = twoDigits(dayOfMonth) + "-" + twoDigits(monthOfYear + 1)
                        + "-" + twoDigits(year);
                etFecha.setText(fecha);
            }
        }, yy, mm, dd);

        datePicker.show();
    }

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

}