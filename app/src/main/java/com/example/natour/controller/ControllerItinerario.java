package com.example.natour.controller;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;
import android.widget.FrameLayout;

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
import com.example.natour.view.InserimentoItinerarioActivity.MappaFragment;
import com.example.natour.view.adapter.ImageAdapter;
import com.example.natour.view.dialog.ErrorDialog;

import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerItinerario
{
    private String nomeItinerario;
    private String durataItinerario;
    private boolean disabiliItinerario;
    private int difficoltàItinerario; //Ha 5 possibili valori, da 1 a 5
    private File gpx;
    private String descrizioneItnerario;
    private static final int PICKFILE_REQUEST_CODE = 100;
    private ImageAdapter imageAdapter;

    /* Coppia valore Key = URI */
    private List<Immagine> mapKeyURI;



    private final FragmentManager fragmentManager;
    private final InserimentoItinerario inserimentoItinerarioActivity;
    private InserimentoPercorsoFragment percorsoFragment;
    private InserimentoItinerarioFragment inserimentoItinerarioFragment;
    private MappaFragment mappaFragment;


    public Itinerario inserisciItinerario(String nome, String durata, boolean disabili, File gpx, String descrizione, List<String> immagini)
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

    public void gotoPercorsoFragment(FrameLayout imageView)
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
        if (fragment.requireView().findViewById(viewId) != null)
        {
            mappaFragment = new MappaFragment(this);
            FragmentTransaction ft = fragmentManager.beginTransaction();


            ft.add(viewId, mappaFragment);
            ft.commit();

        }
    }

    public void resetMapView(InserimentoPercorsoFragment ipf, int map)
    {
        if (percorsoFragment.requireView().findViewById(map) != null)
        {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.remove(mappaFragment);

            mappaFragment = new MappaFragment(this);

            mappaFragment.setEditTextMappa(ipf.getInizioPercorso(), ipf.getFinePercorso(),
                    ipf.getDeleteMarkerInizio(), ipf.getDeleteMarkerFine());
            ft.add(map, mappaFragment);
            ft.commit();


        }
    }


    public String getAddress(GeoPoint point)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(inserimentoItinerarioActivity, Locale.getDefault());
        String address = null;
        String city = null;
        String postalCode = null;
        try
        {
            addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            postalCode = addresses.get(0).getPostalCode();
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        return address + city + postalCode;
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
        } catch (IOException e)
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
                    uploadOnS3Bucket(immagine.getUri(), immagine.getKey(), mapKeyURI.indexOf(immagine));
                }
            }
        }
    }
    public boolean controlloUriDoppio(List<Immagine> immagini, Immagine findUri)
    {
        return immagini.contains(findUri);
    }

    public void uploadOnS3Bucket(Uri uriImage, String key, int positionImage)
    {
        try
        {
            InputStream exampleInputStream = inserimentoItinerarioActivity.getContentResolver().openInputStream(uriImage);
            RxStorageBinding.RxProgressAwareSingleOperation<StorageUploadInputStreamResult> rxUploadOperation =
                    RxAmplify.Storage.uploadInputStream(key, exampleInputStream);


            rxUploadOperation
                    .observeResult()
                    .subscribe(
                            result ->
                            {
                                Log.i("MyAmplifyApp", "Successfully uploaded: ");
                                RxAmplify.Storage.getUrl(key).subscribe(
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
                                                            /* recupero metadati */
                                                            getMetadatiImage(exampleInputStream, uriImage);
                                                        }

                                                        else
                                                        {
                                                            removeImageFromS3Bucket(key, positionImage);
                                                            new ErrorDialog("L'immagine che hai inserito è esplicità, è stata rimossa!").show(fragmentManager, null);
                                                        }
                                                    },
                                                    imageError ->
                                                    {
                                                        removeImageFromS3Bucket(key, positionImage);
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

    public void removeImageFromS3Bucket(String key, int positionImage)
    {
        /* Rimozione dalla Lista e poi dal Bucket S3 */
        mapKeyURI.remove(positionImage);
        imageAdapter.notifyItemRemoved(positionImage);

        /* Rimozione da S3 */
        RxAmplify.Storage.remove(key)
                .subscribe(
                        onRemove -> Log.i("MyAmplifyApp", "Successfully removed: " + onRemove.getKey()),
                        error -> Log.e("MyAmplifyApp", "Remove failure", error)
                );
    }

    public void removeImageFromS3Bucket(String key)
    {
        /* Rimozione da S3 */
        RxAmplify.Storage.remove(key)
                .subscribe(
                        onRemove -> Log.i("MyAmplifyApp", "Successfully removed: " + onRemove.getKey()),
                        error -> Log.e("MyAmplifyApp", "Remove failure", error)
                );
    }

    public void removeOnBackPressedImage()
    {
        for(Immagine img : mapKeyURI)
        {
            removeImageFromS3Bucket(img.getKey());
        }
        mapKeyURI.clear();
        inserimentoItinerarioActivity.finish();
    }

    public void updateScrollViewImage(RecyclerView mRecyclerView)
    {
        LinearLayoutManager linearLayout = new LinearLayoutManager(inserimentoItinerarioFragment.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setAdapter(imageAdapter);


    }

    public void getMetadatiImage(InputStream immagine, Uri uriImage)
    {
        /* Se la posizione dell'immagine è presente */
        try
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            {
                immagine = inserimentoItinerarioActivity.getContentResolver().openInputStream(uriImage);
                ExifInterface metadati = new ExifInterface(immagine);
                float[] latLong = new float[2];
                if(metadati.getLatLong(latLong))
                {
                    Log.i("Metadati", "POS" + latLong[0] + " " + latLong[1]);

                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }
}
