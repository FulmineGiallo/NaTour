package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.example.natour.model.connection.CognitoSettings;
import com.example.natour.view.ConfermaRegistrazioneDialog;
import com.example.natour.view.ErrorDialog;

public class ControllerRegister
{
    private Intent registerToLogin;
    private FragmentManager fragmentManager;
    private Context activity;

    public ControllerRegister(FragmentManager fragmentManager, Context context){
        this.fragmentManager = fragmentManager;
        this.activity = context;
    }


    public void registerUser(String nome,
                             String cognome,
                             String email,
                             String password,
                             String dataDiNascita)
    {
        CognitoUserAttributes userAttributes = new CognitoUserAttributes();

        SignUpHandler signupCallBack = new SignUpHandler()
        {
            @Override
            public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails)
            {
                Log.i("NATOUR", "Registrazione avvenuta" + signUpConfirmationState);

                if(!signUpConfirmationState)
                {
                    Log.i("NATOUR", "Registrazione non confermata, codice di verifica inviato a " + cognitoUserCodeDeliveryDetails.getDestination());
                    inserimentoCodice();
                }
                else
                {
                    Log.i("NATOUR", "Registrazione avvenuta, confermata");
                }
            }
            @Override
            public void onFailure(Exception exception)
            {
                Log.i("NATOUR", "Registrazione fallita" + exception.getLocalizedMessage());
                ErrorDialog errorDialog = new ErrorDialog("Registrazione fallita!");
                errorDialog.show(fragmentManager,"NATOUR");
            }
        };

        userAttributes.addAttribute("name", nome);
        userAttributes.addAttribute("family_name", cognome);
        userAttributes.addAttribute("email", email);
        userAttributes.addAttribute("birthdate",dataDiNascita);
        CognitoSettings cognitoSettings = new CognitoSettings(activity);

        cognitoSettings.getUserPool().signUpInBackground(email, password,
                userAttributes, null, signupCallBack);
    }

    public void inserimentoCodice() {
        ConfermaRegistrazioneDialog bottomSheet = new ConfermaRegistrazioneDialog();
        bottomSheet.show(fragmentManager, "confermaRegistrazione");
    }
}
