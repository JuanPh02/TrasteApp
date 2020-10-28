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
import android.widget.RadioGroup;
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
import com.developersjms.trasteapp.MainActivity;
import com.developersjms.trasteapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PerfilFragment extends Fragment {

    EditText etNombres, etApellidos, etFechaNacimiento, etEmail, etTelefono, etPassword;
    String nombres, apellidos, fechaNacimiento, email, telefono, pass;
    Button btnActualizar;
    private ProgressDialog progress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        /*realtimeDatabase = new RealtimeDatabase(getContext());
        user = FirebaseAuth.getInstance().getCurrentUser();*/
        conectar(view);
        recuperarPreferenciasLogin();
        //Toast.makeText(getContext(),"email -> " + email,Toast.LENGTH_LONG).show();
        //leerPerfil("http://192.168.0.108/trasteapp/leer_perfil.php?email=" + email.trim());

        etFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        /*if (realtimeDatabase.checkConnection()) {
            if (user != null) {
                uploadProfile();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Conexion");
            builder.setMessage("No se pueden cargar los datos \nEstado de la conexión: " + realtimeDatabase.getStateConnection() + "\nPor favor conectese a una red de internet");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }*/

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("¿Actualizar Datos?");
                    builder.setMessage("\nEstá seguro de que desea actualizar los datos?");
                    builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                actualizar();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void recuperarPreferenciasLogin() {
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        //etEmail.setText(preferences.getString("email",""));
        email = preferences.getString("email","");
        //etPassword.setText(preferences.getString("password",""));
    }

    private void leerPerfil(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //etNombres.setText(jsonObject.getString("nombres"));
                        //etApellidos.setText(jsonObject.getString("apellidos"));
                        //etFechaNacimiento.setText(jsonObject.getString("fechaNacimiento"));
                        //etEmail.setText(jsonObject.getString("email"));
                        //etTelefono.setText(jsonObject.getString("telefono"));
                        //etPassword.setText(jsonObject.getString("pass"));
                        nombres = jsonObject.getString("nombres");
                        Toast.makeText(getContext(),"nombres -> " + nombres, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(),"ERROR -> " + e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"ERROR -> " + error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void actualizar() throws Exception {
        nombres = etNombres.getText().toString().trim();
        apellidos = etApellidos.getText().toString().trim();
        fechaNacimiento = etFechaNacimiento.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        telefono = etTelefono.getText().toString().trim();
        pass = etPassword.getText().toString().trim();

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

    /*private void uploadProfile() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando datos del perfil");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        final int totalProgressTime = 1;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;
                cargarDatos(user.getUid());
                while (jumpTime < totalProgressTime) {
                    try {
                        jumpTime++;
                        sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                progress.cancel();
            }
        };
        t.start();
    }*/

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
                etFechaNacimiento.setText(fecha);
            }
        }, yy, mm, dd);

        datePicker.show();
    }

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

    /*public void cargarDatos(String Uid) {
        RealtimeDatabase realtimeDatabase = new RealtimeDatabase(getContext());
        try {

            DatabaseReference ref = realtimeDatabase.dbRef.child("Usuarios").child(Uid);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String tipoDoc, docId, nombres, fechaNac, telefono, email, contraseña;
                        tipoDoc = dataSnapshot.child("tipoDoc").getValue().toString();
                        docId = dataSnapshot.child("docId").getValue().toString();
                        nombres = dataSnapshot.child("nombres").getValue().toString();
                        fechaNac = dataSnapshot.child("fechaNac").getValue().toString();
                        telefono = dataSnapshot.child("telefono").getValue().toString();
                        email = dataSnapshot.child("email").getValue().toString();
                        if (!tipoDoc.isEmpty()) {
                            int refTipoDoc = (tipoDoc.equals("Cedula")) ? R.id.rbCedula : R.id.rbNit;
                            rgTipoDoc.check(refTipoDoc);
                        }

                        et_doc.setText(docId);
                        et_nombre.setText(nombres);
                        et_fecha.setText(fechaNac);
                        et_telefono.setText(telefono);
                        et_email.setText(email);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            Toasty.error(getContext(),"ERROR " + e.getMessage(),Toasty.LENGTH_LONG).show();
        }
    }*/
}