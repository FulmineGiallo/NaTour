package com.example.natour.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import com.example.natour.R;
import com.example.natour.controller.ControllerLogin;
import com.example.natour.model.connection.DBConnection;
import com.google.android.material.tabs.TabLayout;

import java.sql.Connection;
import java.sql.SQLException;

public class Login extends AppCompatActivity {


    Intent intentRegister;
    Button btn_register;
    Button btn_login;
    ControllerLogin controllerLogin;
    private String email;
    private String password;
    EditText edtEmail;
    EditText edtPassword;
    Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        controllerLogin = new ControllerLogin();

        btn_login = findViewById(R.id.btn_login);


        intentRegister = new Intent(this, Register.class);
        btn_register = findViewById(R.id.btn_signin);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentRegister);
            }
        });

        /* ACCESSO UTENTE --------------------------------------------- */
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);


        btn_login.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                try
                {
                    email = edtEmail.getText().toString();
                    password = edtPassword.getText().toString();

                    controllerLogin.checkLogin(email, password, Login.this);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }


        });
        /* ----------------------------------------------------------------*/
    }


}