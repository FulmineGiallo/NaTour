package com.example.natour.controller;

import android.util.Log;

import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.rx.RxStorageBinding;
import com.amplifyframework.storage.result.StorageDownloadFileResult;
import com.example.natour.model.Itinerario;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;

import java.io.File;

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

        RxStorageBinding.RxProgressAwareSingleOperation<StorageDownloadFileResult> download =
                RxAmplify.Storage.downloadFile(
                        key,
                        new File(activity.getCacheDir() + "/" + key +".txt")
                );
        download
                .observeResult()
                .subscribe(
                        result -> Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName()),
                        error -> Log.e("MyAmplifyApp",  "Download Failure", error)
                );



    }



}
