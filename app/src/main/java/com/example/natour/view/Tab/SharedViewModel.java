package com.example.natour.view.Tab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.natour.model.Immagine;
import com.example.natour.model.Itinerario;
import com.example.natour.model.Utente;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel
{
    MutableLiveData<Utente> utenteMutableLiveData = new MutableLiveData<Utente>();
    private final MutableLiveData<ArrayList<Itinerario>> itinerario = new MutableLiveData<>();
    private final MutableLiveData<List<Immagine>> immagineList = new MutableLiveData<>();


    public void setUtente(Utente utente)
    {
        utenteMutableLiveData.postValue(utente);
    }
    public LiveData<Utente> getUtente()
    {
        return utenteMutableLiveData;
    }


    public void setItinerari(ArrayList<Itinerario> itinerario)
    {
        this.itinerario.postValue(itinerario);
    }
    public LiveData<ArrayList<Itinerario>> getItinerari(){
        return itinerario;
    }

    public void setImmagini(List<Immagine> immagineList)
    {
        this.immagineList.postValue(immagineList);
    }
}
