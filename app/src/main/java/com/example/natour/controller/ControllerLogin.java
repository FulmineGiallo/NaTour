package com.example.natour.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.Consumer;
import com.amplifyframework.rx.RxAmplify;
import com.example.natour.model.Utente;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.TabActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.disposables.Disposable;

public class ControllerLogin
{

    private FragmentManager fragmentManager;
    private Context contexController;
    Intent intentHomePage;
    private Utente utenteLoggato = new Utente();
    private UtenteDAO utenteDati = new UtenteDAO();

    public ControllerLogin(FragmentManager fragmentManager, Context contexController)
    {
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
        intentHomePage = new Intent(contexController, TabActivity.class);
    }

    public void checkLogin(String email, String password)
    {

       utenteDati.loginWithCognito(email,password);
       utenteDati.getInformationOfAmplifySession(contexController, utenteLoggato);



        System.out.println(utenteLoggato.toString());
    }
    public void loginWithFacebook()
    {
        Amplify.Auth.signInWithSocialWebUI(AuthProvider.facebook(), (Activity) contexController,
                result -> {
                    Log.i("AuthQuickstart", result.toString());
                    utenteDati = new UtenteDAO();
                    /*utenteLoggato = utenteDati.getInformationOfAmplifySession(contexController);*/
                    contexController.startActivity(intentHomePage);

                },
                error -> Log.e("AuthQuickstart", error.toString())
        );


    }
    public void loginWithGoogle()
    {

    }
}
