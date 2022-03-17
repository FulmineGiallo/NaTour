package com.example.natour.view.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.natour.R;

public class ConfermaDialog extends DialogFragment
{
    private Button conferma;
    private Button annulla;
    private String messaggioDialog;
    private TextView messaggio;
    private ConfermaDialogInterfaccia confermaDialogInterfaccia;

    public ConfermaDialog(String messaggioDialog, ConfermaDialogInterfaccia confermaDialogInterfaccia)
    {
        this.messaggioDialog = messaggioDialog;
        this.confermaDialogInterfaccia = confermaDialogInterfaccia;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.layout_conferma_dialog, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        messaggio = v.findViewById(R.id.txt_dialog);
        messaggio.setText(messaggioDialog);
        conferma  = v.findViewById(R.id.btn_conferma);
        annulla   = v.findViewById(R.id.btn_annulla);

        conferma.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                confermaDialogInterfaccia.actionConferma();
                dismiss();
            }
        });

        annulla.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                confermaDialogInterfaccia.actionAnnulla();
                dismiss();
            }
        });
        return v;
    }


}
