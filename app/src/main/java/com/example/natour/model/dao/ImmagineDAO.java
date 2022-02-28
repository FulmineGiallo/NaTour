package com.example.natour.model.dao;

import android.content.Context;

import com.example.natour.model.Immagine;
import com.example.natour.model.connection.RequestAPI;

import java.util.HashMap;
import java.util.Map;

public class ImmagineDAO
{

    public void insertImmagine(Immagine imgList, String idItinerario, double lat, double longitudine, Context context)
    {
        /* Questo URL va nella tabella Immagini associata a Itinerario */
        Map<String, String> parmasAPI = new HashMap<String, String>();
        String path = "immagine/insertImage.php";

        //Insert URL
        parmasAPI.put("id_key", imgList.getKey());
        parmasAPI.put("fk_itinerario", idItinerario);
        parmasAPI.put("lat_posizione", String.valueOf(lat));
        parmasAPI.put("long_posizione", String.valueOf(longitudine));


        RequestAPI request = new RequestAPI(path, context, parmasAPI);
        request.sendRequest();


    }
}
