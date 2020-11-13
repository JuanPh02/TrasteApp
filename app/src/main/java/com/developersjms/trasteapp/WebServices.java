package com.developersjms.trasteapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class WebServices implements Response.Listener {

    private Context context;
    RequestQueue requestQueue;

    public WebServices(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }
    @Override
    public void onResponse(Object response) {

    }
}
