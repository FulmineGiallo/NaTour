package com.example.natour.controller;

import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.natour.model.dao.ImmagineDAO;
import com.example.natour.model.dao.ItinerarioDAO;
import com.example.natour.model.dao.RecensioneDAO;
import com.example.natour.model.dao.SegnalazioneDAO;
import com.example.natour.model.dao.StatisticheDAO;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.PannelloAdmin.VisualizzaStatistiche;

import org.json.JSONObject;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerStatistiche
{
    private final VisualizzaStatistiche context;
    private final Handler waitPolling;
    private final ItinerarioDAO itinerarioDAO;
    private final PublishSubject<JSONObject> itinerari;
    private final Disposable disposableItinerari;
    private final ImmagineDAO immagineDAO;
    private final PublishSubject<JSONObject> immagini;
    private final Disposable disposableImmagini;
    private final SegnalazioneDAO segnalazioneDAO;
    private final PublishSubject<JSONObject> segnalazioni;
    private final Disposable disposableSegnalazioni;
    private final RecensioneDAO recensioneDAO;
    private final PublishSubject<JSONObject> recensioni;
    private final Disposable disposableRecensioni;
    private final StatisticheDAO statisticheDAO;
    private final Disposable disposableLogin;
    private final Disposable disposableRicerche;
    private final PublishSubject<JSONObject> login;
    private final PublishSubject<JSONObject> ricerche;
    private final UtenteDAO utenteDAO;
    private final PublishSubject<JSONObject> utenti;
    private final Disposable disposableUtenti;

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
                waitPolling.postDelayed(pollingProcess, 3 * 1000);
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
        utenteDAO = new UtenteDAO();
        statisticheDAO = new StatisticheDAO();
        utenti = utenteDAO.getCountUtenti(context.requireContext());
        login = statisticheDAO.getCountLogin(context.requireContext());
        ricerche = statisticheDAO.getCountRicerche(context.requireContext());
        itinerari = itinerarioDAO.getCountItinerari(context.requireContext());
        recensioni = recensioneDAO.getCountRecensioni(context.requireContext());
        segnalazioni = segnalazioneDAO.getCountSegnalazioni(context.requireContext());
        immagini = immagineDAO.getCountImmagini(context.requireContext());
        disposableLogin = login.subscribe(
                success -> {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()-> context.updateLogin(num));
                },
                error -> {

                }
        );
        disposableUtenti = utenti.subscribe(
                success -> {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()-> context.updateUtenti(num));
                },
                error -> {

                }
        );
        disposableRicerche = ricerche.subscribe(
                success -> {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()-> context.updateRicerche(num));
                },
                error -> {

                }
        );
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
                    context.requireActivity().runOnUiThread(()-> context.updateRecensioni(num));
                },
                error ->{}
        );
        disposableSegnalazioni = segnalazioni.subscribe(
                success ->
                {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()-> context.updateSegnalazioni(num));
                },
                error ->{}
        );

        disposableImmagini = immagini.subscribe(
                success->
                {
                    String num = success.getString("numero");
                    context.requireActivity().runOnUiThread(()->
                            context.updateImmagini(num));
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
        statisticheDAO.getCountLogin(context.requireContext(),login,queue);
        statisticheDAO.getCountRicerche(context.requireContext(), ricerche, queue);
        utenteDAO.getCountUtenti(context.requireContext(), utenti, queue);


    }


    public void stopPolling()
    {
        waitPolling.removeCallbacks(pollingProcess);
        disposableItinerari.dispose();
        disposableImmagini.dispose();
        disposableRecensioni.dispose();
        disposableSegnalazioni.dispose();
        disposableLogin.dispose();
        disposableRicerche.dispose();
        disposableUtenti.dispose();
        login.onComplete();
        ricerche.onComplete();
        utenti.onComplete();
        itinerari.onComplete();
        immagini.onComplete();
        recensioni.onComplete();
        segnalazioni.onComplete();

    }
}
