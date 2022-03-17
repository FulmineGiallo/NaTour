package com.example.natour.controller;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.natour.view.admin.MyAlarmManager;

public class ControllerStatistiche
{
    Fragment context;
    public ControllerStatistiche(Fragment context)
    {
        this.context = context;
    }
    public void aggiornaStatisticheItinerario()
    {
        Intent intent = new Intent(context.requireActivity(), MyAlarmManager.class);
        long scTime = 10000; //1mins
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.requireActivity(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.requireActivity().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + scTime, pendingIntent);

    }
}
