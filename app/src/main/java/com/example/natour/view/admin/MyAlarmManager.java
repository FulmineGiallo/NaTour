package com.example.natour.view.admin;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.example.natour.model.dao.ItinerarioDAO;
import com.google.common.util.concurrent.ListenableFuture;

public class MyAlarmManager extends ListenableWorker
{
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public MyAlarmManager(@NonNull Context appContext, @NonNull WorkerParameters workerParams)
    {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork()
    {
        //connect to server..
        Log.i("help","me");
        ItinerarioDAO itinerarioDAO = new ItinerarioDAO();
        itinerarioDAO.getCountItinerari(getApplicationContext()).subscribe(
                result ->
                {
                    Log.i("ITINERARI TOTALI", result.getString("numero"));
                },
                error -> Log.e("ERROR", error.getLocalizedMessage() + "ciao")
        );
        return null;
    }
}