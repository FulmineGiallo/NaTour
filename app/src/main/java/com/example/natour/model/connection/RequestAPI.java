package com.example.natour.model.connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.reactivex.rxjava3.subjects.PublishSubject;


public class RequestAPI
{
    private String endpoint = "http://ec2-34-245-52-200.eu-west-1.compute.amazonaws.com/natour21/api/";
    private String path;
    private Context context;
    private JSONObject respObj;
    private Map<String, String> params = null;
    private PublishSubject<JSONObject> risposta;
    private JSONArray respArr;
    private PublishSubject<JSONArray> rispostArr;
    public RequestAPI(String path, Context context, Map<String, String> paramsBody)
    {
        this.path = path;
        this.context = context;
        this.params = paramsBody;

    }

    public PublishSubject<JSONObject> sendRequest()
    {

        risposta = PublishSubject.create();

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, endpoint + path, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {

                    respObj = new JSONObject(response);
                    risposta.onNext(respObj);

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


        return risposta;
    }
    public PublishSubject<JSONArray> getMultipleRows()
    {
        rispostArr = PublishSubject.create();

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, endpoint + path, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    respArr = new JSONArray(response);
                    rispostArr.onNext(respArr);

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


        return rispostArr;
    }
    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }
}