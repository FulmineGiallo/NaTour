package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.rx.RxAmplify;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.dao.ImmagineDAO;
import com.example.natour.model.dao.ItinerarioDAO;
import com.example.natour.view.Signout;
import com.example.natour.view.Profile.ProfileFragment;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;
import com.example.natour.view.adapter.ProfileAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.rxjava3.subjects.PublishSubject;


public class ControllerProfile
{
    private FragmentManager fragmentManager;
    private Context contexController;
    private ProfileFragment profileFragment;
    private String token;
    private List<Itinerario> listIt;
    private List<Immagine> immagineList = new LinkedList<>();
    private ProfileAdapter adapter;
    private Intent intentLogin;
    private boolean isViewLoaded = false;

    public ControllerProfile(ProfileFragment profileFragment, FragmentManager fragmentManager, Context contexController, String token, List<Itinerario> listIt)
    {
        this.profileFragment = profileFragment;
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
        intentLogin = new Intent(contexController, Signout.class);
        this.token = token;
        this.listIt = listIt;
    }

    public void signOut()
    {
        contexController.startActivity(intentLogin);
    }

    public void getItinerariProfile(){
        ItinerarioDAO itinerarioDAO = new ItinerarioDAO();
        Log.i("TOKEN",token);
        PublishSubject<JSONArray> response = itinerarioDAO.getItinerariFromUtente(contexController, token);
        response.subscribe(
                result ->
                {
                    isViewLoaded = true;
                    listIt.clear();
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
                        itinerario.setFk_utente(token);
                        itinerario.setNomeFile(String.valueOf(result.getJSONObject(i).get("nomefile")));
                        itinerario.setAccessibilitaDisabili(Boolean.parseBoolean(String.valueOf(result.getJSONObject(i).get("disabile"))));
                        listIt.add(itinerario);
                        Immagine newImg = new Immagine("");
                        itinerario.getImmagini().add(newImg);
                        new ImmagineDAO().getImageOfItinerario(itinerario, profileFragment.requireContext())
                                .subscribe(
                                        immagini -> {
                                            JSONObject jsonObject = immagini.getJSONObject(0);
                                            RxAmplify.Storage.getUrl(jsonObject.getString("id_key")).subscribe(
                                                    urlResult -> {

                                                        newImg.setURL(urlResult.getUrl().toString());
                                                        profileFragment.requireActivity().runOnUiThread(()->adapter.notifyItemChanged(listIt.indexOf(itinerario)));
                                                    },
                                                    error -> Log.e("STORAGE ERROR", error.getLocalizedMessage())
                                            );
                                        },
                                        errore -> {

                                        }
                                );
                        Log.i("TEST", "FINE");
                    }
                    //profileFragment.requireActivity().runOnUiThread(()->adapter.notifyItemRangeChanged(0, result.length()));
                    adapter.notifyItemRangeChanged(0, result.length());
                    if(listIt.isEmpty())
                        profileFragment.requireActivity().runOnUiThread(()->profileFragment.showItems());
                },
                error ->
                {
                    Log.e("error",error.getLocalizedMessage());
                }
        );
    }

    public void setAdapter(RecyclerView mRecyclerView)
    {
        adapter = new ProfileAdapter(profileFragment, listIt, fragmentManager, this, immagineList);
        GridLayoutManager layout = new GridLayoutManager(contexController, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setAdapter(adapter);
    }

    public void setToken(String tokenUtente) {
        token = tokenUtente;
    }

    public void visualizzaItinerario(Itinerario itinerario)
    {
        Intent intent = new Intent(profileFragment.requireContext(), VisualizzaItinerarioActivity.class);
        Immagine immagineSaved = new Immagine(itinerario.getImmagini().get(0).getURL());
        itinerario.getImmagini().clear();
        intent.putExtra("itinerarioselezionato", itinerario);
        intent.putExtra("token", token);
        profileFragment.requireActivity().startActivity(intent);
        itinerario.getImmagini().add(immagineSaved);
    }
}
