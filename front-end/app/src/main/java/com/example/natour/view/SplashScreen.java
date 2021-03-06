package com.example.natour.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.example.natour.R;
import com.example.natour.model.Utente;
import com.example.natour.model.dao.StatisticheDAO;
import com.example.natour.view.LoginActivity.Login;
import com.example.natour.view.Tab.TabActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.reactivex.rxjava3.core.Observable;

public class SplashScreen extends AppCompatActivity
{

    private static final String TAG = "SplashScreen";
    Utente utenteLoggato;
    LinearProgressIndicator progress_bar;
    ImageView backGround;
    private FirebaseAnalytics mFirebaseAnalytics;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //gradient animation
        backGround = findViewById(R.id.splash_screen);
        Animation fadein = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -1f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f);
        fadein.setDuration(1000);
        backGround.startAnimation(fadein);

        progress_bar = findViewById(R.id.loading_bar);
        progress_bar.setProgressCompat(0, true);
        try
        {
            RxAmplify.addPlugin(new AWSCognitoAuthPlugin());
            RxAmplify.addPlugin(new AWSS3StoragePlugin());
            RxAmplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        }
        catch (AmplifyException error)
        {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
        RxAmplify.Auth.fetchAuthSession().subscribe
                (
                result -> {
                    progress_bar.setProgressCompat(25, true);
                    if(result.isSignedIn())
                    {

                        Log.i(TAG, "sei gi?? loggato " + result.toString());
                        RxAmplify.Auth.fetchUserAttributes()
                                .doOnSubscribe(consu ->
                                {
                                    progress_bar.setProgressCompat(50, true);
                                    Log.i(TAG, consu.toString());
                                    utenteLoggato = new Utente();
                                })
                                .flatMapObservable(Observable::fromIterable)
                                .subscribe(
                                        eachAttribute -> {
                                            progress_bar.setProgressCompat(progress_bar.getProgress()+5, true);
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
                                        error ->
                                        {
                                            Log.e(TAG, error.toString());
                                            progress_bar.setProgressCompat(70, true);
                                            new Handler(Looper.getMainLooper()).postDelayed(() ->{
                                                        progress_bar.setProgressCompat(100, true);
                                                        new Handler(Looper.getMainLooper()).postDelayed(() ->
                                                        {
                                                            startActivity(new Intent(this,
                                                                    Login.class));
                                                            finish();
                                                        }, 1200);
                                                    }
                                                    , 2000);
                                        },
                                        () -> {

                                            progress_bar.setProgressCompat(90, true);
                                            new StatisticheDAO().updateLogin(this);
                                            new Handler(Looper.getMainLooper()).postDelayed(() ->{
                                                Intent intent = new Intent(this, TabActivity.class);
                                                intent.putExtra("utente",utenteLoggato);
                                                progress_bar.setProgressCompat(100, true);
                                                startActivity(intent);
                                                finish();
                                            }
                                            , 1000);
                                        }
                                );
                    }
                    else
                    {
                        progress_bar.setProgressCompat(70, true);
                        new Handler(Looper.getMainLooper()).postDelayed(() ->{
                                    progress_bar.setProgressCompat(100, true);
                                    new Handler(Looper.getMainLooper()).postDelayed(() ->
                                    {
                                        startActivity(new Intent(this,
                                                Login.class));
                                        finish();
                                    }, 1200);

                                }
                                , 2000);
                    }
                },
                error -> Log.e(TAG, error.toString())
        );
    }
}