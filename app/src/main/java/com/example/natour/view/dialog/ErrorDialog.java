package com.example.natour.view.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.natour.R;

public class ErrorDialog extends DialogFragment
{
    private Button confermaErrore;
    private String errorMSG;
    private TextView msg_errore;


    public ErrorDialog(String errorMSG)
    {
        this.errorMSG = errorMSG;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.layout_error_dialog, container, false);
        msg_errore = v.findViewById(R.id.txt_errore);
        msg_errore.setText(errorMSG);

        confermaErrore = v.findViewById(R.id.btn_errore);
        confermaErrore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });

        return v;
    }

}