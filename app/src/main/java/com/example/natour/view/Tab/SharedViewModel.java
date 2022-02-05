package com.example.natour.view.Tab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.natour.model.Utente;

public class SharedViewModel extends ViewModel
{
    MutableLiveData<Utente> utenteMutableLiveData = new MutableLiveData<Utente>();

    public void setUtente(Utente utente)
    {
        utenteMutableLiveData.postValue(utente);
    }

    public LiveData<Utente> getUtente()
    {
        return utenteMutableLiveData;
    }
}
