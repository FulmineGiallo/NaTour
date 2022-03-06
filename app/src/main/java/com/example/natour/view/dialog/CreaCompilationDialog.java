package com.example.natour.view.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.natour.R;

public class CreaCompilationDialog extends DialogFragment
{

    CompilationCallback callback;

    public CreaCompilationDialog(CompilationCallback callback){
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.crea_compilation_dialog, container);
        Button btn = v.findViewById(R.id.btn_crea_compilation);
        EditText edt_titolo = v.findViewById(R.id.edt_titolo_compilation);
        EditText edt_descrizione = v.findViewById(R.id.edt_descrizione_compilation);
        btn.setOnClickListener(view -> {
            String titolo = edt_titolo.getText().toString();
            String descrizione = edt_descrizione.getText().toString();
            callback.creaCompilation(titolo, descrizione);
            dismiss();
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface CompilationCallback{
        void creaCompilation(String titolo, String descrizione);
    }
}
