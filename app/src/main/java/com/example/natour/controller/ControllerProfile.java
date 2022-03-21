package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.rx.RxAmplify;
import com.example.natour.model.Compilation;
import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.Utente;
import com.example.natour.model.dao.CompilationDAO;
import com.example.natour.model.dao.ImmagineDAO;
import com.example.natour.model.dao.ItinerarioDAO;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.Profile.ProfileCompilation;
import com.example.natour.view.Signout;
import com.example.natour.view.Profile.ProfileFragment;
import com.example.natour.view.VisualizzaItinerario.VisualizzaItinerarioActivity;
import com.example.natour.view.adapter.CompilationAdapter;
import com.example.natour.view.adapter.ItinerariCompilationAdapter;
import com.example.natour.view.adapter.ProfileAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.rxjava3.subjects.PublishSubject;


public class ControllerProfile implements ControllerInterface
{
    private FragmentManager fragmentManager;
    private Context contexController;
    private ProfileFragment profileFragment;
    private ProfileCompilation profileCompilation;
    private String token;
    private List<Itinerario> listIt;
    private List<Compilation> compilationList = new LinkedList<>();
    private List<Immagine> immagineList = new LinkedList<>();

    private ProfileAdapter adapter;
    private CompilationAdapter compilationAdapter;

    private Intent intentLogin;
    private boolean isViewLoaded = false;

    private FirebaseAnalytics mFirebaseAnalytics;

    public ControllerProfile(ProfileFragment profileFragment, FragmentManager fragmentManager, Context contexController, String token, List<Itinerario> listIt, FirebaseAnalytics mFirebaseAnalytics)
    {
        this.profileFragment = profileFragment;
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
        intentLogin = new Intent(contexController, Signout.class);
        this.token = token;
        this.listIt = listIt;
        this.mFirebaseAnalytics = mFirebaseAnalytics;
    }

    public void signOut()
    {
        contexController.startActivity(intentLogin);
    }

    public void setFragmentController(ProfileCompilation profileCompilation)
    {
        this.profileCompilation = profileCompilation;
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
        intent.putExtra("token", itinerario.getFk_utente());
        profileFragment.requireActivity().startActivity(intent);
        itinerario.getImmagini().add(immagineSaved);
    }

    @Override
    public void visualizzaTrigger(String name)
    {
        Bundle bundle = new Bundle();
        bundle.putString("path_name", name);
        mFirebaseAnalytics.logEvent("path_look_up", bundle);
    }


    public void setCompilationAdapter(RecyclerView recyclerView)
    {
        compilationAdapter = new CompilationAdapter(this, compilationList);
        LinearLayoutManager layout = new LinearLayoutManager(contexController,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layout);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layout.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(compilationAdapter);
    }

    public void setItinerariCompilationAdapter(RecyclerView recyclerView, Compilation compilation)
    {
        ItinerariCompilationAdapter itinerariCompilationAdapter = new ItinerariCompilationAdapter(compilation.getItinerariCompilation(),profileFragment, recyclerView, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(contexController,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itinerariCompilationAdapter);
        setItinerariCompilations(compilation, itinerariCompilationAdapter);
    }

    private void setItinerariCompilations(Compilation compilation, ItinerariCompilationAdapter itinerariCompilationAdapter)
    {
        new CompilationDAO().getItinerariFromCompilation(contexController, String.valueOf(compilation.getIdCompilation())).subscribe(
                result ->
                {
                    for(int i = 0; i < result.length(); i++)
                    {
                        Itinerario itinerario = new Itinerario();
                        itinerario.setIdItinerario(String.valueOf(result.getJSONObject(i).get("id_itinerario")));
                        itinerario.setNome(String.valueOf(result.getJSONObject(i).get("nome")));
                        itinerario.setDescrizione(String.valueOf(result.getJSONObject(i).get("descrizione")));
                        itinerario.setDifficoltà(Integer.parseInt(String.valueOf(result.getJSONObject(i).get("difficolta"))));
                        itinerario.setDurata(String.valueOf(result.getJSONObject(i).get("durata")));
                        itinerario.setFk_utente(String.valueOf(result.getJSONObject(i).get("fk_utente")));
                        itinerario.setNomeFile(String.valueOf(result.getJSONObject(i).get("nomefile")));
                        itinerario.setAccessibilitaDisabili(Boolean.parseBoolean(String.valueOf(result.getJSONObject(i).get("disabile"))));
                        compilation.getItinerariCompilation().add(itinerario);
                        Immagine newImg = new Immagine("");
                        itinerario.getImmagini().add(newImg);
                        new ImmagineDAO().getImageOfItinerario(itinerario, profileFragment.requireContext())
                                .subscribe(
                                        immagini -> {
                                            JSONObject jsonObject2 = immagini.getJSONObject(0);
                                            RxAmplify.Storage.getUrl(jsonObject2.getString("id_key")).subscribe(
                                                    urlResult -> {

                                                        newImg.setURL(urlResult.getUrl().toString());
                                                        profileFragment.requireActivity().runOnUiThread(()->
                                                                itinerariCompilationAdapter
                                                                        .notifyItemChanged(compilation.getItinerariCompilation().indexOf(itinerario)));
                                                    },
                                                    error -> Log.e("STORAGE ERROR", error.getLocalizedMessage())
                                            );
                                        },
                                        errore -> {

                                        }
                                );
                        profileFragment.requireActivity().runOnUiThread(() ->
                                itinerariCompilationAdapter.notifyItemInserted(compilation.getItinerariCompilation().indexOf(itinerario)));
                    }

                },
                error -> {}
        );
    }

    public void setCompilations(Utente utente)
    {
        new CompilationDAO().getCompilation(contexController, utente.getToken()).subscribe(
                results->{
                    for(int i = 0; i < results.length(); i++){
                        JSONObject jsonObject = results.getJSONObject(i);
                        Compilation compilation = new Compilation();
                        compilation.setIdCompilation(Integer.parseInt(jsonObject.getString("id_compilation")));
                        compilation.setNome(jsonObject.getString("nome"));
                        compilation.setDescrizione(jsonObject.getString("descrizione"));
                        compilation.setIdUtente(jsonObject.getString("id_utente"));
                        compilationList.add(compilation);
                        profileFragment.requireActivity().runOnUiThread(() ->
                                compilationAdapter.notifyItemInserted(compilationList.indexOf(compilation)));
                    }
                    if(compilationList.isEmpty())
                        profileCompilation.requireActivity().runOnUiThread(()->profileCompilation.showItemsCompilation());
                },
                error -> {}
        );

    }

    public void isAdmin(String tokenUtente, Utente utente)
    {
        UtenteDAO utenteDAO = new UtenteDAO();
        utenteDAO.isAdmin(tokenUtente, contexController).subscribe(
                result ->
                {
                    utente.setAdmin(Boolean.parseBoolean(result.getString("is_admin")));
                    profileFragment.requireActivity().runOnUiThread(()->{
                        if(utente.isAdmin()){
                            profileFragment.showAdminOption();
                        }
                    });
                },
                error ->
                {
                    Log.e("error", error.getLocalizedMessage());
                }
        );
    }
    public void isAdminCompilation(String tokenUtente, Utente utente)
    {
        UtenteDAO utenteDAO = new UtenteDAO();
        utenteDAO.isAdmin(tokenUtente, contexController).subscribe(
                result ->
                {
                    utente.setAdmin(Boolean.parseBoolean(result.getString("is_admin")));
                    profileCompilation.requireActivity().runOnUiThread(()->{
                        if(utente.isAdmin()){
                            profileCompilation.showAdminOption();
                        }
                    });
                },
                error ->
                {
                    Log.e("error", error.getLocalizedMessage());
                }
        );
    }
}
