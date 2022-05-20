package com.example.natour.model;

import android.net.Uri;
import android.util.Log;

import org.osmdroid.views.overlay.Marker;


public class Immagine
{
    private Uri uri;
    private String key;
    private Marker marker;
    private String URL;

    private Double latitude;
    private Double longitude;


    public String getURL()
    {
        return URL;
    }

    public void setURL(String URL)
    {
        this.URL = URL;
    }

    public Immagine(){}

    public Immagine(String URL){
        this.URL = URL;
    }

    public Immagine(Uri uri, String key)
    {
        this.uri = uri;
        this.key = key;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public Uri getUri()
    {
        return uri;
    }

    public void setUri(Uri uri)
    {
        this.uri = uri;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public Marker getMarker()
    {
        return marker;
    }

    public void setMarker(Marker marker)
    {
        Log.i("Immagine", " marker associato all'immagine " + marker.toString());
        this.marker = marker;
    }
}
