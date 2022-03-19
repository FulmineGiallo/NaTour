package com.example.natour.model.dao;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.example.natour.model.connection.RequestAPI;
import com.example.natour.model.dao.daointerface.StatisticheDAOInterface;

import org.json.JSONObject;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class StatisticheDAO implements StatisticheDAOInterface
{
    public void updateRicerche(Context context){
        RequestAPI requestAPI = new RequestAPI("statistiche/updateRicerche.php", context, null);
        requestAPI.sendRequest();
    }
    public void updateLogin(Context context)
        {
        RequestAPI requestAPI = new RequestAPI("statistiche/updateLogin.php", context, null);
        requestAPI.sendRequest();
    }
    public PublishSubject<JSONObject> getCountRicerche(Context context){
        RequestAPI requestAPI = new RequestAPI("statistiche/getNumRierche.php", context, null);
        return  requestAPI.sendRequest();
    }

    public PublishSubject<JSONObject> getCountRicerche(Context context, PublishSubject<JSONObject> publishSubject, RequestQueue queue){
        RequestAPI requestAPI = new RequestAPI("statistiche/getNumRicerche.php", context, null);
        return requestAPI.sendRequest(publishSubject, queue);
    }

    public PublishSubject<JSONObject> getCountLogin(Context context){
        RequestAPI requestAPI = new RequestAPI("statistiche/getNumLogin.php", context, null);
        return requestAPI.sendRequest();
    }

    public PublishSubject<JSONObject> getCountLogin(Context context, PublishSubject<JSONObject> publishSubject, RequestQueue queue){
        RequestAPI requestAPI = new RequestAPI("statistiche/getNumLogin.php", context, null);
        return requestAPI.sendRequest(publishSubject, queue);
    }

}
