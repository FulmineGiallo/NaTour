package com.example.natour.model.connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.natour.model.Utente;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.rx.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


public class RequestAPI
{
    private String endpoint = "http://ec2-54-170-83-9.eu-west-1.compute.amazonaws.com/natour21/api/";
    private String path;
    private Context context;
    private JSONObject respObj;
    private Map<String, String> params = null;
    private PublishSubject<JSONObject> risposta;

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


}