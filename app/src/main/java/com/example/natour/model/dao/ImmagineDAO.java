package com.example.natour.model.dao;

import android.content.Context;

import com.example.natour.model.connection.RequestAPI;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImmagineDAO
{

    public void insertURLImage(String URL, Context activityPrec)
    {
        /* Questo URL va nella tabella Immagini associata a Itinerario */
        Map<String, String> parmasAPI = new HashMap<String, String>();
        //L'idea è di creare Un hashMap e passarla a volley per metterla nel body
        String path = "utente/insertURLImage.php";

        //Insert URL
        parmasAPI.put("token", URL);


        RequestAPI request = new RequestAPI(path, activityPrec, parmasAPI);
        request.sendRequest();

    }

    public void insertItinerario(String tokenUtente, String nome, String durata, File gpx, String descrizione)
    {




    }
}
