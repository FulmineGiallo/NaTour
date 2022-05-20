package com.example.natour.model.dao.daointerface;

import android.content.Context;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.rxjava3.subjects.PublishSubject;

public interface RecensioneDAOInterface
{
    public PublishSubject<JSONObject> insertRecensione(float valutazione, String testoRecensione, String fk_utente, String fk_itinerario, Context context);
    public PublishSubject<JSONArray> getRecensioni(Context context, String fk_itinerario);
    public PublishSubject<JSONObject> getCountRecensioni(Context context);
    public PublishSubject<JSONObject> getCountRecensioni(Context context, PublishSubject<JSONObject> publishSubject, RequestQueue queue);



}
