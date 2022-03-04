package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.example.natour.model.Itinerario;
import com.example.natour.model.dao.ItinerarioDAO;
import com.example.natour.view.InserimentoItinerarioActivity.InserimentoPercorsoFragment;
import com.example.natour.view.Signout;
import com.example.natour.view.Tab.ProfileFragment;
import com.example.natour.view.adapter.MasonryAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.subjects.PublishSubject;


public class ControllerProfile
{
    private FragmentManager fragmentManager;
    private Context contexController;
    private ProfileFragment profileFragment;
    private String token;
    private List<Itinerario> listIt = new ArrayList<>();
    private MasonryAdapter adapter;
    Intent intentLogin;

    public ControllerProfile(FragmentManager fragmentManager, Context contexController, String token, List<Itinerario> listIt)
    {
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
        PublishSubject<JSONArray> response = itinerarioDAO.getItinerariFromUtente(contexController, token);
        response.subscribe(
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
                        listIt.add(itinerario);
                        Log.i("TEST", "FINE");
                    }

                    adapter.notifyItemRangeChanged(0, listIt.size());
                },
                error ->
                {

                }
        );
    }

}
