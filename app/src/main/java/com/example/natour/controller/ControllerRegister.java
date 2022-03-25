package com.example.natour.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.util.AnalyticsUseCase;
import com.example.natour.view.RegisterActivity.ConfermaRegistrazioneDialog;
import com.example.natour.view.RegisterActivity.Register;

import java.util.ArrayList;

public class ControllerRegister
{
    private FragmentManager fragmentManager;
    private Context activity;
    private TextView codiceErrato;
    private Register register;
    private String dataDiNascita;

    public ControllerRegister(FragmentManager fragmentManager, Context context, Register register)
    {
        this.fragmentManager = fragmentManager;
        this.activity = context;
        this.register = register;

    }


    public void registerUser(String nome, String cognome, String email, String password, String dataDiNascita)
    {

        this.dataDiNascita = dataDiNascita;
        ArrayList<AuthUserAttribute> params = new ArrayList<>();
        params.add(new AuthUserAttribute(AuthUserAttributeKey.name(), nome));
        params.add(new AuthUserAttribute(AuthUserAttributeKey.familyName(), cognome));
        /*params.add(new AuthUserAttribute(AuthUserAttributeKey.birthdate(), dataDiNascita));*/

        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttributes(params)
                .build();
        Amplify.Auth.signUp(email, password, options,
                result ->
                {
                    Log.i("AuthQuickStart", "Result: " + result.toString());
                    showCodiceConfermato();
                    Log.i("AUTH USER", result.getUser().getUserId());
                    UtenteDAO utenteDAO = new UtenteDAO();
                    Log.i("DATA DI NASCITA", dataDiNascita);
                    utenteDAO.setTokenUtente(result.getUser().getUserId(), dataDiNascita, nome, cognome, activity);
                },
                error -> Log.e("AuthQuickStart", "Sign up failed", error)
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

                    Log.i("RESULT SIGN IN", result.toString());
                    AnalyticsUseCase.event("register_user", "register", "register_user_application", activity);

                },
                error ->
                {
                    Log.e("AuthQuickstart", error.toString());
                    errore.setVisibility(View.VISIBLE);

                }
        );


    }
}
