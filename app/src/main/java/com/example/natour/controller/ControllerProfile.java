package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;

import com.example.natour.view.Signout;


public class ControllerProfile
{
    private FragmentManager fragmentManager;
    private Context contexController;
    Intent intentLogin;

    public ControllerProfile(FragmentManager fragmentManager, Context contexController)
    {
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
        intentLogin = new Intent(contexController, Signout.class);
    }
    public void signOut()
    {
        contexController.startActivity(intentLogin);


    }
}
