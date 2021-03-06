package com.example.natour.model.dao;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.connection.RequestAPI;
import com.example.natour.model.dao.daointerface.ImmagineDAOInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class ImmagineDAO implements ImmagineDAOInterface
{

    public void insertImmagine(Immagine imgList, String idItinerario, Double lat, Double longitudine, Context context)
    {
        String longi = "";
        String lati = "";

        if((lat != null)&&(longitudine!=null)){
            longi = String.valueOf(longitudine);
            lati = String.valueOf(lat);
        }
        /* Questo URL va nella tabella Immagini associata a Itinerario */
        Map<String, String> parmasAPI = new HashMap<String, String>();
        String path = "immagine/insertImage.php";

        //Insert URL
        parmasAPI.put("id_key", imgList.getKey());
        parmasAPI.put("fk_itinerario", idItinerario);
        parmasAPI.put("lat_posizione", lati);
        parmasAPI.put("long_posizione", longi);


        RequestAPI request = new RequestAPI(path, context, parmasAPI);
        request.sendRequest();
    }

    public PublishSubject<JSONArray> getImageOfItinerario(Itinerario itinerario, Context context)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("fk_itinerario", itinerario.getIdItinerario());
        Log.i("ITINERARIO ID", itinerario.getIdItinerario());
        RequestAPI requestAPI = new RequestAPI("immagine/getImages.php", context, params);
        return requestAPI.getMultipleRows();
    }
    public PublishSubject<JSONObject> getCountImmagini(Context context)
    {
        RequestAPI get = new RequestAPI("immagine/countImmagini.php", context, null);
        return get.sendRequest();
    }

    public PublishSubject<JSONObject> getCountImmagini(Context context, PublishSubject<JSONObject> publishSubject, RequestQueue queue)
    {
        RequestAPI get = new RequestAPI("immagine/countImmagini.php", context, null);
        return get.sendRequest(publishSubject, queue);
    }
}
