package com.example.natour.model.daointerface;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.natour.model.Utente;
import com.example.natour.model.connection.SimpleCallBackInterface;

import java.sql.SQLException;
import java.util.Date;

public interface UtenteDaoInterface
{
    void getInformationOfAmplifySession(Context context, Utente utente);
    void setTokenUtente(String token, String dataDiNascita,Context activityPrec);
}
