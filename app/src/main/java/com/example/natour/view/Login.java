package com.example.natour.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.natour.R;

public class Login extends AppCompatActivity
{
    Button entra;
    Intent homepageIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        entra = findViewById(R.id.btn_login);
        homepageIntent = new Intent(this, Homepage.class);

        entra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(homepageIntent);
            }
        });
    }
}