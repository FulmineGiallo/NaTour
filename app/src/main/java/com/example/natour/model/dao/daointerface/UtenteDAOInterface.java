package com.example.natour.model.dao.daointerface;

import android.content.Context;

import com.amplifyframework.auth.result.AuthSignInResult;

import org.json.JSONObject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;

public interface UtenteDAOInterface
{
    public void insertUtenteSocial(String token, String nome, String cognome, Context activityPrec);
    public void setTokenUtente(String idUtente, String dataDiNascita, String nome, String cognome, Context activityPrec);
    public PublishSubject<JSONObject> getNomeCognomeUtente(String token, Context activityPrec);
    public PublishSubject<JSONObject> getBirthdate(String token, Context context);
    public Single<AuthSignInResult> loginWithCognito(String email, String password);
    public PublishSubject<JSONObject> isAdmin(String token, Context context);
    public PublishSubject<JSONObject> getCountUtenti(Context context);

}
