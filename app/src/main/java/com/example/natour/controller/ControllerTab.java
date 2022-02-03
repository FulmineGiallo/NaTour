package com.example.natour.controller;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.amplifyframework.auth.AuthUserAttribute;
import com.example.natour.model.Utente;
import com.example.natour.model.dao.UtenteDAO;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerTab
{
    private FragmentManager fragmentManager;
    private Context contexController;

    public ControllerTab(FragmentManager fragmentManager, Context contexController)
    {
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
    }

    public Utente loadingUtente()
    {
        UtenteDAO utenteDati = new UtenteDAO();
        Utente utenteLoggato = new Utente();

        Single<List<AuthUserAttribute>> infoUtente = utenteDati.getInformationOfAmplifySession();
        infoUtente.doOnSubscribe(dosub -> Log.i("AuthDemo", "Attributes:"))
                .flatMapObservable(Observable::fromIterable)
                .subscribe(
                        eachAttribute ->
                        {
                            Log.i("AuthDemo", eachAttribute.toString());
                            if(eachAttribute.getKey().getKeyString().equals("email"))
                                utenteLoggato.setEmail(eachAttribute.getValue());
                            if(eachAttribute.getKey().getKeyString().equals("sub"))
                                utenteLoggato.setToken(eachAttribute.getValue());
                            if(eachAttribute.getKey().getKeyString().equals("family_name"))
                                utenteLoggato.setCognome(eachAttribute.getValue());
                            if(eachAttribute.getKey().getKeyString().equals("name"))
                                utenteLoggato.setNome(eachAttribute.getValue());
                        },

                        error -> Log.e("AuthDemo", "Failed to fetch attributes.", error),
                        () ->
                        {
                            PublishSubject<JSONObject> subject;
                            /* Finisce qui */
                            subject = utenteDati.getBirthdate(utenteLoggato.getToken(), contexController);
                            subject.subscribe(
                                    data ->
                                    {

                                    },
                                    dataError ->
                                            Log.e("ERROR Birt", dataError.toString())

                            );
                        }
                );
        return utenteLoggato;
    }
}
