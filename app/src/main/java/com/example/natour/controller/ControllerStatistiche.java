package com.example.natour.controller;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import com.example.natour.model.dao.ItinerarioDAO;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.admin.MyAlarmManager;
import com.example.natour.view.admin.VisualizzaStatistiche;

public class ControllerStatistiche
{
    private VisualizzaStatistiche context;
    private Handler waitPolling;

    private final Runnable pollingProcess = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                aggiornaStatisticheItinerario();
            } finally
            {
                waitPolling.postDelayed(pollingProcess, 5000);
            }
        }
    };
    public ControllerStatistiche(VisualizzaStatistiche context)
    {
        this.context = context;
        waitPolling = new Handler();
        startPolling();
    }

    private void startPolling()
    {
        pollingProcess.run();
    }

    public void aggiornaStatisticheItinerario()
    {
        /*Intent intent = new Intent(context.requireActivity(), MyAlarmManager.class);
        long scTime = 10000; //1mins
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.requireActivity(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.requireActivity().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scTime, pendingIntent);*/
        new ItinerarioDAO().getCountItinerari(context.requireContext()).subscribe(
                success->{
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()->{
                        context.updateItinerari(num);
                    });
                },
                error ->{}
        );
    }


    public void stopPolling()
    {
        waitPolling.removeCallbacks(pollingProcess);
    }
}
