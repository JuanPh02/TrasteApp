package com.developersjms.trasteapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ListViewAdapterActivos extends BaseAdapter {

    Context context;
    ArrayList<Mudanza> lstMudanzas;
    ArrayList<Ciudad> ciudades;
    LayoutInflater inflater;

    public ListViewAdapterActivos(Context context, ArrayList<Mudanza> lstUnidades, ArrayList<Ciudad> ciudades) {
        this.context = context;
        this.lstMudanzas = lstUnidades;
        this.ciudades = ciudades;
    }



    @Override
    public int getCount() {
        return lstMudanzas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tvId, tvDireccionCargue, tvDireccionDescargue, tvFechaHora, tvEstado, tvPrecio;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_row_sactives, parent, false);

        tvId = itemView.findViewById(R.id.lrsa_tvId);
        tvDireccionCargue = itemView.findViewById(R.id.lrsa_tvDireccionCargue);
        tvDireccionDescargue = itemView.findViewById(R.id.lrsa_tvDireccionDescargue);
        tvFechaHora = itemView.findViewById(R.id.lrsa_tvFechaHora);
        tvEstado = itemView.findViewById(R.id.lrsa_tvEstado);
        tvPrecio = itemView.findViewById(R.id.lrsa_tvPrecio);

        Mudanza tr = lstMudanzas.get(position);

        tvId.setText("Tr-" + tr.getIdMudanza());
        tvDireccionCargue.setText(tr.getDireccionPartida() + ", " + ciudades.get(tr.getIdCiudadPartida()-1).getNombre());
        tvDireccionDescargue.setText(tr.getDireccionDestino() + ", " + ciudades.get(tr.getIdCiudadDestino()-1).getNombre());
        tvFechaHora.setText(tr.getFechaHora());
        switch (tr.getEstado()) {
            case 0:
                tvEstado.setTextColor(Color.rgb(250,190,0));
                tvEstado.setText("Pendiente");
                break;
            case 1:
                tvEstado.setTextColor(Color.rgb(0,50,190));
                tvEstado.setText("Aceptado");
                break;
            case 2:
                tvEstado.setTextColor(Color.rgb(220,0,0));
                tvEstado.setText("Rechazado");
                break;
            case 3:
                tvEstado.setTextColor(Color.rgb(65,190,0));
                tvEstado.setText("Finalizado");
                break;
        }

        if(tr.getPrecio() == 0){
            tvPrecio.setText("$ No definido");
        } else {
            tvPrecio.setText( "$ " + tr.getPrecio());
        }

        return itemView;
    }

}
