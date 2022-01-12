package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.TabActivity;
import com.example.natour.view.errorDialog;

import java.sql.SQLException;

public class ControllerLogin
{
    Intent intentHomePage;


    public boolean checkLogin(String email, String password, Context activityPrec, FragmentManager fm) throws SQLException, Exception
    {
        int accesso = 1; //1 presente , 0 assente
        boolean check = false;

        UtenteDAO utente = new UtenteDAO();
        utente.utenteExist(0, activityPrec);


        /* Se il login va a buon fine, bisogna caricare TabActivity */
        if(accesso == 0)
        {
            intentHomePage = new Intent(activityPrec, TabActivity.class);
            activityPrec.startActivity(intentHomePage);

            return check;
        }
        /* Se il login non va a buon fine, bisogna caricare la pagina di errore */
        if(accesso == 1)
        {
            errorDialog errorLogin = new errorDialog("Login errato");
            errorLogin.show(fm, "ERROR");


        }
        return check;

    }
}
