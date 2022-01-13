package com.example.natour.view;

import androidx.appcompat.app.AppCompatActivity;

import com.example.natour.R;
import com.example.natour.controller.ControllerRegister;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity implements ConfermaRegistrazioneDialog.BottomSheetListener
{
    private EditText nome;
    private EditText cognome;
    private EditText email;
    private EditText password;
    private EditText dataDiNascita;
    private Button btn_register;
    ControllerRegister controllerRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        controllerRegister = new ControllerRegister(getSupportFragmentManager(), this);
        nome = findViewById(R.id.edt_nome);
        cognome = findViewById(R.id.edt_cognome);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        dataDiNascita = findViewById(R.id.edt_date);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                controllerRegister.registerUser(String.valueOf(nome.getText()),String.valueOf(cognome.getText()),
                        String.valueOf(email.getText()),String.valueOf(password.getText()),String.valueOf(dataDiNascita.getText()));
            }
        });
    }

    @Override
    public void onButtonClicked(String codice)
    {
        controllerRegister.verficaCodiceCognito(codice, String.valueOf(email.getText()));

    }
}