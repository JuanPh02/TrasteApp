package com.developersjms.trasteapp.ui.support;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.developersjms.trasteapp.MensajeSoporte;
import com.developersjms.trasteapp.R;
import com.developersjms.trasteapp.TiposProblemas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class SupportFragment extends Fragment {

    String email, descripcion;
    int idUsuario, idTipoProblema, idMudanza;
    Spinner spTipoProblemas, spMudanzas;
    EditText etDescripcion;
    Button btnEnviar;
    ArrayList<TiposProblemas> lst_tiposproblemas;
    ArrayList<Integer> idMudanzasUsuario;
    ImageButton btnW, btnI, btnF;
    RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_support, container, false);
        conectar(root);
        recuperarPreferenciasUsuario();
        requestQueue = Volley.newRequestQueue(getContext());
        leerTiposProblemas();
        leerMudanzasUsuario();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idTipoProblema = spTipoProblemas.getSelectedItemPosition();
                idMudanza = spMudanzas.getSelectedItemPosition();
                descripcion = etDescripcion.getText().toString();

                if(spTipoProblemas.getSelectedItemPosition() != 0 && spMudanzas.getSelectedItemPosition() != 0 && !descripcion.isEmpty()) {
                    idTipoProblema = spTipoProblemas.getSelectedItemPosition();
                    idMudanza = Integer.parseInt(spMudanzas.getSelectedItem().toString());
                    descripcion = etDescripcion.getText().toString();
                    enviarSoporte();
                } else {
                    Toasty.warning(getContext(),"Por favor seleccione y llene todos los datos del formulario",Toasty.LENGTH_SHORT).show();
                }
            }
        });

        btnW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=573013544092&text=Hola!%20Necesito%20ayuda%20para%20solicitar%20un%20trasteo%20.%20%E2%9C%8B%F0%9F%9A%9A");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btnI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/juan.pablo.a.hdez/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/juanpablo.arroyave.716");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return root;
    }

    private void recuperarPreferenciasUsuario() {
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        idUsuario = preferences.getInt("idUsuario", 0);
        email = preferences.getString("email", "");
    }

    private void leerTiposProblemas() {
        lst_tiposproblemas = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( "http://192.168.0.108//trasteapp/leer_tiposproblemas.php", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String nombre = "";
                int id = 0;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getInt("idTipoProblema");
                        nombre = jsonObject.getString("nombre");
                        lst_tiposproblemas.add(new TiposProblemas(id, nombre));
                    }
                    cargarSpinnerTiposProblemas();
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

    private void leerMudanzasUsuario() {
        idMudanzasUsuario = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( "http://192.168.0.108//trasteapp/leer_mudanzas_usuario.php?idUsuario="+idUsuario, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int id = 0;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getInt("idMudanza");
                        idMudanzasUsuario.add(id);
                    }
                    cargarSpinnerMudanzas();
                } catch (JSONException e) {
                    Toasty.warning(getContext(),"Error --> " + e.getMessage(), Toasty.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(),"ERROR -> " + error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    private void cargarSpinnerTiposProblemas() {
        String[] tiposProblemas = new String[lst_tiposproblemas.size()+1];
        tiposProblemas[0] = "Seleccione tipo de problema";
        for (int i = 0; i < lst_tiposproblemas.size(); i++) {
            tiposProblemas[i+1] = lst_tiposproblemas.get(i).getNombre();
        }
        spTipoProblemas.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,tiposProblemas));
    }

    private void cargarSpinnerMudanzas() {
        String[] mudanzas = new String[idMudanzasUsuario.size()+1];
        mudanzas[0] = "Seleccione la mudanza";
        for (int i = 0; i < idMudanzasUsuario.size(); i++) {
            mudanzas[i+1] = String.valueOf(idMudanzasUsuario.get(i));
        }
        spMudanzas.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,mudanzas));
    }

    private void enviarSoporte() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.0.108/trasteapp/enviar_soporte_usuario.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Correct")) {
                    Toasty.success(getContext(), "Mensaje de soporte enviado correctamente", Toasty.LENGTH_LONG).show();
                    limpiarCampos();
                } else {
                    Toasty.error(getContext(), "Se ha producido un error al intentar enviar la informacion", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(), "ERROR -> " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("idUsuario", String.valueOf(idUsuario));
                parametros.put("idTipoProblema", String.valueOf(idTipoProblema));
                parametros.put("idMudanza", String.valueOf(idMudanza));
                parametros.put("descripcion", descripcion);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void limpiarCampos() {
        spTipoProblemas.setSelection(0);
        spMudanzas.setSelection(0);
        etDescripcion.setText("");
    }

    private void conectar(View root) {
        spTipoProblemas = root.findViewById(R.id.sp_spTipoProblemas);
        spMudanzas = root.findViewById(R.id.sp_spMudanzas);
        etDescripcion = root.findViewById(R.id.sp_etDescripcion);
        btnEnviar = root.findViewById(R.id.sp_btnEnviar);
        btnW = root.findViewById(R.id.btn_w);
        btnI = root.findViewById(R.id.btn_i);
        btnF = root.findViewById(R.id.btn_f);
    }
}