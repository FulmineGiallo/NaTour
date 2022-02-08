package com.example.natour.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.rx.RxAmplify;
import com.example.natour.R;
import com.example.natour.model.Utente;
import com.example.natour.view.LoginActivity.Login;
import com.example.natour.view.LoginActivity.UtenteSingleton;
import com.example.natour.view.Tab.TabActivity;

import io.reactivex.rxjava3.core.Observable;

public class SplashScreen extends AppCompatActivity
{

    private static final String TAG = "SplashScreen";
    Utente utenteLoggato;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try
        {
            RxAmplify.addPlugin(new AWSCognitoAuthPlugin());
            RxAmplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        }
        catch (AmplifyException error)
        {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        RxAmplify.Auth.fetchAuthSession().subscribe(
                result -> {
                    if(result.isSignedIn())
                    {

                        Log.i(TAG, "sei già loggato " + result.toString());
                        RxAmplify.Auth.fetchUserAttributes()
                                .doOnSubscribe(consu ->
                                {
                                    Log.i(TAG, consu.toString());
                                    utenteLoggato = new Utente();
                                })
                                .flatMapObservable(Observable::fromIterable)
                                .subscribe(
                                        eachAttribute -> {
                                            Log.i("AuthDemo", eachAttribute.toString());
                                            if(eachAttribute.getKey().getKeyString().equals("email"))
                                                utenteLoggato.setEmail(eachAttribute.getValue());
                                            if(eachAttribute.getKey().getKeyString().equals("sub"))
                                                utenteLoggato.setToken(eachAttribute.getValue());
                                            if(eachAttribute.getKey().getKeyString().equals("family_name"))
                                                utenteLoggato.setCognome(eachAttribute.getValue());
                                            if(eachAttribute.getKey().getKeyString().equals("name"))
                                                utenteLoggato.setNome(eachAttribute.getValue());
                                        },
                                        error -> Log.i(TAG,error.toString()),
                                        () -> {
                                            UtenteSingleton.getInstance(utenteLoggato);
                                            new Handler(Looper.getMainLooper()).postDelayed(() ->
                                                    startActivity(new Intent(this,
                                                            TabActivity.class))
                                                    , 2000);

                                        }
                                );
                    }
                    else
                    {
                        new Handler(Looper.getMainLooper()).postDelayed(() ->
                                startActivity(new Intent(this,
                                        Login.class)), 2000);
                    }
                },
                error -> Log.i(TAG, error.toString())
        );
    }
}