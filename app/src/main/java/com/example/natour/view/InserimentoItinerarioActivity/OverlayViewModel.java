package com.example.natour.view.InserimentoItinerarioActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.util.GeoPoint;

public class OverlayViewModel extends ViewModel
{
    private MutableLiveData<GeoPoint> puntoInizio = new MutableLiveData<GeoPoint>();
    private MutableLiveData<GeoPoint> puntoFine =   new MutableLiveData<GeoPoint>();

    public void setInizio(GeoPoint marker)
    {
        puntoInizio.setValue(marker);
    }
    public void setFine(GeoPoint marker)
    {
        puntoFine.setValue(marker);
    }
    public LiveData<GeoPoint> getInizio()
    {
        return puntoInizio;
    }
    public LiveData<GeoPoint> getFine()
    {
        return puntoFine;
    }


    public void removeInizio()
    {
        if(puntoInizio.getValue() != null)
            puntoInizio = new MutableLiveData<>();
    }
    public void removeFine()
    {
        if(puntoFine.getValue() != null)
            puntoFine = new MutableLiveData<>();

    }
}
