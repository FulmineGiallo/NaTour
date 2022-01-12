package com.example.natour.model.connection;

import android.content.Context;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RequestAPI
{
    private String URL;
    private Context context;
    private JSONObject response;

    RequestAPI(String URL, Context context)
    {
        this.URL = URL;
        this.context = context;
    }
}