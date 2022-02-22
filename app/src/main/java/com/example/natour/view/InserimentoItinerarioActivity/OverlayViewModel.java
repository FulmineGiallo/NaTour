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
    private final MutableLiveData<List<GeoPoint>> listaPuntiFoto = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<GeoPoint>> waypoints = new MutableLiveData<>();


    public LiveData<List<GeoPoint>> getListPhoto(){
        return listaPuntiFoto;
    }
    public LiveData<ArrayList<GeoPoint>> getWaypoints() {
        return waypoints;
    }

    public void addPhoto(GeoPoint photo){
        if(listaPuntiFoto.getValue() == null)
            inizializzaLista();
        listaPuntiFoto.getValue().add(photo);
    }

    public void addWaypoint(GeoPoint waypoint){
        if(waypoints.getValue() == null)
            inizializzaWayPoints();
        waypoints.getValue().add(waypoint);
    }

    public void inizializzaWayPoints(){
        waypoints.setValue(new ArrayList<>());
    }
    public void inizializzaLista(){
        listaPuntiFoto.setValue(new LinkedList<>());
    }
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
}
