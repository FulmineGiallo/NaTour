package com.example.natour.view;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.example.natour.R;
import com.example.natour.model.connection.CognitoSettings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity
{
    private EditText nome;
    private EditText cognome;
    private EditText email;
    private EditText password;
    private EditText dataDiNascita;
    private Button btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    private void registerUser()
    {
        nome = findViewById(R.id.edt_nome);
        cognome = findViewById(R.id.edt_cognome);

        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);

        dataDiNascita = findViewById(R.id.edt_date);

        CognitoUserAttributes userAttributes = new CognitoUserAttributes();

        SignUpHandler signupCallBack = new SignUpHandler() {
            @Override
            public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails)
            {
                Log.i("NATOUR", "Registrazione avvenuta" + signUpConfirmationState);

                if(!signUpConfirmationState)
                {
                    Log.i("NATOUR", "Registrazione non confermata, codice di verifica inviato a " + cognitoUserCodeDeliveryDetails.getDestination());
                }
                else
                {
                    Log.i("NATOUR", "Registrazione avvenuta, confermata");
                }
            }

            @Override
            public void onFailure(Exception exception)
            {
                Log.i("NATOUR", "Registrazione fallita" + exception.getLocalizedMessage());
            }
        };

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                userAttributes.addAttribute("name", String.valueOf(nome.getText()));
                userAttributes.addAttribute("family_name", String.valueOf(cognome.getText()));
                userAttributes.addAttribute("email", String.valueOf(email.getText()));
                userAttributes.addAttribute("birthdate",String.valueOf(dataDiNascita.getText()));
                CognitoSettings cognitoSettings = new CognitoSettings(Register.this);

                cognitoSettings.getUserPool().signUpInBackground(String.valueOf(email.getText()), String.valueOf(password.getText()),
                        userAttributes, null, signupCallBack);
            }
        });

    }
}