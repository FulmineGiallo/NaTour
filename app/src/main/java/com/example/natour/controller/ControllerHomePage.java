package com.example.natour.controller;

import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.rx.RxAmplify;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.dao.ImmagineDAO;
import com.example.natour.model.dao.ItinerarioDAO;
import com.example.natour.view.Tab.HomePageFragment;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;
import com.example.natour.view.adapter.MasonryAdapter;
import com.example.natour.view.adapter.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerHomePage implements ControllerInterface
{
    private FragmentManager fragmentManager;
    private HomePageFragment fragment;
    private MasonryAdapter adapter;
    private ArrayList<Itinerario> itinerari = new ArrayList<>();
    private List<Immagine> immagineList = new ArrayList<>();
    private String token;

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
                        itinerario.setNomeFile(String.valueOf(result.getJSONObject(i).get("nomefile")));
                        itinerario.setAccessibilitaDisabili(Boolean.parseBoolean(String.valueOf(result.getJSONObject(i).get("disabile"))));
                        itinerari.add(itinerario);
                        Immagine newImg = new Immagine("");
                        immagineList.add(newImg);
                        new ImmagineDAO().getImageOfItinerario(itinerario, fragment.requireContext())
                                .subscribe(
                                        immagini -> {
                                                JSONObject jsonObject = immagini.getJSONObject(0);
                                                RxAmplify.Storage.getUrl(jsonObject.getString("id_key")).subscribe(
                                                        urlResult -> {

                                                            newImg.setURL(urlResult.getUrl().toString());
                                                            fragment.requireActivity().runOnUiThread(()->adapter.notifyItemChanged(itinerari.indexOf(itinerario)));
                                                        },
                                                        error -> Log.e("STORAGE ERROR", error.getLocalizedMessage())
                                                );
                                        },
                                        errore -> {

                                        }
                                );

                        Log.i("TEST", "FINE");
                    }

                    adapter.notifyItemRangeChanged(0, itinerari.size());
                    fragment.setModelItinerari(itinerari);
                    fragment.setModelImmagini(immagineList);
                },
                error ->
                {

                }
        );

    }

    public void setAdapter(RecyclerView mRecyclerView, String tokenUtente)
    {
        adapter = new MasonryAdapter(fragment, itinerari, fragmentManager, this, immagineList);
        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
        this.token = tokenUtente;
    }

    public void visualizzaItinerario(Itinerario itinerario)
    {
        Intent intent = new Intent(fragment.requireContext(), VisualizzaItinerarioActivity.class);
        intent.putExtra("itinerarioselezionato", itinerario);
        intent.putExtra("token", token);
        fragment.requireActivity().startActivity(intent);
    }

    public void addItinerario(Itinerario itinerario)
    {
        itinerari.add(itinerario);
        adapter.notifyItemInserted(itinerari.indexOf(itinerario));
    }


}