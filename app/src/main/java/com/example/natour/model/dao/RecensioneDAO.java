package com.example.natour.model.dao;

import android.content.Context;

import com.example.natour.model.connection.RequestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class RecensioneDAO
{

    public PublishSubject<JSONObject> insertRecensione(float valutazione, String testoRecensione, String fk_utente, String fk_itinerario, Context context)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("valutazione", String.valueOf(valutazione));
        params.put("testo", testoRecensione);
        params.put("fk_utente", fk_utente);
        params.put("fk_itinerario", fk_itinerario);
        RequestAPI requestAPI = new RequestAPI("recensione/insertRecensione.php", context, params);
        return requestAPI.sendRequest();

    }
    public PublishSubject<JSONArray> getRecensioni(Context context, String fk_itinerario)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("fk_itinerario", fk_itinerario);
        RequestAPI request = new RequestAPI("recensione/getRecensione.php", context,params);
        return  request.getMultipleRows();

    }

    public PublishSubject<JSONObject> getCountRecensioni(Context context)
    {
        RequestAPI get = new RequestAPI("recensione/countRecensioni.php", context, null);
        return get.sendRequest();
    }

}
