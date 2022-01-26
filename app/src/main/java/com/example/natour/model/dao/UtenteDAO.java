package com.example.natour.model.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
    Map<String, String> parmasAPI = new HashMap<String, String>();

    public void setTokenUtente(String idUtente, String dataDiNascita, Context activityPrec)
    {
        /* L'idea è di creare Un hashMap e passarla a volley per metterla nel body */
        String URL = "http://ec2-34-243-75-95.eu-west-1.compute.amazonaws.com/natour21/api/utente/insertToken.php";

        /* Insert Token e Data Di Nascita nella registrazione*/
        parmasAPI.put("token", idUtente);
        parmasAPI.put("dataDiNascita", dataDiNascita);


        RequestAPI request = new RequestAPI(URL, activityPrec, parmasAPI);
        JSONObject responseAPI = request.sendRequest();




    }


    public Utente getInformationOfAmplifySession()
    {
        Utente nuovoUtente = new Utente();


        Amplify.Auth.fetchUserAttributes(
                result -> {
                    AuthUserAttribute id = result.get(3);
                    StringBuffer idUtente = new StringBuffer(id.getValue());
                    String token;
                    token = idUtente.substring(idUtente.indexOf("userId\":\"") + 9, idUtente.indexOf(",") - 1 );

                    nuovoUtente.setToken(token);
                    Log.i("NAME", nuovoUtente.getToken());
                },
                error -> Log.e("Error", error.getLocalizedMessage())

        );


        return nuovoUtente;
    }


}


