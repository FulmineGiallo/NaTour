package com.example.natour.model.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import com.example.natour.model.daointerface.UtenteDaoInterface;
import com.example.natour.model.Utente;
import com.example.natour.view.Login;

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

public class UtenteDAO
{
    Map<String, String> parmasAPI = new HashMap<String, String>();

    public int utenteExist(String email, String password, Context activityPrec) throws SQLException
    {
        /* L'idea è di creare Un hashMap e passarla a volley per metterla nel body */
        String URL = "http://ec2-52-210-159-218.eu-west-1.compute.amazonaws.com/natour21/api/utente/login.php";

        /* Per cercare un utente nel database bisogna mettere l'email e la password */
        parmasAPI.put("email", email);
        parmasAPI.put("password", password);

        /*RequestAPI request = new RequestAPI(URL, activityPrec, parmasAPI);*/



        return 0;
    }
}


