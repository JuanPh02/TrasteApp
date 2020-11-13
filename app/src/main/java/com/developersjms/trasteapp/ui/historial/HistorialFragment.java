package com.developersjms.trasteapp.ui.historial;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.developersjms.trasteapp.Ciudad;
import com.developersjms.trasteapp.ListViewAdapterActivos;
import com.developersjms.trasteapp.Mudanza;
import com.developersjms.trasteapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class HistorialFragment extends Fragment {

    ArrayList<Mudanza> mudanzas;
    ArrayList<Ciudad> ciudades;
    ListViewAdapterActivos adapter;
    ListView listView;
    TextView tvAlert;
    ImageView imgAlert;
    int idUsuario;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_historial, container, false);
        conectar(root);
        recuperarPreferencias();
        requestQueue = Volley.newRequestQueue(getContext());
        leerCiudades();
        leerMudanzas();
        return root;
    }

    private void recuperarPreferencias() {
        SharedPreferences preferences = getActivity().getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        idUsuario = preferences.getInt("idUsuario",0);
    }

    private void leerMudanzas() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, 700);
        leerMudanzas("http://192.168.0.108//trasteapp/leer_mudanzas.php?idUsuario="+idUsuario);
    }

    private void leerMudanzas(String URL) {
        mudanzas = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int idMudanza, idConductor = 0, idVehiculo = 0, idCiudadPartida, idCiudadDestino, estado, precio;
                String direccionPartida, direccionDestino, fechaHora, descripcionObjetos;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        estado = jsonObject.getInt("estado");
                        idMudanza = jsonObject.getInt("idMudanza");
                        idCiudadPartida = jsonObject.getInt("idCiudadPartida");
                        direccionPartida = jsonObject.getString("direccionPartida");
                        idCiudadDestino = jsonObject.getInt("idCiudadDestino");
                        direccionDestino = jsonObject.getString("direccionDestino");
                        fechaHora = jsonObject.getString("fechaHora");
                        descripcionObjetos = jsonObject.getString("descripcionObjetos");
                        precio = jsonObject.getInt("precio");
                        if(estado != 0) {
                            idConductor = jsonObject.getInt("idConductor");
                            idVehiculo = jsonObject.getInt("idVehiculo");
                        }
                        mudanzas.add(new Mudanza(idMudanza,idConductor,idVehiculo,idCiudadPartida,direccionPartida,idCiudadDestino,direccionDestino,fechaHora,descripcionObjetos,estado,precio));
                    }
                    adapter = new ListViewAdapterActivos(getContext(), mudanzas, ciudades);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    Toasty.warning(getContext(),"Error--> " + e.getMessage(), Toasty.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.warning(getContext(),"Solicita tu primer servicio y aquí podrás consultarlo", Toasty.LENGTH_LONG).show();
                tvAlert.setText("No hay registros");
                listView.setEmptyView(tvAlert);
                imgAlert.setVisibility(View.VISIBLE);
            }
        });
        requestQueue.add(jsonArrayRequest);
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

    private void conectar(View root) {
        listView = root.findViewById(R.id.lvActiveServices);
        tvAlert = root.findViewById(R.id.tvAlertLv);
        imgAlert = root.findViewById(R.id.imgAlert);
    }
}