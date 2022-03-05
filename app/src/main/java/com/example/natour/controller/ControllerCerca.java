package com.example.natour.controller;

import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.view.Tab.CercaFragment;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;
import com.example.natour.view.adapter.MasonryAdapter;
import com.example.natour.view.adapter.SpacesItemDecoration;

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

    public ControllerCerca(FragmentManager fragmentManager, CercaFragment fragment, ArrayList<Itinerario> itinerari)
    {
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        this.itinerari = itinerari;

    }


    @Override
    public void visualizzaItinerario(Itinerario itinerario)
    {
        Intent intent = new Intent(fragment.requireContext(), VisualizzaItinerarioActivity.class);
        intent.putExtra("itinerarioselezionato", itinerario);
        intent.putExtra("token", token);
        fragment.requireActivity().startActivity(intent);
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

    }

    public void setListaItinerari(ArrayList<Itinerario> itinerariFiltrati)
    {
        this.itinerari = itinerariFiltrati;
    }
    public void setAdapter(RecyclerView mRecyclerView)
    {
        adapter = new MasonryAdapter(fragment, copiaLista, fragmentManager, this, immagineList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
    }


    public void filtraItinerarioWithFilter(String address, int difficolta, String durata, boolean disabili)
    {
        copiaLista.clear();
        for (Itinerario i : itinerari)
        {
            if(i.getDifficoltà() == difficolta || i.getDurata().contains(durata) || i.isAccessibilitaDisabili())
            {

            }
        }
        Log.i("SIZE COPIA", String.valueOf(copiaLista.size()));
        adapter.notifyItemRangeChanged(0, copiaLista.size() - 1);

    }

}
