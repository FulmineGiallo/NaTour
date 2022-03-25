package com.example.natour.controller;

import android.content.Context;
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
import com.example.natour.util.AnalyticsUseCase;
import com.example.natour.view.Tab.CercaFragment;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;
import com.example.natour.view.adapter.MasonryAdapter;
import com.example.natour.view.adapter.SpacesItemDecoration;
import com.example.natour.view.dialog.ErrorDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

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
        AnalyticsUseCase.event("visualizza_itinerario_ricercato", "visualizza", "visualizza_in_cerca", fragment.getContext());

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
        AnalyticsUseCase.event("cerca", "cerca", "cerca_con_search", fragment.getContext());

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
        Boolean exists = Boolean.FALSE;
        AnalyticsUseCase.event("filtro", "filter", "cerca_con_filtro", fragment.getContext());

        new StatisticheDAO().updateRicerche(fragment.requireContext());
        try{
            exists = filterResult(address, difficolta, durata, disabili, fragment.requireContext());
        }catch (NullPointerException e){
            exists = Boolean.FALSE;
        }
        if(!exists)
            new ErrorDialog("Non ci sono itinerario che rispecchiano questo filtro!").show(fragmentManager, null);

        Log.i("SIZE COPIA", String.valueOf(copiaLista.size()));
        adapter.notifyItemRangeChanged(0, copiaLista.size() - 1);

    }

    public Boolean filterResult(String address, int difficolta, String durata, boolean disabili, Context context) throws NullPointerException
    {
        boolean exists = Boolean.FALSE;
        copiaLista.clear();
        List<Address> addressList = new ArrayList<>();
        for (Itinerario i : itinerari)
        {
            if(i.isAccessibilitaDisabili() == disabili && i.getDifficoltà() == difficolta)
            {
                if(!address.isEmpty())
                {
                    Geocoder coder = new Geocoder(context);
                    Address location = null;
                    Address currLocation = null;
                    try
                    {
                        addressList = coder.getFromLocationName(address, 5);
                        location = addressList.get(0);
                        addressList = coder.getFromLocation(i.getWaypoints().get(0).getLatitude()
                                , i.getWaypoints().get(0).getLongitude(), 1);
                        currLocation = addressList.get(0);
                    }
                    catch (IOException | IndexOutOfBoundsException e)
                    {
                        e.printStackTrace();
                    }

                    if(currLocation != null && currLocation.getCountryName().equals(location.getCountryName()))
                        if(!copiaLista.contains(i))
                        {
                            if(durata.isEmpty())
                            {
                                copiaLista.add(i);
                                exists = true;
                            }
                            else if(i.getDurata().contains(durata))
                            {
                                copiaLista.add(i);
                                exists = true;
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
                            exists = true;
                        }
                        else if(i.getDurata().contains(durata))
                        {
                            copiaLista.add(i);
                            exists = true;
                        }
                    }
                }

            }

        }
        return exists;
    }

    public ArrayList<Itinerario> getCopiaLista()
    {
        return copiaLista;
    }
}
