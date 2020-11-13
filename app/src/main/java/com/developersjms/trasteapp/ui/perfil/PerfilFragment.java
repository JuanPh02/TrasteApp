package com.developersjms.trasteapp.ui.perfil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developersjms.trasteapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import es.dmoral.toasty.Toasty;

public class PerfilFragment extends Fragment {

    EditText etNombres, etApellidos, etFechaNacimiento, etEmail, etTelefono, etPassword;
    String nombres, apellidos, fechaNacimiento, email, telefono, pass;
    DatePickerDialog datePickerDialog;
    Button btnActualizar;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        conectar(view);
        recuperarPreferenciasUsuario();
        leerPerfil();

        etFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Actualizar Perfil");
                builder.setMessage("\n¿En realidad desea actualizar sus datos?");
                builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actualizarPerfil();
                    }
                });
                builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        });

        return view;
    }

    private void recuperarPreferenciasUsuario() {
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        email = preferences.getString("email","");
    }

    private void leerPerfil() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando Perfil...");
        progressDialog.show();
        leerPerfil("http://192.168.0.108/trasteapp/leer_perfil.php?email=" + email.trim());
    }

    private void actualizarPerfil() {
        nombres = etNombres.getText().toString().trim();
        apellidos = etApellidos.getText().toString().trim();
        fechaNacimiento = etFechaNacimiento.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        pass = etPassword.getText().toString().trim();
        actualizarPerfil("http://192.168.0.108/trasteapp/actualizar_usuario.php");
    }

    private void actualizarPerfil(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                llenarCampos();
                Toasty.success(getContext(), "Su perfil se ha actualizado correctamente.", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(),"ERROR -> " + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nombres", nombres);
                parametros.put("apellidos", apellidos);
                parametros.put("fechaNacimiento", fechaNacimiento);
                parametros.put("email", email);
                parametros.put("pass", pass);
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void leerPerfil(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    //res =jsonObject.getString("nombres");
                    nombres = jsonObject.getString("nombres");
                    apellidos = jsonObject.getString("apellidos");
                    fechaNacimiento = jsonObject.getString("fechaNacimiento");
                    email = jsonObject.getString("email");
                    telefono = jsonObject.getString("telefono");
                    pass = jsonObject.getString("pass");
                    llenarCampos();
                } catch (JSONException e) {
                    Toasty.warning(getContext(),"Error --> " + e.getMessage(), Toasty.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"ERROR -> " + error.toString(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void llenarCampos() {
        etNombres.setText(nombres);
        etApellidos.setText(apellidos);
        etFechaNacimiento.setText(fechaNacimiento);
        etEmail.setText(email);
        etTelefono.setText(telefono);
        etPassword.setText(pass);
    }

    private void showDatePickerDialog() {
        String[] parts = fechaNacimiento.split("-");
        int dd = Integer.parseInt(parts[2]);
        int mm = Integer.parseInt(parts[1]) - 1;
        int yy = Integer.parseInt(parts[0]);

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int dayOfMonth, int monthOfYear, int year) {

                String fecha = twoDigits(dayOfMonth) + "-" + twoDigits(monthOfYear + 1)
                + "-" + twoDigits(year);
                etFechaNacimiento.setText(fecha);
            }
        }, yy, mm, dd);

        datePickerDialog.show();
    }

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

    private void conectar(View view) {
        etNombres = view.findViewById(R.id.p_etNombres);
        etApellidos = view.findViewById(R.id.p_etApellidos);
        etFechaNacimiento = view.findViewById(R.id.p_etFechaNacimiento);
        etEmail = view.findViewById(R.id.p_etEmail);
        etTelefono = view.findViewById(R.id.p_etTelefono);
        etPassword = view.findViewById(R.id.p_etPassword);
        btnActualizar = view.findViewById(R.id.btnActualizar);
    }

}