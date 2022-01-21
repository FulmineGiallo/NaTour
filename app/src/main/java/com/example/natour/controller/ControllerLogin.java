package com.example.natour.controller;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.amazonaws.auth.AWSCognitoIdentityProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amplifyframework.core.Amplify;
import com.example.natour.model.connection.CognitoSettings;
import com.example.natour.view.TabActivity;
import com.example.natour.view.ErrorDialog;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerLogin
{
    Intent intentHomePage;
    private FragmentManager fragmentManager;
    private Context contexController;

    public ControllerLogin(FragmentManager fragmentManager, Context contexController)
    {
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
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
                    intentHomePage = new Intent(contexController, TabActivity.class);
                    contexController.startActivity(intentHomePage);
                    ((Activity) contexController).finish();
                    System.out.println(Amplify.Auth.getCurrentUser().getUserId());
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );


    }
    public void loginWithFacebook()
    {

    }
}
