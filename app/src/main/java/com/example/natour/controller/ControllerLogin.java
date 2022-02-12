package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.rx.RxAmplify;
import com.example.natour.model.Utente;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.ErrorDialog;
import com.example.natour.view.LoginActivity.Login;
import com.example.natour.view.LoginActivity.UtenteSingleton;
import com.example.natour.view.Tab.TabActivity;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerLogin
{

    private FragmentManager fragmentManager;
    private Context contexController;
    Intent intentHomePage;
    private Utente utenteLoggato;
    private UtenteDAO utenteDati = new UtenteDAO();
    private String TAG = "Controller Login";

    public ControllerLogin(FragmentManager fragmentManager, Context contexController)
    {
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
        intentHomePage = new Intent(contexController, TabActivity.class);

    }

    public void checkLoginCognito(String email, String password)
    {
       utenteLoggato = new Utente();
       Single<AuthSignInResult> login;


       login = utenteDati.loginWithCognito(email,password);
       login.subscribe(
                       result ->
                       {
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
                                                           utenteLoggato.setDataDiNascita(data.getString("data_di_nascita"));
                                                           intentHomePage.putExtra("utente",utenteLoggato);
                                                           contexController.startActivity(intentHomePage);
                                                       },
                                                       dataError ->
                                                               Log.e("ERROR Birt", dataError.toString())

                                               );
                                           }
                                   );
                       },
                       error ->
                       {
                           new ErrorDialog("Utente non esistente, registrati!").show(fragmentManager, null);
                           Log.e("AuthQuickstart", error.toString());
                       }
               );



    }
    public void checkLoginFacebook()
    {
        utenteDati.loginWithFacebook(contexController);


    }
    public void checkLoginGoogle()
    {
        utenteLoggato = new Utente();
        RxAmplify.Auth.signInWithSocialWebUI(AuthProvider.google(), (Login) contexController)
                .subscribe(
                        result ->
                        {
                            Log.i("AuthQuickstart", result.toString());
                            RxAmplify.Auth.fetchUserAttributes()
                                    .doOnSubscribe(esult -> Log.i("AuthDemo", "Attributes:" + esult.toString()))
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
                                                intentHomePage.putExtra("utente",utenteLoggato);
                                                contexController.startActivity(intentHomePage);
                                            }
                                    );
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );



    }
}
