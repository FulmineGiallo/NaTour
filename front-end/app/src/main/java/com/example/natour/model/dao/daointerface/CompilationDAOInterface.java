package com.example.natour.model.dao.daointerface;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.rxjava3.subjects.PublishSubject;

public interface CompilationDAOInterface
{
    PublishSubject<JSONObject> insertCompilation(String titolo, String descrizione, String fk_utente, Context context);
    PublishSubject<JSONArray> getCompilation(Context context, String fk_utente);
    PublishSubject<JSONArray> getItinerariFromCompilation(Context context ,String id_compilation);
    void addItinerarioToCompilation(int idCompilation, String idItinerario, Context context);


}
