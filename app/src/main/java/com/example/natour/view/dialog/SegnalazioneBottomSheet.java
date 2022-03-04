package com.example.natour.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.natour.R;
import com.example.natour.controller.ControllerVisualizzaItinerario;
import com.example.natour.model.Segnalazione;
import com.example.natour.view.adapter.SegnalazioniAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class SegnalazioneBottomSheet extends BottomSheetDialogFragment
{
    CallbackSegnalazione listener;
    SegnalazioniAdapter segnalazioniAdapter;
    ControllerVisualizzaItinerario controller;

    public SegnalazioneBottomSheet(List<Segnalazione> segnalazioneList, ControllerVisualizzaItinerario controller){
        this.controller = controller;
        segnalazioniAdapter = new SegnalazioniAdapter(segnalazioneList, getParentFragmentManager(), controller, requireContext());
        getSegnalazioni();
    }

    private void getSegnalazioni()
    {
        //todo: query per le segnalazioni e aggiornamento adapter

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.segnalazione_bottom_sheet, container);
        v.findViewById(R.id.btn_invia_segnalazione).setOnClickListener(view -> {
            listener.callbackSegnalazione(((EditText)v.findViewById(R.id.edt_testo_segnalazione)).getText().toString());
        });
        return v;
    }

    public interface CallbackSegnalazione{
        void callbackSegnalazione(String segnalazione);
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            listener = (CallbackSegnalazione) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    " must implement CallbackSegnalazione");
        }
    }
}
