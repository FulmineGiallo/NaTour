package com.example.natour.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.natour.model.connection.DBConnection;
import com.example.natour.model.dao.UtenteDAO;
import com.example.natour.view.Login;
import com.example.natour.view.TabActivity;

import java.sql.Connection;
import java.sql.SQLException;

public class ControllerLogin
{
    Login loginActivity;
    Button btnLogin;
    Connection connection;
    Intent intentHomePage;


    public void checkLogin(String email, String password, Context activityPrec) throws SQLException, Exception
    {


        int accesso = 0; //1 presente , 0 assente

        UtenteDAO utente = new UtenteDAO();
        utente.utenteExist(0, activityPrec);


        /* Se il login va a buon fine, bisogna caricare TabActivity */
        if(accesso == 0)
        {
            intentHomePage = new Intent(activityPrec, TabActivity.class);
            activityPrec.startActivity(intentHomePage);

        }
        /* Se il login non va a buon fine, bisogna caricare la pagina di errore */
        if(accesso == 0)
        {
            //TODO: Fare interfaccia di errore
        }

    }
}
