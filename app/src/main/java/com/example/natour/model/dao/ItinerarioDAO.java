package com.example.natour.model.dao;

import android.content.Context;

import com.example.natour.model.connection.RequestAPI;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ItinerarioDAO
{
    public void insertItinerario(String nome, String durata, boolean disabili, int difficolta, String descrizione, FileOutputStream waypoints, Context context, String tokenUtente)
    {
        Map<String, String> parmasAPI = new HashMap<String, String>();
        parmasAPI.put("nome",nome);
        parmasAPI.put("durata", durata);
        parmasAPI.put("disabili", String.valueOf(disabili));
        parmasAPI.put("descrizione", descrizione);
        parmasAPI.put("difficolta", String.valueOf(difficolta));
        parmasAPI.put("fk_utente", tokenUtente);

        RequestAPI insert = new RequestAPI("insertItinerario", context, parmasAPI);
    }


}
