package com.example.natour.controller;

import static org.apache.commons.lang3.StringUtils.join;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.example.natour.model.connection.CognitoSettings;
import com.example.natour.view.ConfermaRegistrazioneDialog;
import com.example.natour.view.ErrorDialog;
import com.example.natour.view.Login;

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
                    showCodiceConfermato();
                }
                else
                {
                    Log.i("NATOUR", "Registrazione avvenuta, confermata");
                }
            }
            @Override
            public void onFailure(Exception exception)
            {
                Log.i("NATOUR", "Registrazione fallita -----> " + exception.getLocalizedMessage() + " <---- ");
                StringBuffer stringBuffer = new StringBuffer(exception.getLocalizedMessage());
                stringBuffer.delete(stringBuffer.indexOf(".") + 1,stringBuffer.length());
                ErrorDialog errorDialog = new ErrorDialog(stringBuffer.toString());
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

    public void showCodiceConfermato()
    {
        ConfermaRegistrazioneDialog bottomSheet = new ConfermaRegistrazioneDialog();
        bottomSheet.show(fragmentManager, "confermaRegistrazione");

        /*Logica conferma codice di Cognito */
    }

    public void verficaCodiceCognito(String codice, String email)
    {
        new ConfirmTask().execute(codice,email);

    }

    public void passaAlLogin() {

    }

    private class ConfirmTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings){
            final String[] result = new String[1];

            //Callback Handler for confirmSignUp API
            final GenericHandler confirmationCallBack = new GenericHandler() {
                @Override
                public void onSuccess() {
                    //User was successfully confirmed
                    result[0] = "Confermato";
                }

                @Override
                public void onFailure(Exception exception) {
                    //User confirmation failed. Check exception for the cause.
                    result[0] = "Failed: "+exception.getMessage();
                }
            };
            CognitoSettings cognitoSettings = new CognitoSettings(activity);
            CognitoUser thisUser = cognitoSettings.getUserPool().getUser(strings[1]);
            /*
            * this will cause confirmation to fail if
            * the user attribute (alias) has been verified for
            * another user in the same pool*/
            thisUser.confirmSignUp(strings[0],false, confirmationCallBack);
            return result[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("NATOUR","Confirmation result: " + s);
        }
    }

}
