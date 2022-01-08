package com.example.natour.model.daointerface;

import com.example.natour.model.Utente;

import java.sql.SQLException;
import java.util.Date;

public interface UtenteDaoInterface
{
    /* Metodo per vedere se un utente esiste */
    int utenteExist(String email, String password)throws SQLException;

    /* Registrazione Utente */
    Utente creaUtente(String nome, String cognome, String email, String passowrd, Date data)throws SQLException;


}
