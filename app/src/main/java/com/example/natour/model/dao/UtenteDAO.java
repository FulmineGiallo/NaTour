package com.example.natour.model.dao;

import com.example.natour.model.daointerface.UtenteDaoInterface;
import com.example.natour.model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UtenteDAO implements UtenteDaoInterface
{
    private final PreparedStatement login;
    public UtenteDAO(Connection connection) throws SQLException
    {

        login = connection.prepareStatement("SELECT Count(*) FROM Utente WHERE email = ? AND password = ?");
    }

    @Override
    public int utenteExist(String email, String password) throws SQLException
    {

        int accesso = 0; //se ritorna zero l'utente non esiste altrimenti 1

        try
        {
            login.setString(1,email);
            login.setString(2,password);
            ResultSet rs = login.executeQuery();
            while(rs.next())
            {
                accesso = rs.getInt("count");
            }
        }
        catch (SQLException error){
            //TODO: Aggiungere risposta all'errore
        }
        return accesso;
    }

    @Override
    public Utente creaUtente(String nome, String cognome, String email, String passowrd, Date data) throws SQLException
    {
        return null;
    }
}

