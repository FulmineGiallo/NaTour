package com.example.natour.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.HostedUIOptions;
import com.amazonaws.mobile.client.IdentityProvider;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.example.natour.R;
import com.example.natour.controller.ControllerLogin;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity
{


    Intent intentRegister;
    Button btn_register;
    Button btn_login;
    ControllerLogin controllerLogin;
    private String email;
    private String password;
    EditText edtEmail;
    EditText edtPassword;

    FragmentManager fm = getSupportFragmentManager();
    ImageButton googleLogin;
    ImageButton facebookLogin;

    private static final int RC_SIGN_IN = 007;
    Intent intentLoginHappened;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        controllerLogin = new ControllerLogin(fm, this);
        btn_login = findViewById(R.id.btn_login);
        googleLogin = findViewById(R.id.image_google);
        intentRegister = new Intent(this, Register.class);
        btn_register = findViewById(R.id.btn_signin);
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(intentRegister);
            }
        });

        /* ---------------------- ACCESSO UTENTE --------------------------------------------- */
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);

        try
        {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        }
        catch (AmplifyException error)
        {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }


        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );

        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                controllerLogin.checkLogin(email, password);
            }
        });

        /*                          FACEBOOK                        */

        facebookLogin = findViewById(R.id.image_facebook);
        facebookLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                controllerLogin.loginWithFacebook();
            }
        });

        googleLogin = findViewById(R.id.image_google);
        googleLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                controllerLogin.loginWithGoogle();
            }
        });
    }

}