package com.example.natour.model.dao;

import android.content.Context;

import com.example.natour.model.connection.RequestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class RecensioneDAO
{

    public PublishSubject<JSONObject> insertRecensione(int rate, String testoRecensione, String fk_utente, String fk_itinerario, Context context)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("rate", String.valueOf(rate));
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

}
