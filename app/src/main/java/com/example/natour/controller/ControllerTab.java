package com.example.natour.controller;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.amplifyframework.auth.AuthUserAttribute;
import com.example.natour.model.Utente;
import com.example.natour.model.dao.UtenteDAO;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ControllerTab
{
    private FragmentManager fragmentManager;
    private Context contexController;

    public ControllerTab(FragmentManager fragmentManager, Context contexController)
    {
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
    }

    public Utente loadingUtente()
    {
        UtenteDAO utenteDati = new UtenteDAO();
        Utente utenteLoggato = new Utente();


        return utenteLoggato;
    }
}
