package com.example.natour.model.dao;

import android.content.Context;

import com.example.natour.model.connection.RequestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class ItinerarioDAO
{
    public PublishSubject<JSONObject> insertItinerario(String key, String nome, String durata, boolean disabili, int difficolta, String descrizione, Context context, String tokenUtente, String nomefile)
    {
        boolean eseguito = false;

        Map<String, String> parmasAPI = new HashMap<String, String>();
        parmasAPI.put("id_itinerario", key);
        parmasAPI.put("nome",nome);
        parmasAPI.put("durata", durata);
        parmasAPI.put("disabile", String.valueOf(disabili));
        parmasAPI.put("descrizione", descrizione);
        parmasAPI.put("difficolta", String.valueOf(difficolta));
        parmasAPI.put("fk_utente", tokenUtente);
        parmasAPI.put("nomefile", nomefile);

        RequestAPI insert = new RequestAPI("itinerario/insertItinerario.php", context, parmasAPI);
        return insert.sendRequest();


    }
    public PublishSubject<JSONArray> getItinerari(Context context)
    {
        RequestAPI request = new RequestAPI("itinerario/get_itinerario.php", context,null);
        return  request.getMultipleRows();


    }
    public PublishSubject<JSONArray> getItinerariFromUtente(Context context, String token)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("fk_utente",token);

        RequestAPI request = new RequestAPI("itinerario/getItinerarioFromUtente.php", context,params);
        return  request.getMultipleRows();
    }

}
