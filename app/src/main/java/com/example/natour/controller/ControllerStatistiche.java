package com.example.natour.controller;

import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.natour.model.dao.ImmagineDAO;
import com.example.natour.model.dao.ItinerarioDAO;
import com.example.natour.model.dao.RecensioneDAO;
import com.example.natour.model.dao.SegnalazioneDAO;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.PannelloAdmin.VisualizzaStatistiche;

import org.json.JSONObject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerStatistiche
{
    private final VisualizzaStatistiche context;
    private final Handler waitPolling;
    private ItinerarioDAO itinerarioDAO;
    private PublishSubject<JSONObject> itinerari;
    private Disposable disposableItinerari;
    private ImmagineDAO immagineDAO;
    private PublishSubject<JSONObject> immagini;
    private Disposable disposableImmagini;
    private SegnalazioneDAO segnalazioneDAO;
    private PublishSubject<JSONObject> segnalazioni;
    private Disposable disposableSegnalazioni;
    private RecensioneDAO recensioneDAO;
    private PublishSubject<JSONObject> recensioni;
    private Disposable disposableRecensioni;
    private final RequestQueue queue;

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
                waitPolling.postDelayed(pollingProcess, 5 * 1000);
            }
        }
    };
    public ControllerStatistiche(VisualizzaStatistiche context)
    {
        this.context = context;
        queue = Volley.newRequestQueue(context.requireContext());
        waitPolling = new Handler();
        itinerarioDAO = new ItinerarioDAO();
        recensioneDAO = new RecensioneDAO();
        segnalazioneDAO = new SegnalazioneDAO();
        immagineDAO = new ImmagineDAO();
        itinerari = itinerarioDAO.getCountItinerari(context.requireContext());
        recensioni = recensioneDAO.getCountRecensioni(context.requireContext());
        segnalazioni = segnalazioneDAO.getCountSegnalazioni(context.requireContext());
        immagini = immagineDAO.getCountImmagini(context.requireContext());
        disposableItinerari = itinerari.subscribe(
                success ->
                {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(() ->
                            context.updateItinerari(num));
                },
                error ->
                {
                }
        );
        disposableRecensioni = recensioni.subscribe(
                success ->
                {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()->{
                        context.updateRecensioni(num);
                    });
                },
                error ->{}
        );
        disposableSegnalazioni = segnalazioni.subscribe(
                success ->
                {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()->{
                        context.updateSegnalazioni(num);
                    });
                },
                error ->{}
        );

        disposableImmagini = immagini.subscribe(
                success->
                {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()->
                    {
                        context.updateImmagini(num);
                    });
                }

        );
        startPolling();
    }

    private void startPolling()
    {
        pollingProcess.run();
    }

    public void aggiornaStatisticheItinerario()
    {
        /*if(disposableItinerari != null){
            resetObservers();
        }*/
        itinerarioDAO.getCountItinerari(context.requireContext(), itinerari, queue);
        recensioneDAO.getCountRecensioni(context.requireContext(),recensioni, queue);
        segnalazioneDAO.getCountSegnalazioni(context.requireContext(),segnalazioni, queue);
        immagineDAO.getCountImmagini(context.requireContext(),immagini, queue);


    }

    private void resetObservers()
    {
        disposableItinerari.dispose();
        disposableImmagini.dispose();
        disposableRecensioni.dispose();
        disposableSegnalazioni.dispose();
        disposableItinerari = null;
        disposableImmagini = null;
        disposableRecensioni = null;
        disposableSegnalazioni = null;
        JSONObject jsonObject = new JSONObject();
        itinerari.onNext(jsonObject);
        immagini.onNext(jsonObject);
        recensioni.onNext(jsonObject);
        segnalazioni.onNext(jsonObject);
        itinerari.onComplete();
        immagini.onComplete();
        recensioni.onComplete();
        segnalazioni.onComplete();
        itinerari = new ItinerarioDAO().getCountItinerari(context.requireContext());
        recensioni = new RecensioneDAO().getCountRecensioni(context.requireContext());
        segnalazioni = new SegnalazioneDAO().getCountSegnalazioni(context.requireContext());
        immagini = new ImmagineDAO().getCountImmagini(context.requireContext());
    }


    public void stopPolling()
    {
        waitPolling.removeCallbacks(pollingProcess);
        disposableItinerari.dispose();
        disposableImmagini.dispose();
        disposableRecensioni.dispose();
        disposableSegnalazioni.dispose();
        itinerari.onComplete();
        immagini.onComplete();
        recensioni.onComplete();
        segnalazioni.onComplete();
    }
}
