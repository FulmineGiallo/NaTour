package com.example.natour.controller;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.rx.RxStorageBinding;
import com.amplifyframework.storage.result.StorageDownloadFileResult;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.dao.ImmagineDAO;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;
import com.example.natour.view.adapter.ImageAdapter;
import com.example.natour.view.adapter.MasonryAdapter;
import com.example.natour.view.adapter.NotDeletableImageAdapter;
import com.example.natour.view.adapter.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

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
    public ControllerVisualizzaItinerario(VisualizzaItinerarioActivity activity, Itinerario itinerario)
    {
        this.activity = activity;
        this.itinerario = itinerario;
        LinkedList<Immagine> listImmagine = new LinkedList<>();
        itinerario.setImmagini(listImmagine);
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

                    for(int i = 0; i < result.length(); i++){
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

    public void setURLImmagine(){
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
        /*SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);*/

    }

}
