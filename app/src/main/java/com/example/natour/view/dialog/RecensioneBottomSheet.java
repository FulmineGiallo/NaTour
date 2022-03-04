package com.example.natour.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.natour.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RecensioneBottomSheet extends BottomSheetDialogFragment
{
    CallbackRecensione listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.inserisci_recensione_bottom_sheet, container);
        //v.findviewbyid
        Button btn = v.findViewById(R.id.btn_conferma_recensione);
        btn.setOnClickListener(view ->
        {
            listener.callbackRecensione(((RatingBar) v.findViewById(R.id.rate_voto_dato)).getNumStars(),
                    ((EditText) v.findViewById(R.id.edt_testo_recensione)).getText().toString());
            dismiss();
        });


        return v;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            listener = (CallbackRecensione) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    " must implement CallbackRecensione");
        }
    }

    public interface CallbackRecensione{
        void callbackRecensione(int rate, String recensione);

    }
}
