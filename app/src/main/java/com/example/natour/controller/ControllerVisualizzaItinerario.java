package com.example.natour.controller;

import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.rx.RxStorageBinding;
import com.amplifyframework.storage.result.StorageDownloadFileResult;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.Recensione;
import com.example.natour.model.dao.ImmagineDAO;
import com.example.natour.model.dao.RecensioneDAO;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;
import com.example.natour.view.adapter.NotDeletableImageAdapter;
import com.example.natour.view.adapter.RecensioniAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerVisualizzaItinerario
{
    private NotDeletableImageAdapter imageAdapter;
    private VisualizzaItinerarioActivity activity;
    private Itinerario itinerario;
    private String tokenUtenteLoggato;
    private ArrayList<Recensione> recensioni = new ArrayList<>();
    private RecensioniAdapter recensioneAdapter;

    public ControllerVisualizzaItinerario(VisualizzaItinerarioActivity activity, Itinerario itinerario, String token)
    {
        this.activity = activity;
        this.itinerario = itinerario;
        LinkedList<Immagine> listImmagine = new LinkedList<>();
        itinerario.setImmagini(listImmagine);
        this.tokenUtenteLoggato = token;
    }


    public void getWaypointsFromItinerario()
    {
        String key = itinerario.getNomeFile();
        LinkedList<Float> lat = new LinkedList<>();
        LinkedList<Float> longitudine = new LinkedList<>();

        RxStorageBinding.RxProgressAwareSingleOperation<StorageDownloadFileResult> download =
                RxAmplify.Storage.downloadFile(
                        key,
                        new File(activity.getApplicationContext().getFilesDir() + "/download.txt")
                );

        download
                .observeResult()
                .subscribe(
                        result ->
                        {
                            Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getAbsolutePath());
                            try
                            {
                                BufferedReader br = new BufferedReader(new FileReader(result.getFile()));
                                String line;
                                int i = 0;
                                while ((line = br.readLine()) != null)
                                {
                                    if(i % 2 == 0)
                                    {
                                        lat.add(Float.parseFloat(line));
                                    }
                                    else
                                    {
                                        longitudine.add(Float.parseFloat(line));
                                    }
                                    i++;
                                }
                                br.close();
                            }
                            catch (IOException e)
                            {
                                Log.e("ERROR DOWNLOAD FILE", e.getLocalizedMessage());
                            }
                            ArrayList<GeoPoint> waypoints = new ArrayList<>();
                            for(int j = 0; j < lat.size(); j++)
                            {
                                waypoints.add(new GeoPoint(lat.get(j), longitudine.get(j)));
                            }
                            itinerario.setWaypoints(waypoints);
                            for(GeoPoint p : waypoints)
                                Log.i("WAYPOINTS in LISTA", p.getLatitude() + " " + p.getLongitude());

                            activity.setMappa();
                        },
                        error -> Log.e("MyAmplifyApp",  "Download Failure", error)
                );



    }

    public void getImageItinerario()
    {
        /* Per caricare le foto si deve ottnere l'image view corrispondente */
        ImmagineDAO immagineDAO = new ImmagineDAO();

        PublishSubject<JSONArray> response = immagineDAO.getImageOfItinerario(itinerario, this.activity.getApplicationContext());
        response.subscribe(
                result ->
                {

                    for(int i = 0; i < result.length(); i++)
                    {
                        JSONObject jsonObject = result.getJSONObject(i);
                        Immagine immagine = new Immagine(null, jsonObject.getString("id_key"));
                        immagine.setLatitude(Float.parseFloat(jsonObject.getString("lat_posizione")));
                        immagine.setLongitude(Float.parseFloat(jsonObject.getString("long_posizione")));
                        itinerario.getImmagini().add(immagine);
                        Log.i("IN FOR", immagine.toString());
                    }
                    setURLImmagine();
                },
                error ->
                {

                }
        );
    }

    public void setURLImmagine()
    {
        for(Immagine img: itinerario.getImmagini()){
            RxAmplify.Storage.getUrl(img.getKey()).subscribe(
                    urlResult ->
                    {
                        Log.i("MyAmplifyApp", "Successfully generated: " + urlResult.getUrl());
                        img.setURL(urlResult.getUrl().toString());
                        activity.setImage(img);
                        activity.runOnUiThread(()->imageAdapter.notifyItemChanged(itinerario.getImmagini().indexOf(img)));
                    },
                    error -> Log.e("MyAmplifyApp", "URL generation failure", error)
            );
        }
    }

    public void setAdapter(RecyclerView mRecyclerView)
    {
        imageAdapter = new NotDeletableImageAdapter(itinerario.getImmagini(), activity.getSupportFragmentManager(), this, activity);
        LinearLayoutManager linearLayout = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setAdapter(imageAdapter);


    }

    public void showSegnalazioni()
    {
        //todo: mostrare il bottom sheet con le info delle segnalazioni
    }

    public void setRecensioniAdapter(RecyclerView mRec)
    {
        recensioneAdapter = new RecensioniAdapter(recensioni, activity.getSupportFragmentManager(), this, activity);
        LinearLayoutManager linearLayout = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        mRec.setLayoutManager(linearLayout);
        mRec.setAdapter(recensioneAdapter);
    }

    public void insertRecensione(int rate, String testoRecensione)
    {
        RecensioneDAO recensioneDAO = new RecensioneDAO();
        recensioneDAO.insertRecensione(rate, testoRecensione, tokenUtenteLoggato, itinerario.getIdItinerario(), activity);
    }

    public void getRecensioneItinerario(TextView mediaTotale)
    {
        RecensioneDAO recensioneDAO = new RecensioneDAO();
        UtenteDAO utenteDAO = new UtenteDAO();


        PublishSubject<JSONArray> result = recensioneDAO.getRecensioni(activity, itinerario.getIdItinerario());
        result.subscribe(
                onResult ->
                {
                    for(int i = 0; i < onResult.length(); i++)
                    {
                        JSONObject jsonObject = onResult.getJSONObject(i);
                        Recensione recensione = new Recensione();
                        recensione.setValutazione(Integer.parseInt(jsonObject.getString("valutazione")));
                        recensione.setTesto(jsonObject.getString("testo"));
                        PublishSubject<JSONObject> resultUtente = utenteDAO.getNomeCognomeUtente(jsonObject.getString("fk_utente"), activity);
                        resultUtente.subscribe(
                                utente ->
                                {
                                    recensione.setUtente(utente.getString("nome") + " " + utente.getString("cognome"));
                                    recensione.setFk_itinerario(itinerario.getIdItinerario());

                                    activity.runOnUiThread(()->recensioneAdapter.notifyItemChanged(recensioni.indexOf(recensione)));
                                },
                                errorUtente ->
                                {

                                }
                        );
                        Log.i("RECENSIONE", recensione.toString());
                        recensioni.add(recensione);
                    }

                    calcoloMediaRecensioni(mediaTotale);

                },
                onError ->
                {

                }
        );

    }
    public void calcoloMediaRecensioni(TextView mediaTotale)
    {
        int sommaTotale = 0;
        float media = 0;
        for(Recensione r : recensioni)
        {
            sommaTotale = sommaTotale + r.getValutazione();
        }
        media = (float) sommaTotale / (float) recensioni.size();

        mediaTotale.setText("MEDIA TOTALE: " + String.valueOf(media));


    }

}
