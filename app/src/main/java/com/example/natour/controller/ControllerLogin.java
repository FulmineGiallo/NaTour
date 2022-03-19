package com.example.natour.controller;

import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.rx.RxAmplify;
import com.example.natour.model.Utente;
import com.example.natour.model.dao.StatisticheDAO;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.PannelloAdmin.AdminPanel;
import com.example.natour.view.dialog.ErrorDialog;
import com.example.natour.view.LoginActivity.Login;
import com.example.natour.view.Tab.TabActivity;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerLogin
{

    private final FragmentManager fragmentManager;
    private final Login contexController;
    private final Intent intentHomePage;
    private Utente utenteLoggato;
    private final UtenteDAO utenteDati = new UtenteDAO();
    private Disposable loginDisposable;
    private Disposable userInfoDisposable;
    private Disposable setDataDiNascitaDisposable;

    public ControllerLogin(FragmentManager fragmentManager, Login contexController)
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
       loginDisposable = login.subscribe(
                       result ->
                       {
                           Single<List<AuthUserAttribute>> infoUtente = utenteDati.getInformationOfAmplifySession();
                           userInfoDisposable = infoUtente.doOnSubscribe(dosub -> Log.i("AuthDemo", "Attributes:"))
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
                                               setDataDiNascitaDisposable = subject.subscribe(
                                                       data ->
                                                       {
                                                           new StatisticheDAO().updateLogin(contexController);
                                                           utenteLoggato.setDataDiNascita(data.getString("data_di_nascita"));
                                                           intentHomePage.putExtra("utente",utenteLoggato);
                                                           contexController.startActivity(intentHomePage);
                                                           terminaLogin();
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
        utenteLoggato = new Utente();
        loginDisposable = RxAmplify.Auth.signInWithSocialWebUI(AuthProvider.facebook(), contexController)
                .subscribe(
                        result ->
                        {
                            Log.i("AuthQuickstart", result.toString());
                            userInfoDisposable = RxAmplify.Auth.fetchUserAttributes()
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
                                                new StatisticheDAO().updateLogin(contexController);
                                                intentHomePage.putExtra("utente", utenteLoggato);
                                                utenteDati.insertUtenteSocial(utenteLoggato.getToken(), utenteLoggato.getNome(), utenteLoggato.getCognome(), contexController);
                                                contexController.startActivity(intentHomePage);
                                                terminaLogin();
                                            }
                                    );
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );
    }

    public void checkLoginGoogle()
    {
        utenteLoggato = new Utente();
        loginDisposable = RxAmplify.Auth.signInWithSocialWebUI(AuthProvider.google(), contexController)
                .subscribe(
                        result ->
                        {
                            Log.i("AuthQuickstart", result.toString());
                            userInfoDisposable= RxAmplify.Auth.fetchUserAttributes()
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
                                                new StatisticheDAO().updateLogin(contexController);
                                                intentHomePage.putExtra("utente",utenteLoggato);
                                                utenteDati.insertUtenteSocial(utenteLoggato.getToken(), utenteLoggato.getNome(), utenteLoggato.getCognome(), contexController);
                                                contexController.startActivity(intentHomePage);
                                                terminaLogin();
                                            }
                                    );
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );



    }

    public void terminaLogin()
    {
        /*contexController.finish();
        loginDisposable.dispose();
        userInfoDisposable.dispose();
        setDataDiNascitaDisposable.dispose();*/
    }
}
