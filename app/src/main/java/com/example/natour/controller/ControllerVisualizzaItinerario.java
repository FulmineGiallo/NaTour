package com.example.natour.controller;

import android.util.Log;

import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.rx.RxStorageBinding;
import com.amplifyframework.storage.result.StorageDownloadFileResult;
import com.example.natour.model.Itinerario;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;

import org.osmdroid.util.GeoPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ControllerVisualizzaItinerario
{
    private VisualizzaItinerarioActivity activity;
    private Itinerario itinerario;
    public ControllerVisualizzaItinerario(VisualizzaItinerarioActivity activity, Itinerario itinerario)
    {
        this.activity = activity;
        this.itinerario = itinerario;
    }


    public void getWaypointsFromItinerario()
    {
        String key = itinerario.getNomeFile();
        StringBuilder text = new StringBuilder();
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



}
