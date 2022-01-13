package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.example.natour.model.connection.CognitoSettings;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.TabActivity;
import com.example.natour.view.ErrorDialog;

import java.sql.SQLException;

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
        final int[] accesso = new int[0]; //1 presente , 0 assente
        boolean check = false;

        UtenteDAO utente = new UtenteDAO();
        utente.utenteExist(email, password, contexController);
        final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice)
            {
                Log.i("LOGIN", "Login avvenuto con successo, puoi prendere il token qui");
                accesso[0] = 1;
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
                accesso[0] = 0;
                Log.i("ERROR_LOGIN", "Login Failed" + exception.getLocalizedMessage());
            }
        };

        /*Verifica con Cognito */
        CognitoSettings cognitoSettings = new CognitoSettings(contexController);
        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(email);
        thisUser.getSessionInBackground(authenticationHandler);

        /* Se il login va a buon fine, bisogna caricare TabActivity */
        if(accesso[0] == 0)
        {

            intentHomePage = new Intent(contexController, TabActivity.class);
            contexController.startActivity(intentHomePage);

            check = true;

            return check;
        }
        /* Se il login non va a buon fine, bisogna caricare la pagina di errore */
        if(accesso[0] == 1)
        {
            ErrorDialog errorLogin = new ErrorDialog("Login errato");
            errorLogin.show(fragmentManager, "ERROR");
        }
        return check;

    }
}
