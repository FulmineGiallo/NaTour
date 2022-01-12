package com.example.natour.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.natour.R;

public class errorDialog extends DialogFragment
{
    Button confermaErrore;
    String errorMSG;
    TextView msg_errore;
    Context context;

    public errorDialog(String errorMSG)
    {
        this.errorMSG = errorMSG;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_error_dialog, container, false);

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

    /*public void showDialogError(Context context, String errorMSG)
    {
        setContentView(R.layout.activity_error_dialog);
        msg_errore = findViewById(R.id.txt_errore);
        msg_errore.setText(errorMSG);

        confermaErrore = findViewById(R.id.btn_errore);

        final Dialog dialogError = new Dialog(context);
        dialogError.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogError.setCancelable(true);
        dialogError.setContentView(R.layout.activity_error_dialog);



        dialogError.show();


    }*/
}