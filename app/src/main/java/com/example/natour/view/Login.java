package com.example.natour.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.natour.R;
import com.google.android.material.tabs.TabLayout;

public class Login extends AppCompatActivity {

    Intent intentHomePage;
    Intent intentRegister;
    Button btn_register;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intentHomePage = new Intent(this, TabActivity.class);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(intentHomePage);
                //TODO: inserire metodi per effettuare effettivamente il login
            }
        });
        intentRegister = new Intent(this, Register.class);
        btn_register = findViewById(R.id.btn_signin);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentRegister);
            }
        });
    }
}