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
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.natour.model.connection.CognitoSettings;
import com.example.natour.view.ConfermaRegistrazioneDialog;
import com.example.natour.view.ErrorDialog;
import com.example.natour.view.Login;
import com.example.natour.view.Register;

import java.util.ArrayList;
import java.util.List;

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


    public void registerUser(String nome, String cognome, String email, String password, String dataDiNascita)
    {


        ArrayList<AuthUserAttribute> params = new ArrayList<>();
        params.add(new AuthUserAttribute(AuthUserAttributeKey.name(), nome));
        params.add(new AuthUserAttribute(AuthUserAttributeKey.familyName(), cognome));
        params.add(new AuthUserAttribute(AuthUserAttributeKey.birthdate(), dataDiNascita));

        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttributes(params)
                .build();
        Amplify.Auth.signUp(email, password, options,
                result ->
                {
                    Log.i("AuthQuickStart", "Result: " + result.toString());
                    showCodiceConfermato();
                },
                error -> Log.e("AuthQuickStart", "Sign up failed", error)
                // TODO: Fare schermata sign up failed
        );




    }

    public void showCodiceConfermato()
    {
        ConfermaRegistrazioneDialog bottomSheet = new ConfermaRegistrazioneDialog();
        bottomSheet.setCancelable(false);
        bottomSheet.show(fragmentManager, "confermaRegistrazione");

    }

    public void verficaCodiceCognito(String codice, String email, TextView errore, ConfermaRegistrazioneDialog dialog)
    {
        Amplify.Auth.confirmSignUp(
                email,
                codice,
                result ->{
                    Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    register.runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            dialog.dismiss();
                            register.load();
                        }
                    });
                },
                error ->
                {
                    Log.e("AuthQuickstart", error.toString());
                    errore.setVisibility(View.VISIBLE);
                }
        );



    }
}
