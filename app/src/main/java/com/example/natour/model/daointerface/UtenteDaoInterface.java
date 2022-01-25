package com.example.natour.model.daointerface;

import android.content.Context;

import com.example.natour.model.Utente;

import java.sql.SQLException;
import java.util.Date;

public interface UtenteDaoInterface
{
    public Utente getInformationOfAmplifySession();
    public int utenteExist(String email, String password, Context activityPrec);
}
