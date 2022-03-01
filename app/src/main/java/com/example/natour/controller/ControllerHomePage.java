package com.example.natour.controller;

import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.model.Itinerario;
import com.example.natour.model.dao.ItinerarioDAO;
import com.example.natour.view.Tab.HomePageFragment;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;
import com.example.natour.view.adapter.MasonryAdapter;
import com.example.natour.view.adapter.SpacesItemDecoration;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerHomePage
{
    private FragmentManager fragmentManager;
    private HomePageFragment fragment;
    private MasonryAdapter adapter;
    private List<Itinerario> itinerari = new ArrayList<>();

    public ControllerHomePage(FragmentManager fragmentManager, HomePageFragment fragment)
    {
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        setItinerariHomePage();

    }

    public void setItinerariHomePage()
    {
        ItinerarioDAO itinerarioDAO = new ItinerarioDAO();
        PublishSubject<JSONArray> risultato = itinerarioDAO.getItinerari(fragment.requireContext());
        risultato.subscribe(
                result ->
                {

                    for(int i = 0; i < result.length(); i++)
                    {

                        Itinerario itinerario = new Itinerario();
                        Log.i("SIZE", String.valueOf(result.length()));
                        Log.i("TEST", String.valueOf(result.getJSONObject(i).get("nome")));


                        itinerario.setIdItinerario(String.valueOf(result.getJSONObject(i).get("id_itinerario")));
                        itinerario.setNome(String.valueOf(result.getJSONObject(i).get("nome")));
                        itinerario.setDescrizione(String.valueOf(result.getJSONObject(i).get("descrizione")));
                        itinerario.setDifficoltà(Integer.parseInt(String.valueOf(result.getJSONObject(i).get("difficolta"))));
                        itinerario.setDurata(String.valueOf(result.getJSONObject(i).get("durata")));
                        itinerario.setFk_utente(String.valueOf(result.getJSONObject(i).get("fk_utente")));

/*
                        itinerari.get(i).setWaypoints(getWaypointsFromFile(result.getJSONObject(i).getString("nomefile")));
*/
                        itinerario.setAccessibilitaDisabili(Boolean.parseBoolean(String.valueOf(result.getJSONObject(i).get("disabile"))));
                        itinerari.add(itinerario);
                        Log.i("TEST", "FINE");
                    }

                    adapter.notifyItemRangeChanged(0, itinerari.size());
                },
                error ->
                {

                }
        );

    }

   /* private List<GeoPoint> getWaypointsFromFile(String nomefile)
    {
        //Downlod file, prendere informazioni e metterla dentro alla lista



    }*/

    public void setAdapter(RecyclerView mRecyclerView)
    {
        adapter = new MasonryAdapter(itinerari, fragmentManager, this);
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);

    }

    public void visualizzaItinerario(Itinerario itinerario)
    {
        Intent intent = new Intent(fragment.requireContext(), VisualizzaItinerarioActivity.class);
        intent.putExtra("itinerarioselezionato", itinerario);
        fragment.requireActivity().startActivity(intent);
    }
}
