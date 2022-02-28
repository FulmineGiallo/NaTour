package com.example.natour.view.InserimentoItinerarioActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.natour.R;
import com.example.natour.view.Tab.TabActivity;

public class ItinerarioInseritoTransition extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerario_inserito_transition);

        new Handler().postDelayed(() -> startActivity(new Intent(this, TabActivity.class)),5000);
    }
}