package com.example.natour.view.Tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.natour.R;


public class NotificaFragment extends Fragment
{

    private TextView txt_notifica;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_notifica, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        txt_notifica = getView().findViewById(R.id.txt_avvisonotifica);
        txt_notifica.setVisibility(View.INVISIBLE);

        boolean notificheRicevute = false;

        /* Inizializzazione MESSAGGI */
        /* - Vedo se quell'utente ha messaggi altrimenti mostro la schermata di base */


        /* Se non ha messaggi */
        if(notificheRicevute == false)
        {
            txt_notifica.setVisibility(View.VISIBLE);
        }
        else
        {
            /* Visualizzo le chat con gli altri utenti */

        }
    }
}