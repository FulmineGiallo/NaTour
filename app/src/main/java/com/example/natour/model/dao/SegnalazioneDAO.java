package com.example.natour.model.dao;

import android.content.Context;

import com.example.natour.model.connection.RequestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class SegnalazioneDAO
{
    public PublishSubject<JSONObject> insertSegnalazione(String titolo, String testo, String fk_utente, String fk_itinerario, Context context)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("titolo", titolo);
        params.put("descrizione", testo);
        params.put("fk_utente", fk_utente);
        params.put("fk_itinerario", fk_itinerario);
        RequestAPI requestAPI = new RequestAPI("segnalazione/insertSegnalazione.php", context, params);
        return requestAPI.sendRequest();

    }
    public PublishSubject<JSONArray> getSegnalazioni(Context context, String fk_itinerario)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("fk_itinerario", fk_itinerario);
        RequestAPI request = new RequestAPI("segnalazione/getSegnalazione.php", context,params);
        return  request.getMultipleRows();
    }
    public PublishSubject<JSONObject> getNumSegnalazioni(Context context, String fk_itinerario){
        HashMap<String, String> params = new HashMap<>();
        params.put("fk_itinerario", fk_itinerario);
        RequestAPI request = new RequestAPI("segnalazione/getCountSegnalazione.php", context, params);
        return request.sendRequest();
    }
}
