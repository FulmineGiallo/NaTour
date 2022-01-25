package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;

import com.example.natour.view.TabActivity;

public class ControllerHomePage
{
    private FragmentManager fragmentManager;
    private Context contexController;

    public ControllerHomePage(FragmentManager fragmentManager, Context contexController)
    {
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
    }



}
