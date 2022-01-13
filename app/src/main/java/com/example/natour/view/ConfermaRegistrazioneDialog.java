package com.example.natour.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.natour.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ConfermaRegistrazioneDialog extends BottomSheetDialogFragment
{
    private BottomSheetListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.conferma_registrazione_bottom_sheet,container, false);

        Button conferma = v.findViewById(R.id.btn_confermaCodice);
        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonClicked(((EditText)(v.findViewById(R.id.edt_codice_conferma))).getText().toString());

                dismiss();
            }
        });

        return v;
    }

    public interface BottomSheetListener{
        void onButtonClicked(String codice);
        /*
        * come argomenti a questo metodo si possono mettere
        * qualsiasi tipo di dato, esso sarà passato all'attività
        * che chiama questo dialog
        * */
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+
                    " must implement BottomSheetListener");
        }
    }
}
