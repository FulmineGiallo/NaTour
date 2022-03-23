package com.example.natour.controller;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.dao.StatisticheDAO;
import com.example.natour.view.Tab.CercaFragment;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;
import com.example.natour.view.adapter.MasonryAdapter;
import com.example.natour.view.adapter.SpacesItemDecoration;
import com.example.natour.view.dialog.ErrorDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ControllerCerca implements ControllerInterface
{
    private FragmentManager fragmentManager;
    private CercaFragment fragment;
    private MasonryAdapter adapter;
    private ArrayList<Itinerario> itinerari;
    private List<Immagine> immagineList = new LinkedList<>();
    private String token;
    private ArrayList<Itinerario> copiaLista = new ArrayList<>();
    private FirebaseAnalytics mFirebaseAnalytics;

    public ControllerCerca(FragmentManager fragmentManager, CercaFragment fragment, ArrayList<Itinerario> itinerari, FirebaseAnalytics mFirebaseAnalytics)
    {
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        this.itinerari = itinerari;
        this.mFirebaseAnalytics = mFirebaseAnalytics;
    }


    @Override
    public void visualizzaItinerario(Itinerario itinerario)
    {
        Intent intent = new Intent(fragment.requireContext(), VisualizzaItinerarioActivity.class);
        Immagine immagineSaved = new Immagine(itinerario.getImmagini().get(0).getURL());
        itinerario.getImmagini().clear();
        intent.putExtra("itinerarioselezionato", itinerario);
        intent.putExtra("token", token);
        fragment.requireActivity().startActivity(intent);
        itinerario.getImmagini().add(immagineSaved);
    }

    @Override
    public void visualizzaTrigger(String name)
    {
        Bundle bundle = new Bundle();
        bundle.putString("path_name", name);
        mFirebaseAnalytics.logEvent("path_look_up", bundle);
    }


    public void filtraItinerarioWithSearch(String query)
    {
        copiaLista.clear();
        for (Itinerario i : itinerari)
        {
            if(i.getNome().contains(query))
                copiaLista.add(i);
        }
        Log.i("SIZE COPIA", String.valueOf(copiaLista.size()));
        adapter.notifyItemRangeChanged(0, copiaLista.size() - 1);
        new StatisticheDAO().updateRicerche(fragment.requireContext());
    }

    public void setListaItinerari(ArrayList<Itinerario> itinerariFiltrati)
    {
        this.itinerari = itinerariFiltrati;
    }
    public void setAdapter(RecyclerView mRecyclerView)
    {
        adapter = new MasonryAdapter(fragment, copiaLista, fragmentManager, this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
    }


    public void filtraItinerarioWithFilter(String address, int difficolta, String durata, boolean disabili)
    {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "cercafiltro");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "cerca");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "filtro");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        new StatisticheDAO().updateRicerche(fragment.requireContext());
        copiaLista.clear();
        GeoPoint p1;
        List<Address> addressList = new ArrayList<>();
        boolean exists = false;
        for (Itinerario i : itinerari)
        {
            if(i.isAccessibilitaDisabili() == disabili && i.getDifficoltà() == difficolta)
            {
                if(!address.isEmpty())
                {
                    Geocoder coder = new Geocoder(fragment.getActivity());
                    try
                    {
                        addressList = coder.getFromLocationName(address, 5);
                        Address location = addressList.get(0);
                        location.getLatitude();
                        location.getLongitude();
                        p1 = new GeoPoint(location.getLatitude(), location.getLongitude());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    if(addressList.contains(address))
                        if(!copiaLista.contains(i))
                        {
                            if(durata.isEmpty())
                            {
                                copiaLista.add(i);
                            }
                            else if(i.getDurata().contains(durata))
                            {
                                copiaLista.add(i);
                            }
                        }
                }
                else
                {
                    if(!copiaLista.contains(i))
                    {
                        if(durata.isEmpty())
                        {
                            copiaLista.add(i);
                        }
                        else if(i.getDurata().contains(durata))
                        {
                            copiaLista.add(i);
                        }
                    }
                }
                exists = true;
            }

        }
        if(!exists)
            new ErrorDialog("Non ci sono itinerario che rispecchiano questo filtro!").show(fragmentManager, null);

        Log.i("SIZE COPIA", String.valueOf(copiaLista.size()));
        adapter.notifyItemRangeChanged(0, copiaLista.size() - 1);
        
    }

    public ArrayList<Itinerario> getCopiaLista()
    {
        return copiaLista;
    }
}
