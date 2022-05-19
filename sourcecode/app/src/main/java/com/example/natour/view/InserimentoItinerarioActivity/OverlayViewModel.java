package com.example.natour.view.InserimentoItinerarioActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.natour.model.Immagine;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OverlayViewModel extends ViewModel
{
    private MutableLiveData<GeoPoint> puntoInizio = new MutableLiveData<>();
    private final MutableLiveData<GeoPoint> puntoFine = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<GeoPoint>> waypoints = new MutableLiveData<>();
    private final MutableLiveData<LinkedList<Immagine>> imgList = new MutableLiveData<>();


    public void setWaypoints(ArrayList<GeoPoint> waypoints){
        this.waypoints.postValue(waypoints);
    }
    public void setInizio(GeoPoint marker)
    {
        puntoInizio.postValue(marker);
    }
    public void setFine(GeoPoint marker)
    {
        puntoFine.postValue(marker);
    }
    public void setImgList(LinkedList<Immagine> imgList){
        this.imgList.postValue(imgList);
    }

    public LiveData<LinkedList<Immagine>> getImgList(){
        return imgList;
    }
    public LiveData<GeoPoint> getInizio()
    {
        return puntoInizio;
    }
    public LiveData<GeoPoint> getFine()
    {
        return puntoFine;
    }
    public LiveData<ArrayList<GeoPoint>> getWaypoints(){
        return waypoints;
    }
}
