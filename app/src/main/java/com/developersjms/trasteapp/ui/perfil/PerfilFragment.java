package com.developersjms.trasteapp.ui.perfil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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


import com.developersjms.trasteapp.R;

import java.util.Calendar;

public class PerfilFragment extends Fragment {

    RadioGroup rgTipoDoc;
    EditText et_doc, et_nombre, et_fecha, et_telefono, et_email;
    Button btnActualizar;
    private ProgressDialog progress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        /*realtimeDatabase = new RealtimeDatabase(getContext());
        user = FirebaseAuth.getInstance().getCurrentUser();*/
        conectar(view);

        et_fecha.setOnClickListener(new View.OnClickListener() {
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

    private void actualizar() throws Exception {
        int refTipoDoc;
        String tipoDoc = "", docId, nombres, fechaNac, telefono, email, contraseña;
        if (rgTipoDoc.getCheckedRadioButtonId() != -1) {
            refTipoDoc = rgTipoDoc.getCheckedRadioButtonId();
            tipoDoc = (refTipoDoc == R.id.rbCedula) ? "Cedula" : "Nit";
        }
        docId = et_doc.getText().toString();
        nombres = et_nombre.getText().toString();
        fechaNac = et_fecha.getText().toString();
        telefono = et_telefono.getText().toString();
        email = et_email.getText().toString();

        /*if (tipoDoc.isEmpty() || docId.isEmpty() || nombres.isEmpty() || fechaNac.isEmpty() || telefono.isEmpty()
                || email.isEmpty()) {
            Toasty.warning(getContext(), "Por favor llene todos los campos", Toasty.LENGTH_LONG).show();
        } else {
            Usuario usuario = new Usuario();
            usuario.setTipoDoc(tipoDoc);
            usuario.setDocId(docId);
            usuario.setNombres(nombres);
            usuario.setFechaNac(fechaNac);
            usuario.setTelefono(telefono);
            usuario.setEmail(email);
            realtimeDatabase.updateUser(user.getUid(), usuario);
            Toasty.success(getContext(), "Los datos se han actualizado con éxito", Toasty.LENGTH_LONG).show();
        }*/

    }

    private void conectar(View view) {
        rgTipoDoc = view.findViewById(R.id.rgTipoDoc);
        et_doc = view.findViewById(R.id.et_doc);
        et_nombre = view.findViewById(R.id.et_nombre);
        et_fecha = view.findViewById(R.id.et_fecha);
        et_telefono = view.findViewById(R.id.et_telefono);
        et_email = view.findViewById(R.id.et_email);
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

                String fecha = twoDigits(year) + "/" + twoDigits(monthOfYear + 1)
                        + "/" + twoDigits(dayOfMonth);
                et_fecha.setText(fecha);
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