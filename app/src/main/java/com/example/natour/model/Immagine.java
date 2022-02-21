package com.example.natour.model;

import android.net.Uri;


public class Immagine
{
    private Uri uri;
    private String key;

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
}
