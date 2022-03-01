package com.example.natour.controller;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.util.Log;

import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.rx.RxStorageBinding;
import com.amplifyframework.storage.result.StorageDownloadFileResult;
import com.example.natour.model.Itinerario;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
        File file = new File(getApplicationContext().getFilesDir() + "/" + key +".txt");
        if (!file.exists())
        {
            file.mkdirs();
        }
        RxStorageBinding.RxProgressAwareSingleOperation<StorageDownloadFileResult> download =
                RxAmplify.Storage.downloadFile(
                        key,
                        file
                );
        download
                .observeResult()
                .subscribe(
                        result ->
                        {
                            Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getAbsolutePath());
                            try
                            {
                                BufferedReader br = new BufferedReader(new FileReader(file));
                                String line;

                                while ((line = br.readLine()) != null)
                                {
                                    text.append(line);
                                    text.append('\n');
                                }
                                br.close();
                            }
                            catch (IOException e)
                            {
                                //You'll need to add proper error handling here
                            }
                            Log.i("COORDINATE", String.valueOf(text));
                        },
                        error -> Log.e("MyAmplifyApp",  "Download Failure", error)
                );



    }



}
