package com.example.natour.view.LoginActivity;

import com.example.natour.model.Utente;

public class UtenteSingleton
{
    private static UtenteSingleton utenteSingleton;
    private Utente utente;


    private UtenteSingleton(Utente utente)
    {
        this.utente =  utente;
    }

    public static UtenteSingleton getInstance(Utente utente)
    {
        if(utenteSingleton == null)
            utenteSingleton = new UtenteSingleton(utente);

        return utenteSingleton;
    }
    public static UtenteSingleton getInstance() throws SingletonNullException
    {
        if(utenteSingleton == null)
            throw new SingletonNullException("Istanza non definita");


        return utenteSingleton;
    }

    public Utente getUtente()
    {
        return utente;
    }
}
