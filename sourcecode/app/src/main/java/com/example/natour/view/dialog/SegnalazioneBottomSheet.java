package com.example.natour.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    List<Segnalazione> segnalazioneList;

    public SegnalazioneBottomSheet(List<Segnalazione> segnalazioneList, ControllerVisualizzaItinerario controller){
        this.controller = controller;
        this.segnalazioneList = segnalazioneList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.segnalazione_bottom_sheet, container);
        EditText titolo = v.findViewById(R.id.edt_titolo_segnalazione);
        EditText descrizione = v.findViewById(R.id.edt_testo_segnalazione);
        v.findViewById(R.id.btn_invia_segnalazione).setOnClickListener(view -> {
            if(descrizione.length() > 140 || titolo.length() > 50){
                new ErrorDialog("Testo inserito troppo grande, titolo di massimo 50 caratteri e descrizione di 140");
            }
            else{
                listener.callbackSegnalazione(descrizione.getText().toString(),
                        titolo.getText().toString());
                dismiss();
            }
        });
        segnalazioniAdapter = new SegnalazioniAdapter(segnalazioneList, getParentFragmentManager(), controller, requireContext());
        RecyclerView recyclerView = v.findViewById(R.id.rec_view_segnalazioni);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(segnalazioniAdapter);
        controller.updateAdapter(segnalazioniAdapter);


        return v;
    }

    public interface CallbackSegnalazione{
        void callbackSegnalazione(String segnalazione, String titolo);
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
