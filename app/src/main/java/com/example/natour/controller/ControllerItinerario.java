package com.example.natour.controller;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.rx.RxStorageBinding;
import com.amplifyframework.storage.result.StorageUploadInputStreamResult;
import com.example.natour.R;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.connection.RequestAPI;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerario;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerarioFragment;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoPercorsoFragment;
import com.example.natour.view.adapter.ImageAdapter;
import com.example.natour.view.dialog.ErrorDialog;

import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import io.reactivex.rxjava3.subjects.PublishSubject;
import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;

public class ControllerItinerario implements LocationListener
{
    private String nomeItinerario;
    private String durataItinerario;
    private boolean disabiliItinerario;
    private int difficoltàItinerario; //Ha 5 possibili valori, da 1 a 5
    private File gpx;
    private String descrizioneItnerario;
    private static final int PICKFILE_REQUEST_CODE = 100;
    private ImageAdapter imageAdapter;
    private Boolean gpxInserted = Boolean.FALSE;

    //informazioni della mappa
    private final ArrayList<GeoPoint> waypoints = new ArrayList<>();
    private final LinkedList<Immagine> listPhotoPoints = new LinkedList<>();
    private GeoPoint inizioPercorso;
    private GeoPoint finePercorso;

    /* Coppia valore Key = URI */
    private List<Immagine> mapKeyURI;



    private final FragmentManager fragmentManager;
    private final InserimentoItinerario inserimentoItinerarioActivity;
    private InserimentoPercorsoFragment percorsoFragment;
    private InserimentoItinerarioFragment inserimentoItinerarioFragment;


    public Itinerario inserisciItinerario(String nome, String durata, boolean disabili, String descrizione)
    {
        Itinerario itinerarioInserito = new Itinerario();
        /* INSERIMENTO DELL'ID ALL'INTERNO DEL DATABASE E DELLE SUE INFORMAZIONI DI BASE */
        /* Chiamato all'ItinerarioDAO */


        return itinerarioInserito;
    }

    public ControllerItinerario(FragmentManager fragmentManager, InserimentoItinerario inserimentoItinerarioActivity)
    {
        this.fragmentManager = fragmentManager;
        this.inserimentoItinerarioActivity = inserimentoItinerarioActivity;
        this.mapKeyURI = new ArrayList<>();
        this.imageAdapter = new ImageAdapter(mapKeyURI, fragmentManager, this);

    }

    public void inizializzaInterfaccia()
    {
        if (inserimentoItinerarioActivity.findViewById(R.id.fragmentMappa) != null)
        {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            inserimentoItinerarioFragment = new InserimentoItinerarioFragment(this);
            ft.add(R.id.fragmentMappa, inserimentoItinerarioFragment);
            ft.commit();

        }
    }

    public void gotoPercorsoFragment(MapView imageView)
    {
        if (percorsoFragment == null)
            percorsoFragment = new InserimentoPercorsoFragment(this);

        fragmentManager
                .beginTransaction()
                .addSharedElement(imageView, "big_map")
                .replace(R.id.fragmentMappa, percorsoFragment)
                .addToBackStack(InserimentoItinerarioFragment.class.getSimpleName())
                .commit();
    }


    public void goBack()
    {
        inserimentoItinerarioActivity.onBackPressed();
        imageAdapter.notifyDataSetChanged();
    }

    public void setMapView(Fragment fragment, int viewId)
    {

    }

    public void resetMapView(InserimentoPercorsoFragment ipf, int map)
    {

    }


    public String getAddress(GeoPoint point)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(inserimentoItinerarioActivity, Locale.getDefault());
        String address = null;
        try
        {
            addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        return address;
    }

    public GeoPoint currentPositionPhone()
    {
        LocationManager locManager = (LocationManager) inserimentoItinerarioActivity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(inserimentoItinerarioActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(inserimentoItinerarioActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);
            Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude;
            double latitude;
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            return new GeoPoint(latitude, longitude);
        } else
            return new GeoPoint(40.839326626673405, 14.185227261143826);
    }

    public GeoPoint getLocationFromAddress(String strAddress)
    {
        Geocoder coder = new Geocoder(inserimentoItinerarioActivity);
        List<Address> address;
        GeoPoint p1;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint(location.getLatitude(), location.getLongitude());

            return p1;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void uploadFile()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(inserimentoItinerarioActivity.getPackageManager()) != null)
            inserimentoItinerarioActivity.startActivityForResult(intent, PICKFILE_REQUEST_CODE);

    }

    public void fileInserito(int requestCode, int resultCode, Intent intent)
    {

        Uri singolaFoto;
        String singolaKey = UUID.randomUUID().toString();


        if (resultCode == RESULT_OK)
        {
            /* Inserimento singolo di un immagine */
            if(intent.getData() != null)
            {
                singolaFoto = intent.getData();
                Immagine immagine = new Immagine(singolaFoto, singolaKey);



                if(!controlloUriDoppio(mapKeyURI, immagine))
                {
                    mapKeyURI.add(immagine);
                    imageAdapter.notifyItemInserted(mapKeyURI.indexOf(immagine));
                    uploadOnS3Bucket(immagine, mapKeyURI.indexOf(immagine));
                }
            }
        }
    }
    public boolean controlloUriDoppio(List<Immagine> immagini, Immagine findUri)
    {
        return immagini.contains(findUri);
    }

    public void uploadOnS3Bucket(Immagine immagine, int positionImage)
    {
        try
        {
            InputStream exampleInputStream = inserimentoItinerarioActivity.getContentResolver().openInputStream(immagine.getUri());
            RxStorageBinding.RxProgressAwareSingleOperation<StorageUploadInputStreamResult> rxUploadOperation =
                    RxAmplify.Storage.uploadInputStream(immagine.getKey(), exampleInputStream);


            rxUploadOperation
                    .observeResult()
                    .subscribe(
                            result ->
                            {
                                Log.i("MyAmplifyApp", "Successfully uploaded: ");
                                RxAmplify.Storage.getUrl(immagine.getKey()).subscribe(
                                        risultato -> {
                                            Log.i("MyAmplifyApp", "Successfully generated: " + risultato.getUrl());
                                            String URL = "https://besimsoft.com/.natour21/uq6PMpSfiZ/api/itinerary/nfws";
                                            String URLImage = risultato.getUrl().toString();
                                            //Controllo effettuata sulla singola aggiunta
                                            Map<String, String> paramsUrl = new HashMap<>();
                                            paramsUrl.put("image_url", URLImage);
                                            RequestAPI requestImage = new RequestAPI("", inserimentoItinerarioActivity, paramsUrl);
                                            requestImage.setEndpoint(URL);
                                            PublishSubject<JSONObject> response = requestImage.sendRequest();
                                            response.subscribe(
                                                    imageResult ->
                                                    {
                                                        if(imageResult.getBoolean("is_save"))
                                                        {
                                                            Log.i("CONFERMA IMG", "Rimane nel Bucket, immagine valida");
                                                            immagine.setURL(URLImage);
                                                            /* recupero metadati */
                                                            getMetadatiImage(exampleInputStream, immagine);
                                                        }

                                                        else
                                                        {
                                                            removeImageFromS3Bucket(immagine, positionImage);
                                                            new ErrorDialog("L'immagine che hai inserito è esplicità, è stata rimossa!").show(fragmentManager, null);
                                                        }
                                                    },
                                                    imageError ->
                                                    {
                                                        removeImageFromS3Bucket(immagine, positionImage);
                                                        new ErrorDialog("Errore nel caricamento dell'ìmmagine, riprova con un'altra immagine!").show(fragmentManager, null);
                                                    }
                                            );
                                        },
                                        error -> Log.e("MyAmplifyApp", "URL generation failure", error)
                                );

                            },
                            error -> Log.e("MyAmplifyApp", "Upload failed", error)
                    );
        }
        catch (FileNotFoundException error)
        {
            Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
        }
    }

    public void removeImageFromS3Bucket(Immagine img, int positionImage)
    {
        /* Rimozione dalla Lista e poi dal Bucket S3 */
        mapKeyURI.remove(positionImage);
        imageAdapter.notifyItemRemoved(positionImage);

        /* Rimozione da S3 */
        RxAmplify.Storage.remove(img.getKey())
                .subscribe(
                        onRemove ->
                        {
                            Log.i("MyAmplifyApp", "Successfully removed: " + onRemove.getKey());
                        },
                        error ->
                        {
                            Log.e("MyAmplifyApp", "Remove failure", error);
                        }
                );
    }

    public void removeImageFromS3Bucket(Immagine img)
    {


        /* Rimozione da S3 */
        RxAmplify.Storage.remove(img.getKey())
                .subscribe(
                        onRemove ->
                        {
                            Log.i("MyAmplifyApp", "Successfully removed: " + onRemove.getKey());

                        },
                        error ->
                        {
                            Log.e("MyAmplifyApp", "Remove failure", error);
                        }
                );
    }

    public void removeOnBackPressedImage()
    {
        for(Immagine img : mapKeyURI)
        {
            removeImageFromS3Bucket(img);
        }
        mapKeyURI.clear();
    }

    public void updateScrollViewImage(RecyclerView mRecyclerView)
    {
        LinearLayoutManager linearLayout = new LinearLayoutManager(inserimentoItinerarioFragment.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setAdapter(imageAdapter);


    }

    public void getMetadatiImage(InputStream immagine, Immagine uriImage)
    {
        /* Se la posizione dell'immagine è presente */
        try
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            {
                immagine = inserimentoItinerarioActivity.getContentResolver().openInputStream(uriImage.getUri());
                ExifInterface metadati = new ExifInterface(immagine);
                float[] latLong = new float[2];
                if(metadati.getLatLong(latLong))
                {
                    Log.i("Metadati", "POS" + latLong[0] + " " + latLong[1]);
                    /*mappaFragment.addPhotoMarker(uriImage.getMarker());*/
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public void getGPXFromDevice()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/xml");
        Log.i("LANCIO", "GPX");
        if (intent.resolveActivity(inserimentoItinerarioActivity.getPackageManager()) != null)
            inserimentoItinerarioActivity.startActivityForResult(intent, 20);

    }
    public void insertGPX(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == 20)
            if (resultCode == RESULT_OK)
            {
                String fileName = inserimentoItinerarioActivity.getContentResolver().getType(intent.getData());
                Log.e("FILENAME", fileName);
                // Inserito il file dal File Explorer viene effettuato un controllo per garantire che il file sia del tipo .gpx
                if (fileName.contains("xml"))
                {
                    // Essendo il file di tipo .gpx vengono presi dall'intent le informazioni per gestire l'itinerario
                    GPXParser parser = new GPXParser();
                    Gpx parsedGpx = null;
                    try
                    {
                        InputStream in = inserimentoItinerarioActivity.getContentResolver().openInputStream(intent.getData());
                        parsedGpx = parser.parse(in);
                    }
                    catch (IOException  | XmlPullParserException e)
                    {
                        new ErrorDialog("Errore nel caricamento del file .gpx").show(fragmentManager, null);
                        e.printStackTrace();
                    }
                    if (parsedGpx == null)
                    {
                        new ErrorDialog("Errore nel caricamento del file .gpx").show(fragmentManager, null);
                    }
                    else
                    {
                        /*
                            Nei casi precedenti viene controllato che il file gpx sia stato correttamente  analizzato dal parser
                            Ed entra in questo else solamente se vengono passati tutti i controlli procedendo poi a recuperare eventuali routes e waypoint
                        */
                        if(parsedGpx.getTracks() != null)
                        {
                            waypoints.clear();
                            gpxInserted = Boolean.TRUE;
                            for(int i = 0; i < parsedGpx.getTracks().size(); i++){
                                for(int j = 0; j < parsedGpx.getTracks().get(i).getTrackSegments().get(i).getTrackPoints().size(); j++){
                                    waypoints.add(new GeoPoint(parsedGpx.getTracks().get(i).getTrackSegments().get(i).getTrackPoints().get(j).getLatitude(), parsedGpx.getTracks().get(i).getTrackSegments().get(i).getTrackPoints().get(j).getLongitude()));
                                    Log.i("WAYPOINTS", waypoints.get(j).getLatitude() + "    " + waypoints.get(j).getLongitude());
                                }
                            }
                            /* Creare Route nel mappaFragment */
                            Log.e("WAYPOINTS SIZE", String.valueOf(waypoints.size()));
                        }

                    }
                }
                else
                {
                    new ErrorDialog("Il file inserito non è di tipo .gpx").show(fragmentManager, null);
                }
            }
    }

    public void conservaInizio(GeoPoint p)
    {
        inizioPercorso = p;
    }

    public void conservaFine(GeoPoint p)
    {
        finePercorso = p;
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider)
    {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider)
    {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }
}
