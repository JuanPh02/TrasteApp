package com.developersjms.trasteapp.ui.support;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.developersjms.trasteapp.MensajeSoporte;
import com.developersjms.trasteapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.dmoral.toasty.Toasty;


public class SupportFragment extends Fragment {

    EditText etNombre, etEmail, etProblema, etDetalles;
    Button btnEnviar;
    FloatingActionButton fab_whatsapp;
    //RealtimeDatabase realtimeDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_support, container, false);
        conectar(root);
        //realtimeDatabase = new RealtimeDatabase(getContext());
        //cargarDatos();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(getContext(),"Mensaje de soporte enviado correctamente",Toasty.LENGTH_LONG).show();
                limpiarCampos();
            }
        });

        /*btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre, email, problema, detalles;
                nombre = etNombre.getText().toString();
                email = etEmail.getText().toString();
                problema = etProblema.getText().toString();
                detalles = etDetalles.getText().toString();

                if(nombre.isEmpty() || email.isEmpty() || problema.isEmpty() || detalles.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
                } else {
                    MensajeSoporte mensaje = new MensajeSoporte();
                    mensaje.setNombre(nombre);
                    mensaje.setEmail(email);
                    mensaje.setProblema(problema);
                    mensaje.setDetalles(detalles);
                    try {
                        //realtimeDatabase.addMessage(mensaje);
                        Toast.makeText(getContext(),"El mensaje se ha enviado correctamente", Toast.LENGTH_LONG).show();
                        limpiarCampos();
                    } catch (Exception e) {
                        Toast.makeText(getContext(),"ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("ERROR");
                        builder.setMessage("No se pudo realizar la operación \n" + e.getMessage());

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                    }
                }
            }
        });*/

        /*fab_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=573013544092&text=Hola!%20Necesito%20ayuda%20para%20solicitar%20un%20trasteo%20.%20%E2%9C%8B%F0%9F%9A%9A");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });*/

        return root;
    }

    /*private void cargarDatos() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        etEmail.setText(user.getEmail());
        etEmail.setFocusable(false);
        etEmail.setEnabled(false);
        etEmail.setCursorVisible(false);
        etNombre.setText(user.getDisplayName());
    }*/

    private void limpiarCampos() {
        etNombre.setText("");
        etEmail.setText("");
        etProblema.setText("");
        etDetalles.setText("");
    }

    private void conectar(View root) {
        etNombre = root.findViewById(R.id.sp_et_nombre);
        etEmail = root.findViewById(R.id.sp_et_email);
        etProblema = root.findViewById(R.id.sp_et_problema);
        etDetalles = root.findViewById(R.id.sp_et_detalle);
        btnEnviar = root.findViewById(R.id.sp_btnEnviar);
        //fab_whatsapp = root.findViewById(R.id.fab_whatsapp);
    }
}