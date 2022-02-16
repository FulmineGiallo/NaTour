package com.example.natour.controller;

import android.location.Address;
import android.location.Geocoder;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.natour.R;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerario;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoItinerarioFragment;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoPercorsoFragment;
import com.example.natour.view.InserimentoItinerarioActivity.MappaFragment;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.natour.model.Itinerario;

import java.io.File;
import java.util.List;

public class ControllerItinerario
{
    private String nomeItinerario;
    private String durataItinerario;
    private boolean disabiliItinerario;
    private int difficoltàItinerario; //Ha 5 possibili valori, da 1 a 5
    private File gpx;
    private String descrizioneItnerario;
    private List<String> listaImmagini; //Questa lista contiene tutti i link alle immagine inserite




    public Itinerario inserisciItinerario(String nome, String durata, boolean disabili, File gpx, String descrizione, List<String> immagini)
    {
        Itinerario itinerarioInserito = new Itinerario();
        /* INSERIMENTO DELL'ID ALL'INTERNO DEL DATABASE E DELLE SUE INFORMAZIONI DI BASE */





        return itinerarioInserito;
    }




    private final FragmentManager fragmentManager;
    private final InserimentoItinerario inserimentoItinerarioActivity;
    private InserimentoPercorsoFragment percorsoFragment;
    private MappaFragment mappaFragment;


    public ControllerItinerario(FragmentManager fragmentManager, InserimentoItinerario inserimentoItinerarioActivity){
        this.fragmentManager = fragmentManager;
        this.inserimentoItinerarioActivity = inserimentoItinerarioActivity;
    }

    public void inizializzaInterfaccia()
    {
        if(inserimentoItinerarioActivity.findViewById(R.id.fragmentMappa) != null)
        {
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.add(R.id.fragmentMappa, new InserimentoItinerarioFragment(this));
            ft.commit();

        }
    }

    public void gotoPercorsoFragment()
    {
        if(percorsoFragment == null)
            percorsoFragment = new InserimentoPercorsoFragment(this);

        fragmentManager
                .beginTransaction()
                .replace(R.id.fragmentMappa, percorsoFragment)
                .addToBackStack(InserimentoItinerarioFragment.class.getSimpleName())
                .commit();
    }


    public void goBack()
    {
        inserimentoItinerarioActivity.onBackPressed();
    }

    public void setMapView(Fragment fragment,int viewId)
    {
        if(fragment.requireView().findViewById(viewId) != null)
        {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            mappaFragment = new MappaFragment(this);

            ft.add(viewId, mappaFragment);
            ft.commit();
        }
    }

    public void resetMapView(InserimentoPercorsoFragment ipf, int map)
    {
        if(percorsoFragment.requireView().findViewById(map) != null)
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

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint(location.getLatitude(), location.getLongitude());

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
