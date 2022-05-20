package com.example.natour.view.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
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
    private Integer icon = null;


    public ErrorDialog(String errorMSG)
    {
        this.errorMSG = errorMSG;
    }

    public ErrorDialog(String errorMSG, int icon) {
        this.errorMSG = errorMSG;
        this.icon = icon;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.layout_error_dialog, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        msg_errore = v.findViewById(R.id.txt_errore);
        ImageView imageView = v.findViewById(R.id.img_waringLogin);
        if(icon != null)
            imageView.setImageResource(icon);
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