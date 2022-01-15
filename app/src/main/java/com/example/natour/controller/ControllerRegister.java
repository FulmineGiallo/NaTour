package com.example.natour.controller;

import static org.apache.commons.lang3.StringUtils.join;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import com.example.natour.view.Register;

public class ControllerRegister
{
    private Intent registerToLogin;
    private FragmentManager fragmentManager;
    private Context activity;
    private TextView codiceErrato;
    private Register register;

    public ControllerRegister(FragmentManager fragmentManager, Context context, Register register)
    {
        this.fragmentManager = fragmentManager;
        this.activity = context;
        this.register = register;

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
        bottomSheet.setCancelable(false);
        bottomSheet.show(fragmentManager, "confermaRegistrazione");

    }

    public void verficaCodiceCognito(String codice, String email, TextView errore, ConfermaRegistrazioneDialog dialog)
    {
        codiceErrato = errore;
        final String[] result = new String[1];
        Thread richiestaCodiceCognito = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final GenericHandler confirmationCallBack = new GenericHandler()
                {
                    @Override
                    public void onSuccess()
                    {
                        result[0] = "Confermato";
                        register.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                dialog.dismiss();
                                register.load();

                            }
                        });

                    }
                    @Override
                    public void onFailure(Exception exception)
                    {
                        result[0] = "Failed: "+exception.getMessage();
                        register.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                codiceErrato.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                };

                CognitoSettings cognitoSettings = new CognitoSettings(activity);
                CognitoUser thisUser = cognitoSettings.getUserPool().getUser(email);

                thisUser.confirmSignUp(codice,false, confirmationCallBack);
            }
        });
        richiestaCodiceCognito.start();
    }

    public void passaAlLogin()
    {

    }


}
