package com.example.natour.view.InserimentoItinerarioActivity;//package com.example.natour.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.natour.R;

public class InserimentoItinerario extends AppCompatActivity
{




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_itinerario);

        if(findViewById(R.id.fragmentMappa) != null)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.fragmentMappa, new InserimentoItinerarioFragment());
            ft.commit();
        }
    }


}