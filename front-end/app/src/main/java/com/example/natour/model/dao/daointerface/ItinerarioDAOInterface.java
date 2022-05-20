package com.example.natour.model.dao.daointerface;

import android.content.Context;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.rxjava3.subjects.PublishSubject;

public interface ItinerarioDAOInterface
{
    PublishSubject<JSONObject> insertItinerario(String key, String nome, String durata, boolean disabili, int difficolta, String descrizione, Context context, String tokenUtente, String nomefile);PublishSubject<JSONArray> getItinerari(Context context);
    PublishSubject<JSONArray> getItinerariFromUtente(Context context, String token);
    PublishSubject<JSONObject> getCountItinerari(Context context);
    PublishSubject<JSONObject> getCountItinerari(Context context, PublishSubject<JSONObject> publishSubject, RequestQueue queue);

}
