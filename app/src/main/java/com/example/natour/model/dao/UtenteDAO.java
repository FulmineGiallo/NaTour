package com.example.natour.model.dao;

import android.content.Context;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.rx.RxAmplify;
import com.example.natour.model.connection.RequestAPI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class UtenteDAO
{
    public void insertUtenteSocial(String token, String nome, String cognome, Context activityPrec)
    {
        Map<String, String> parmasAPI = new HashMap<String, String>();
        //L'idea è di creare un hashMap e passarla a volley per metterla nel body
        String path = "utente/insertUtente.php";
        //Insert UTENTE
        parmasAPI.put("nome", nome);
        parmasAPI.put("cognome", cognome);
        parmasAPI.put("token", token);

        RequestAPI request = new RequestAPI(path, activityPrec, parmasAPI);
        request.sendRequest();
    }
    public void setTokenUtente(String idUtente, String dataDiNascita, String nome, String cognome, Context activityPrec)
    {
        Map<String, String> parmasAPI = new HashMap<String, String>();
         //L'idea è di creare Un hashMap e passarla a volley per metterla nel body
        String path = "utente/insertToken.php";

         //Insert Token e Data Di Nascita nella registrazione
        parmasAPI.put("nome", nome);
        parmasAPI.put("cognome", cognome);
        parmasAPI.put("token", idUtente);
        parmasAPI.put("data_di_nascita", dataDiNascita);


        RequestAPI request = new RequestAPI(path, activityPrec, parmasAPI);
        request.sendRequest();
    }
    public PublishSubject<JSONObject> getNomeCognomeUtente(String token, Context activityPrec)
    {
        Map<String, String> parmasAPI = new HashMap<String, String>();
        String path = "utente/getNomeUtente.php";

        parmasAPI.put("token", token);
        RequestAPI request = new RequestAPI(path, activityPrec, parmasAPI);
        return request.sendRequest();
    }
    public PublishSubject<JSONObject> getBirthdate(String token, Context context)
    {

        HashMap<String,String> parmasAPI = new HashMap<>();
        String path = "utente/getBirthdate.php";
        parmasAPI.put("token", token);

        RequestAPI request = new RequestAPI(path, context, parmasAPI);
        return request.sendRequest();



    }
    public Single<AuthSignInResult> loginWithCognito(String email, String password)
    {

        return RxAmplify.Auth.signIn(email, password);

    }

    public Single<List<AuthUserAttribute>> getInformationOfAmplifySession()
    {
        Single<List<AuthUserAttribute>> infoUtente = RxAmplify.Auth.fetchUserAttributes();



        return infoUtente;
    }



}


