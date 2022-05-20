package com.example.natour.model.dao.daointerface;

import android.content.Context;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import io.reactivex.rxjava3.subjects.PublishSubject;

public interface StatisticheDAOInterface
{
    public void updateRicerche(Context context);
    public void updateLogin(Context context);
    public PublishSubject<JSONObject> getCountRicerche(Context context);
    public PublishSubject<JSONObject> getCountRicerche(Context context, PublishSubject<JSONObject> publishSubject, RequestQueue queue);
    public PublishSubject<JSONObject> getCountLogin(Context context);
    public PublishSubject<JSONObject> getCountLogin(Context context, PublishSubject<JSONObject> publishSubject, RequestQueue queue);
}
