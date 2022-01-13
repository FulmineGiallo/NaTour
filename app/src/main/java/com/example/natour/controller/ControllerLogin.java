package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.TabActivity;
import com.example.natour.view.ErrorDialog;

import java.sql.SQLException;

public class ControllerLogin
{
    Intent intentHomePage;
    private FragmentManager fragmentManager;
    private Context contexController;

    public ControllerLogin(FragmentManager fragmentManager, Context contexController)
    {
        this.fragmentManager = fragmentManager;
        this.contexController = contexController;
    }

    public boolean checkLogin(String email, String password) throws SQLException, Exception
    {
        int accesso = 1; //1 presente , 0 assente
        boolean check = false;

        UtenteDAO utente = new UtenteDAO();
        utente.utenteExist(email, password, contexController);


        /* Se il login va a buon fine, bisogna caricare TabActivity */
        if(accesso == 0)
        {
            intentHomePage = new Intent(contexController, TabActivity.class);
            contexController.startActivity(intentHomePage);

            return check;
        }
        /* Se il login non va a buon fine, bisogna caricare la pagina di errore */
        if(accesso == 1)
        {
            ErrorDialog errorLogin = new ErrorDialog("Login errato");
            errorLogin.show(fragmentManager, "ERROR");
        }
        return check;

    }
}
