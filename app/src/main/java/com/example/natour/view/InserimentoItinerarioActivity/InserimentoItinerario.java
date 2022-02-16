package com.example.natour.view.InserimentoItinerarioActivity;//package com.example.natour.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.natour.R;
import com.example.natour.controller.ControllerItinerario;

public class InserimentoItinerario extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_itinerario);

        ControllerItinerario controllerItinerario = new ControllerItinerario(getSupportFragmentManager(), this);
        controllerItinerario.inizializzaInterfaccia();
    }


}