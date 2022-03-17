package com.example.natour.view.admin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.natour.model.dao.ItinerarioDAO;

public class MyAlarmManager extends BroadcastReceiver
{
    Context context;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;
        //connect to server..
        ItinerarioDAO itinerarioDAO = new ItinerarioDAO();
        itinerarioDAO.getCountItinerari(context).subscribe(
                result ->
                {
                    Log.i("ITINERARI TOTALI", result.getString("numero"));
                },
                error -> Log.e("ERROR", error.getLocalizedMessage())
        );
    }
}