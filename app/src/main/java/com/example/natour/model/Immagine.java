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

    public String getURL()
    {
        return URL;
    }

    public void setURL(String URL)
    {
        this.URL = URL;
    }

    public Immagine(Uri uri, String key)
    {
        this.uri = uri;
        this.key = key;
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
