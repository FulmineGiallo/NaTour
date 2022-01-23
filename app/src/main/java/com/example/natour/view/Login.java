package com.example.natour.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.example.natour.R;
import com.example.natour.controller.ControllerLogin;

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

        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
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

        facebookLogin = findViewById(R.id.img_facebook);
        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // For Facebook
                Amplify.Auth.signInWithSocialWebUI(AuthProvider.facebook(), Login.this,
                        result -> Log.i("AuthQuickstart", result.toString()),
                        error -> Log.e("AuthQuickstart", error.toString())
                );

            }
        });

    }

}