package com.example.natour.model.dao;

import android.content.Context;

import com.example.natour.model.connection.RequestAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class CompilationDAO
{
    public PublishSubject<JSONObject> insertCompilation(String titolo, String descrizione, String fk_utente, Context context)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("nome", titolo);
        params.put("descrizione", descrizione);
        params.put("id_utente", fk_utente);
        RequestAPI requestAPI = new RequestAPI("compilation/create_compilation.php", context, params);
        return requestAPI.sendRequest();

    }
    public PublishSubject<JSONArray> getCompilation(Context context, String fk_utente)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("id_utente", fk_utente);
        RequestAPI request = new RequestAPI("compilation/get_compilation_from_utente.php", context,params);
        return  request.getMultipleRows();

    }
}
