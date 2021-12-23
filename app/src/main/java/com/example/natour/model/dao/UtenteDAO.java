package com.example.natour.model.dao;

import com.example.natour.daointerface.UtenteDaoInterface;
import com.example.natour.model.Utente;

import java.sql.SQLException;
import java.util.Date;

public class UtenteDAO implements UtenteDaoInterface
{

    @Override
    public int utenteExist(String email, String password) throws SQLException
    {
        return 0;
    }

    @Override
    public Utente creaUtente(String nome, String cognome, String email, String passowrd, Date data) throws SQLException
    {
        return null;
    }
}

