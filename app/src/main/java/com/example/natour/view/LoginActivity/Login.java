package com.example.natour.view.LoginActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.amplifyframework.rx.RxAmplify;
import com.example.natour.R;
import com.example.natour.controller.ControllerLogin;
import com.example.natour.view.RegisterActivity.Register;

import io.reactivex.rxjava3.disposables.Disposable;

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
    private Disposable sessionDisposable;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        controllerLogin = new ControllerLogin(fm, this);
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.btn_login);
        googleLogin = findViewById(R.id.image_google);
        intentRegister = new Intent(this, Register.class);
        btn_register = findViewById(R.id.btn_signin);

        /* ---------------------- ACCESSO UTENTE --------------------------------------------- */
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        edtEmail.setText("carminefb@live.it");
        edtPassword.setText("Carmine13-");

        sessionDisposable = RxAmplify.Auth.fetchAuthSession()
                .subscribe(
                        result -> Log.i("SESSION", result.toString()),
                        error -> Log.e("SESSIONERR", error.toString())
                );

        btn_login.setOnClickListener(view ->
        {
            email = edtEmail.getText().toString();
            password = edtPassword.getText().toString();

            controllerLogin.checkLoginCognito(email, password);
        });
        btn_register.setOnClickListener(view -> startActivity(intentRegister));

        facebookLogin = findViewById(R.id.image_facebook);
        facebookLogin.setOnClickListener(view -> controllerLogin.checkLoginFacebook());

        googleLogin = findViewById(R.id.image_google);
        googleLogin.setOnClickListener(view -> controllerLogin.checkLoginGoogle());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        sessionDisposable.dispose();
        controllerLogin.terminaLogin();
    }
}