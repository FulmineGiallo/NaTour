package com.example.natour.model.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.natour.model.daointerface.UtenteDaoInterface;
import com.example.natour.model.Utente;
import com.example.natour.view.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UtenteDAO
{
    public void utenteExist(int id, Context activityPrec) throws SQLException
    {
        String url = "http://ec2-54-155-8-120.eu-west-1.compute.amazonaws.com/natour21/api/utente/login.php";
        RequestQueue queue = Volley.newRequestQueue(activityPrec);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(activityPrec, "Data added to API", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activityPrec, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
        queue.add(request);
    }
}


