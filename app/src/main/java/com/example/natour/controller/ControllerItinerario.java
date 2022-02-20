package com.example.natour.controller;

import static android.app.Activity.RESULT_OK;

import android.content.ClipData;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.example.natour.model.Itinerario;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerario;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerarioFragment;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoPercorsoFragment;
import com.example.natour.view.InserimentoItinerarioActivity.MappaFragment;
import com.example.natour.view.adapter.ImageAdapter;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ControllerItinerario
{
    private String nomeItinerario;
    private String durataItinerario;
    private boolean disabiliItinerario;
    private int difficoltàItinerario; //Ha 5 possibili valori, da 1 a 5
    private File gpx;
    private String descrizioneItnerario;
    private List<String> URLImmagini; //Questa lista contiene tutti i link alle immagine inserite
    private static final int PICKFILE_REQUEST_CODE = 100;
    private List<Uri> imageItinerario;
    private ImageAdapter imageAdapter;

    private final FragmentManager fragmentManager;
    private final InserimentoItinerario inserimentoItinerarioActivity;
    private InserimentoPercorsoFragment percorsoFragment;
    private InserimentoItinerarioFragment inserimentoItinerarioFragment;

    private MappaFragment mappaFragment;
    private RecyclerView mRecyclerView;

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
        this.imageItinerario = new ArrayList<>();
        this.imageAdapter = new ImageAdapter(imageItinerario);
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if (intent.resolveActivity(inserimentoItinerarioActivity.getPackageManager()) != null)
            inserimentoItinerarioActivity.startActivityForResult(intent, PICKFILE_REQUEST_CODE);

    }

    public void fileInserito(int requestCode, int resultCode, Intent intent)
    {
        boolean imageConsentite = true;
        Uri singolaFoto;
        if (resultCode == RESULT_OK)
        {
            /* Inserimento singolo di un immagine */
            if(intent.getData() != null)
            {
                singolaFoto = intent.getData();
                if(!controlloUriDoppio(imageItinerario, singolaFoto))
                {
                    imageItinerario.add(singolaFoto);
                    imageAdapter.notifyItemInserted(imageItinerario.indexOf(singolaFoto));
                }
            }
            /* Inserimento multiplo di un immagine*/
            else if (intent.getClipData() != null)
            {
                ClipData clipData = intent.getClipData();
                ClipData.Item item;
                int i;
                for(i = 0; i < clipData.getItemCount(); i++)
                {
                    item = clipData.getItemAt(i);
                    if(!controlloUriDoppio(imageItinerario, item.getUri()))
                        imageItinerario.add(item.getUri());
                }
                imageAdapter.notifyItemRangeInserted(imageItinerario.indexOf(clipData.getItemAt(0)), clipData.getItemCount());

            }

            

            //TODO: controllo immagini non appropiate


            if(imageConsentite == true)
            {
                /* Gestire la lista List<String> URLImmagini */
            }
            else
            {
                /* Effettuare il remove sul bucket di S3 */

            }


        }
    }
    public boolean controlloUriDoppio(List<Uri> uri, Uri findUri)
    {
        return uri.contains(findUri);
    }
    public void uploadOnS3Bucket(Uri uriImage)
    {
        try
        {
            InputStream exampleInputStream = inserimentoItinerarioActivity.getContentResolver().openInputStream(uriImage);

            RxStorageBinding.RxProgressAwareSingleOperation<StorageUploadInputStreamResult> rxUploadOperation =
                    RxAmplify.Storage.uploadInputStream("ImageInserita", exampleInputStream);

            rxUploadOperation
                    .observeResult()
                    .subscribe(
                            result ->
                            {
                                Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey());
                                //TODO: prendere l'url delle immagini caricate e caricale sulla VM (EC2)
                            },
                            error -> Log.e("MyAmplifyApp", "Upload failed", error)
                    );
        }
        catch (FileNotFoundException error)
        {
            Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
        }
    }

    public void updateScrollViewImage(RecyclerView mRecyclerView)
    {
        LinearLayoutManager linearLayout = new LinearLayoutManager(inserimentoItinerarioFragment.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayout);
        mRecyclerView.setAdapter(imageAdapter);


    }
}
