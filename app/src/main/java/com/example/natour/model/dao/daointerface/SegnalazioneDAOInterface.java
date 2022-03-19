package com.example.natour.model.dao.daointerface;

import android.content.Context;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.rxjava3.subjects.PublishSubject;

public interface SegnalazioneDAOInterface
{
    public PublishSubject<JSONObject> insertSegnalazione(String titolo, String testo, String fk_utente, String fk_itinerario, Context context);
    public PublishSubject<JSONArray> getSegnalazioni(Context context, String fk_itinerario);
    public PublishSubject<JSONObject> getNumSegnalazioni(Context context, String fk_itinerario);
    public PublishSubject<JSONObject> getCountSegnalazioni(Context context);
    public PublishSubject<JSONObject> getCountSegnalazioni(Context context, PublishSubject<JSONObject> publishSubject, RequestQueue queue);

}
