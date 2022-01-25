package com.example.natour.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.core.Amplify;
import com.example.natour.model.Utente;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.TabActivity;

public class ControllerLogin
{

    private FragmentManager fragmentManager;
    private Context contexController;
    Intent intentHomePage;

    public ControllerLogin(FragmentManager fragmentManager, Context contexController)
    {
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
        intentHomePage = new Intent(contexController, TabActivity.class);
    }

    public void checkLogin(String email, String password)
    {
        /* UtenteDAO utente = new UtenteDAO();
        utente.utenteExist(email, password, contexController);*/


        Amplify.Auth.signIn(
                email,
                password,
                result ->{
                    Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");

                    contexController.startActivity(intentHomePage);
                    ((Activity) contexController).finish();
                    System.out.println(Amplify.Auth.getCurrentUser().getUserId());
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );


    }
    public void loginWithFacebook()
    {
        Amplify.Auth.signInWithSocialWebUI(AuthProvider.facebook(), (Activity) contexController,
                result -> {
                    Log.i("AuthQuickstart", result.toString());
                    contexController.startActivity(intentHomePage);
                    /*((Activity) contexController).finish();*/
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
    public void loginWithGoogle()
    {
        Amplify.Auth.signInWithSocialWebUI(AuthProvider.google(), (Activity) contexController,
                result -> {
                    Log.i("AuthQuickstart", result.toString());
                    contexController.startActivity(intentHomePage);
                    /*((Activity) contexController).finish();*/
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
}
