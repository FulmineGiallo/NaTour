package com.example.natour.model.dao;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.natour.R;
import com.example.natour.controller.ControllerLogin;
import com.example.natour.model.connection.RequestAPI;
import com.example.natour.model.connection.SimpleCallBackInterface;
import com.example.natour.model.daointerface.UtenteDaoInterface;
import com.example.natour.model.Utente;
import com.example.natour.view.Login;
import com.google.android.gms.auth.api.Auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UtenteDAO implements UtenteDaoInterface
{


    public void setTokenUtente(String idUtente, String dataDiNascita, Context activityPrec)
    {
        Map<String, String> parmasAPI = new HashMap<String, String>();
        /* L'idea è di creare Un hashMap e passarla a volley per metterla nel body */
        String URL = R.string.endpointVM + "utente/insertToken.php";

        /* Insert Token e Data Di Nascita nella registrazione*/
        parmasAPI.put("token", idUtente);
        parmasAPI.put("dataDiNascita", dataDiNascita);


        RequestAPI request = new RequestAPI(URL, activityPrec, parmasAPI);
        JSONObject responseAPI = request.sendRequest();


    }
    public void existUtente(String token, Context context)
    {
        Map<String, String> parmasAPI = new HashMap<String, String>();
        String URL = R.string.endpointVM + "utente/existUtente.php";
        parmasAPI.put("token", token);
        JSONObject response;

        RequestAPI requestExistUtente = new RequestAPI(URL, context, parmasAPI);

    }
    public String getBirthdate(String token, Context context)
    {
        Map<String, String> parmasAPI = new HashMap<String, String>();
        String URL = R.string.endpointVM + "utente/getBirthdate.php";
        parmasAPI.put("token", token);
        JSONObject response ;

        RequestAPI requestBirthdate = new RequestAPI(URL, context, parmasAPI);
        response = requestBirthdate.sendRequest();

        Log.i("COMPLEANNO", response.toString());
        return response.toString();

    }
    public void loginWithCognito(String email, String password)
    {
        Amplify.Auth.signIn(email, password,
                result ->{
                    Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );

    }

    public void getInformationOfAmplifySession(Context context, Utente utente)
    {
        Amplify.Auth.fetchUserAttributes(
                attributes ->
                {
                    Log.i("AuthDemo", "User attributes = " + attributes.get(0).getValue());
                    Log.i("AuthDemo", "User attributes = " + attributes.get(1).getValue());
                    Log.i("AuthDemo", "User attributes = " + attributes.get(2).getValue());
                    Log.i("AuthDemo", "User attributes = " + attributes.get(3).getValue());
                    Log.i("AuthDemo", "User attributes = " + attributes.get(4).getValue());
                    utente.setEmail(attributes.get(0).getValue());
                    Log.i("EMAIL", utente.getEmail());
                    utente.setToken(attributes.get(1).getValue());
                    utente.setNome(attributes.get(2).getValue());
                    utente.setCognome(attributes.get(3).getValue());

                },
                error -> Log.e("AuthDemo", "Failed to fetch user attributes.", error)
        );
        while(utente.getCognome() == null)
        {

        }
        utente.setDataDiNascita(getBirthdate(utente.getToken(), context));


    }

    public Utente getInformationOfAmplifySessionWithGoogle(Context context)
    {
        Utente nuovoUtente = new Utente();


        Amplify.Auth.signInWithSocialWebUI(AuthProvider.google(), (Activity) context,
                result ->
                {
                    Amplify.Auth.fetchUserAttributes(
                            attributes ->
                            {
                                Log.i("AuthDemo 1", "User attributes = " + attributes.get(0).getValue());
                                Log.i("AuthDemo 2", "User attributes = " + attributes.get(1).getValue());
                                Log.i("AuthDemo 3", "User attributes = " + attributes.get(2).getValue());
                                Log.i("AuthDemo 4", "User attributes = " + attributes.get(3).getValue());
                                Log.i("AuthDemo 5", "User attributes = " + attributes.get(4).getValue());

                                nuovoUtente.setEmail(attributes.get(0).getValue());
                                StringBuffer idUtente = new StringBuffer(attributes.get(3).getValue());
                                String token;
                                token = idUtente.substring(idUtente.indexOf("userId\":\"") + 9, idUtente.indexOf(",") - 1 );

                                nuovoUtente.setToken(token);
                                nuovoUtente.setNome(attributes.get(1).getValue());
                                nuovoUtente.setCognome(attributes.get(4).getValue());


                            },
                            error -> Log.e("AuthDemo", "Failed to fetch user attributes.", error)
                    );
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );




        return nuovoUtente;
    }
    public Utente getInformationOfAmplifySessionWithFacebook(@NonNull SimpleCallBackInterface<Utente> callBack)
    {
        Utente nuovoUtente = new Utente();


        Amplify.Auth.fetchUserAttributes(
                attributes ->
                {
                    Log.i("AuthDemo", "User attributes = " + attributes.get(0).getValue());
                    Log.i("AuthDemo", "User attributes = " + attributes.get(1).getValue());
                    Log.i("AuthDemo", "User attributes = " + attributes.get(2).getValue());
                    Log.i("AuthDemo", "User attributes = " + attributes.get(3).getValue());
                    Log.i("AuthDemo", "User attributes = " + attributes.get(4).getValue());

                    nuovoUtente.setEmail(attributes.get(0).getValue());
                    nuovoUtente.setToken(attributes.get(1).getValue());
                    nuovoUtente.setNome(attributes.get(2).getValue());
                    nuovoUtente.setCognome(attributes.get(3).getValue());

                    /* RECUPERO DATA DI NASCITA */



                    callBack.callback(nuovoUtente);
                },
                error -> Log.e("AuthDemo", "Failed to fetch user attributes.", error)
        );


        return nuovoUtente;
    }


}


