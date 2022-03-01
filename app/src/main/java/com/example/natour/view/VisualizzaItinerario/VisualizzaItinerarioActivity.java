package com.example.natour.view.VisualizzaItinerario;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.natour.R;
import com.example.natour.model.Itinerario;

public class VisualizzaItinerarioActivity extends AppCompatActivity
{
    private TextView nomeUtente;
    private TextView nomeItinerario;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_itinerario2);
        Itinerario itinerario;
        itinerario = (Itinerario) getIntent().getSerializableExtra("itinerarioselezionato");

        nomeUtente = findViewById(R.id.txt_nomecognome);
        nomeItinerario = findViewById(R.id.txt_nomeitinerario);
        nomeItinerario.setText(itinerario.getNome());


    }

}