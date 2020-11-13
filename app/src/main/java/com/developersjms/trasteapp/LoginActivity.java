package com.developersjms.trasteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin, btnRegistrarse;
    String nombres, apellidos, fechaNacimiento, email, telefono, pass, lastSesion;
    int id;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        conectar();
        recuperarPreferencias();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();
                pass = etPassword.getText().toString().trim();
                if (!email.isEmpty() && !pass.isEmpty()) {
                    validarUsuario();
                } else {
                    Toasty.warning(getApplicationContext(),"No se permiten campos vacios",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });
    }

    private void validarUsuario() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Validando...");
        progressDialog.show();
        validarUsuario("http://192.168.0.108/trasteapp/validar_usuario.php");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, 200);
    }

    private void validarUsuario(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()) {
                    leerPerfil("http://192.168.0.108/trasteapp/leer_perfil.php?email=" + email.trim());
                    updateLastSesion();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
                //progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"ERROR -> " + error.toString(),Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", email);
                parametros.put("pass", pass);
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateLastSesion() {
        Date date = new Date();
        //Obtener hora y fecha y salida con formato:
        DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        lastSesion = hourdateFormat.format(date);
        updateLastSesion("http://192.168.0.108/trasteapp/last_sesion.php");
    }

    private void updateLastSesion(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"ERROR -> " + error.toString(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("email", email);
                parametros.put("lastSesion", lastSesion);
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void leerPerfil(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    //res =jsonObject.getString("nombres");
                    id = jsonObject.getInt("idUsuario");
                    nombres = jsonObject.getString("nombres");
                    apellidos = jsonObject.getString("apellidos");
                    fechaNacimiento = jsonObject.getString("fechaNacimiento");
                    email = jsonObject.getString("email");
                    telefono = jsonObject.getString("telefono");
                    pass = jsonObject.getString("pass");
                    //Toasty.warning(getApplicationContext(),"USer -> " + id,Toasty.LENGTH_LONG).show();
                    guardarPreferencias();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void guardarPreferencias() {
        //Datos
        SharedPreferences preferencesUser = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorUser = preferencesUser.edit();
        editorUser.putInt("idUsuario", id);
        //Toasty.warning(getApplicationContext(),"USer -> " + id,Toasty.LENGTH_LONG).show();
        editorUser.putString("nombres", nombres);
        editorUser.putString("apellidos", apellidos);
        editorUser.putString("fechaNacimiento", fechaNacimiento);
        editorUser.putString("email", email);
        editorUser.putString("telefono", telefono);
        editorUser.putString("password", pass);
        editorUser.commit();
    }

    private void recuperarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        etEmail.setText(preferences.getString("email",""));
        etPassword.setText(preferences.getString("password",""));
    }

    private void conectar() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrarse = findViewById(R.id.btn_Registrarse);
    }
}