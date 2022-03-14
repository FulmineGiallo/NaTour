package com.example.natour.view.MessaggioActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.natour.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class MessaggioFragment extends Fragment
{
    private TextView txtMessaggiVuoti;

    public MessaggioFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_messaggio, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        txtMessaggiVuoti = getView().findViewById(R.id.txt_msgRicevuti);
        txtMessaggiVuoti.setVisibility(View.INVISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        boolean messaggiRicevuti = false;

        /* Inizializzazione MESSAGGI */
        /* - Vedo se quell'utente ha messaggi altrimenti mostro la schermata di base */


        /* Se non ha messaggi */
        if(messaggiRicevuti == false)
        {
            txtMessaggiVuoti.setVisibility(View.VISIBLE);
        }
        else
        {
            /* Visualizzo le chat con gli altri utenti */

        }



    }
}