package com.example.natour.controller;

import android.os.Handler;

import com.example.natour.model.dao.ImmagineDAO;
import com.example.natour.model.dao.ItinerarioDAO;
import com.example.natour.model.dao.RecensioneDAO;
import com.example.natour.model.dao.SegnalazioneDAO;
import com.example.natour.view.PannelloAdmin.VisualizzaStatistiche;

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
        new ItinerarioDAO().getCountItinerari(context.requireContext()).subscribe(
                success->{
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()->{
                        context.updateItinerari(num);
                    });
                },
                error ->{}
        );
        new RecensioneDAO().getCountRecensioni(context.requireContext()).subscribe(
                success ->
                {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()->{
                        context.updateRecensioni(num);
                    });
                },
                error ->{}
        );
        new SegnalazioneDAO().getCountSegnalazioni(context.requireContext()).subscribe(
                success ->
                {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()->{
                        context.updateSegnalazioni(num);
                    });
                },
                error ->{}
        );

        new ImmagineDAO().getCountImmagini(context.requireContext()) .subscribe(
                success->
                {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()->
                    {
                        context.updateImmagini(num);
                    });
                }

        );

    }


    public void stopPolling()
    {
        waitPolling.removeCallbacks(pollingProcess);
    }
}
