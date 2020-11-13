package com.developersjms.trasteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class RegistroActivity extends AppCompatActivity {

    EditText etNombres, etApellidos, etFechaNacimiento, etEmail, etTelefono, etPassword;
    Button btnBack, btnRegistrarse;
    String nombres, apellidos, fechaNacimiento, email, telefono, pass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        conectar();

        etFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombres = etNombres.getText().toString().trim();
                apellidos = etApellidos.getText().toString().trim();
                fechaNacimiento = etFechaNacimiento.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                telefono = etTelefono.getText().toString().trim();
                pass = etPassword.getText().toString().trim();

                if(!nombres.isEmpty() && !apellidos.isEmpty() && !fechaNacimiento.isEmpty() && !email.isEmpty()
                        && !telefono.isEmpty() && !pass.isEmpty()) {
                    registrarUsuario();
                } else {
                    Toasty.warning(getApplicationContext(),"Hay campos vacios. Debe llenar todos los campos",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registrarUsuario() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando...");
        progressDialog.show();
        registrarUsuario("http://192.168.0.108/trasteapp/insertar_usuario.php");
    }

    private void registrarUsuario(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Correct")) {
                    Toasty.success(getApplicationContext(), "Se ha realizado el registro correctamente. Ya puede loguearse", Toast.LENGTH_LONG).show();
                    limpiarCampos();
                } else {
                    Toasty.error(getApplicationContext(), "Se ha producido un error al intentar realizar el registro", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(),"ERROR -> " + error.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nombres", nombres);
                parametros.put("apellidos", apellidos);
                parametros.put("fechaNacimiento", fechaNacimiento);
                parametros.put("email", email);
                parametros.put("telefono", telefono);
                parametros.put("pass", pass);
                return parametros;
                }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showDatePickerDialog() {
        Calendar calendario = Calendar.getInstance();
        int dd = calendario.get(Calendar.DAY_OF_MONTH);
        int mm = calendario.get(Calendar.MONTH);
        int yy = calendario.get(Calendar.YEAR);

        DatePickerDialog datePicker = new DatePickerDialog(RegistroActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    private void limpiarCampos() {
        etNombres.setText("");
        etApellidos.setText("");
        etFechaNacimiento.setText("");
        etEmail.setText("");
        etTelefono.setText("");
        etPassword.setText("");
    }

    private void conectar() {
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        etEmail = findViewById(R.id.etEmail);
        etTelefono = findViewById(R.id.etTelefono);
        etPassword = findViewById(R.id.etPassword);
        btnBack = findViewById(R.id.btnBack);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
    }
}