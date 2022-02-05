package com.example.natour.view.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.rx.RxAmplify;
import com.example.natour.R;
import com.example.natour.controller.ControllerLogin;
import com.example.natour.view.RegisterActivity.Register;

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

        /* ---------------------- ACCESSO UTENTE --------------------------------------------- */
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);

        edtEmail.setText("carminefb@live.it");
        edtPassword.setText("Carmine13-");
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


        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                controllerLogin.checkLoginCognito(email, password);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(intentRegister);
            }
        });

        facebookLogin = findViewById(R.id.image_facebook);
        facebookLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                controllerLogin.checkLoginFacebook();
            }
        });

        googleLogin = findViewById(R.id.image_google);
        googleLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                controllerLogin.checkLoginGoogle();
            }
        });
    }

}