package com.example.natour.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.Consumer;
import com.amplifyframework.rx.RxAmplify;
import com.example.natour.model.Utente;
import com.example.natour.model.connection.RequestAPI;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.ErrorDialog;
import com.example.natour.view.TabActivity;

import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerLogin
{

    private FragmentManager fragmentManager;
    private Context contexController;
    Intent intentHomePage;
    private Utente utenteLoggato;
    private UtenteDAO utenteDati = new UtenteDAO();

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
                           contexController.startActivity(intentHomePage);
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
        utenteDati.loginWithGoogle(contexController);
        utenteDati.getInformationOfAmplifySessionWithGoogle(contexController, utenteLoggato);
        System.out.println(utenteLoggato.toString());
    }
}
