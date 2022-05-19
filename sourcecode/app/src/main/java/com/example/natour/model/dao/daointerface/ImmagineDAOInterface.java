package com.example.natour.model.dao.daointerface;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.rxjava3.subjects.PublishSubject;

public interface ImmagineDAOInterface
{
    void insertImmagine(Immagine imgList, String idItinerario, Double lat, Double longitudine, Context context);
    PublishSubject<JSONArray> getImageOfItinerario(Itinerario itinerario, Context context);
    PublishSubject<JSONObject> getCountImmagini(Context context);
    PublishSubject<JSONObject> getCountImmagini(Context context, PublishSubject<JSONObject> publishSubject, RequestQueue queue);

}
