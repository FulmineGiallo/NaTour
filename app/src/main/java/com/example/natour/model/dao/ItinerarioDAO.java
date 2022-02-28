package com.example.natour.model.dao;

import android.content.Context;

import com.example.natour.model.connection.RequestAPI;

import java.util.HashMap;
import java.util.Map;

public class ItinerarioDAO
{
    public void insertItinerario(String key, String nome, String durata, boolean disabili, int difficolta, String descrizione, Context context, String tokenUtente, String nomefile)
    {
        Map<String, String> parmasAPI = new HashMap<String, String>();
        parmasAPI.put("id_itinerario", key);
        parmasAPI.put("nome",nome);
        parmasAPI.put("durata", durata);
        parmasAPI.put("disabili", String.valueOf(disabili));
        parmasAPI.put("descrizione", descrizione);
        parmasAPI.put("difficolta", String.valueOf(difficolta));
        parmasAPI.put("fk_utente", tokenUtente);
        parmasAPI.put("nomefile", nomefile);

        RequestAPI insert = new RequestAPI("itinerario/insertItinerario.php", context, parmasAPI);
        insert.sendRequest();
    }


}
