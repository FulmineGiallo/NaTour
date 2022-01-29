package com.example.natour.model.connection;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.loader.content.Loader;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.natour.view.Login;

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
    private String endpoint = "http://ec2-54-170-83-9.eu-west-1.compute.amazonaws.com/natour21/api/";
    private String path;
    private Context context;
    private JSONObject respObj;
    private Map<String, String> params = null;

    public RequestAPI(String path, Context context, Map<String, String> paramsBody)
    {
        this.path = path;
        this.context = context;
        this.params = paramsBody;

    }

    public JSONObject sendRequest()
    {

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, endpoint + path, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    respObj = new JSONObject(response);
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
                Log.e("ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };

        queue.add(request);

        while(respObj == null)
        {

        }

        return respObj;
    }



}