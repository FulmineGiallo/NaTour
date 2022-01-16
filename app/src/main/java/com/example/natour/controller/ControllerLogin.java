package com.example.natour.controller;

import android.accounts.Account;
import android.accounts.AccountManager;
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

    public boolean checkLogin(String email, String password)
    {
        boolean check = false;

       /* UtenteDAO utente = new UtenteDAO();
        utente.utenteExist(email, password, contexController);*/

        final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice)
            {
                Log.i("LOGIN", "Login avvenuto con successo, puoi prendere il token qui");
                intentHomePage = new Intent(contexController, TabActivity.class);
                contexController.startActivity(intentHomePage);
                /* userSession contiene il token */

            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId)
            {
                Log.i("LOGIN", "getAuthenticationDetails()...");
                /* password per continuare */
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, password, null);

                authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                authenticationContinuation.continueTask();


            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {

            }

            @Override
            public void onFailure(Exception exception)
            {
                ErrorDialog errorLogin = new ErrorDialog("Email o Password non corretti, riprova!");
                errorLogin.show(fragmentManager, "ERROR");

                Log.i("ERROR_LOGIN", "Login Failed" + exception.getLocalizedMessage());
            }
        };

        /*Verifica con Cognito */
        CognitoSettings cognitoSettings = new CognitoSettings(contexController);
        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(email);
        thisUser.getSessionInBackground(authenticationHandler);


        return check;

    }

}
