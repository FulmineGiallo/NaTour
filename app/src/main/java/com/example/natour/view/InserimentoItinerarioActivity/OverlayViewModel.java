package com.example.natour.view.InserimentoItinerarioActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OverlayViewModel extends ViewModel
{
    private final MutableLiveData<GeoPoint> puntoInizio = new MutableLiveData<>();
    private final MutableLiveData<GeoPoint> puntoFine = new MutableLiveData<>();

    public void setInizio(GeoPoint marker)
    {
        puntoInizio.postValue(marker);
    }
    public void setFine(GeoPoint marker)
    {
        puntoFine.postValue(marker);
    }
    public LiveData<GeoPoint> getInizio()
    {
        return puntoInizio;
    }
    public LiveData<GeoPoint> getFine()
    {
        return puntoFine;
    }
}
