package com.example.natour.model.connection;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestAPI
{
   /* private String URL;
    private Context context;
    private JSONObject response;
    private Map<String, String> paramsBody = new HashMap<String, String>();

    public RequestAPI(String URL, Context context, Map<String, String> paramsBody)
    {
        this.URL = URL;
        this.context = context;
        this.paramsBody = paramsBody;
    }

    *//* OTTENERE IL JSONOBJECT indietro *//*
    RequestQueue queue = Volley.newRequestQueue(context);
    StringRequest request = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>()
    {
        @Override
        public void onResponse(String response)
        {
            Toast.makeText(context, "Data added to API", Toast.LENGTH_SHORT).show();
            try
            {
                JSONObject respObj = new JSONObject(response);
                String name = respObj.getString("email");
                System.out.println(name + "OOOOOOOOOOOOO");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }, new com.android.volley.Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
        }
    }) {
        @Override
        protected Map<String, String> getParams()
        {

            Map<String, String> params = new HashMap<String, String>();
            params.put("id_utente", "1");
            return params;
        }
    };
        queue.add(request);*/
}