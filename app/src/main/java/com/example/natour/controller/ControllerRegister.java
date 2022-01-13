package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;

import com.example.natour.view.ConfermaRegistrazioneDialog;

public class ControllerRegister
{
    private Intent registerToLogin;
    private FragmentManager fragmentManager;
    private Context activity;

    public ControllerRegister(FragmentManager fragmentManager, Context context){
        this.fragmentManager = fragmentManager;
        this.activity = context;
    }


    public void inserimentoCodice() {
        ConfermaRegistrazioneDialog bottomSheet = new ConfermaRegistrazioneDialog();
        bottomSheet.show(fragmentManager, "confermaRegistrazione");
    }
}
