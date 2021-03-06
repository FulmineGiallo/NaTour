package com.example.natour.model;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Itinerario implements Serializable
{
    private String idItinerario;
    private String nome;
    private String durata;
    private boolean accessibilitaDisabili;
    private int difficolt√†; //va da 1 a 5
    private String descrizione;
    private ArrayList<GeoPoint> waypoints; //Percorso
    private String fk_utente; //Chi ha creato l'itinerario
    private String nomeFile;
    private LinkedList<Immagine> immagini = new LinkedList<>();

    public LinkedList<Immagine> getImmagini()
    {
        return immagini;
    }

    public void setImmagini(LinkedList<Immagine> immagini)
    {
        this.immagini = immagini;
    }

    public String getNomeFile()
    {
        return nomeFile;
    }

    public void setNomeFile(String nomeFile)
    {
        this.nomeFile = nomeFile;
    }

    public String getIdItinerario()
    {
        return idItinerario;
    }

    public void setIdItinerario(String idItinerario)
    {
        this.idItinerario = idItinerario;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getDurata()
    {
        return durata;
    }

    public void setDurata(String durata)
    {
        this.durata = durata;
    }

    public boolean isAccessibilitaDisabili()
    {
        return accessibilitaDisabili;
    }

    public void setAccessibilitaDisabili(boolean accessibilitaDisabili)
    {
        this.accessibilitaDisabili = accessibilitaDisabili;
    }

    public int getDifficolt√†()
    {
        return difficolt√†;
    }

    @Override
    public String toString()
    {
        return "Itinerario{" +
                "idItinerario='" + idItinerario + '\'' +
                ", nome='" + nome + '\'' +
                ", durata='" + durata + '\'' +
                ", accessibilitaDisabili=" + accessibilitaDisabili +
                ", difficolt√†=" + difficolt√† +
                ", descrizione='" + descrizione + '\'' +
                ", waypoints=" + waypoints +
                ", fk_utente='" + fk_utente + '\'' +
                '}';
    }

    public void setDifficolt√†(int difficolt√†)
    {
        this.difficolt√† = difficolt√†;
    }

    public String getDescrizione()
    {
        return descrizione;
    }

    public void setDescrizione(String descrizione)
    {
        this.descrizione = descrizione;
    }

    public ArrayList<GeoPoint> getWaypoints()
    {
        return waypoints;
    }

    public void setWaypoints(ArrayList<GeoPoint> waypoints)
    {
        this.waypoints = waypoints;
    }

    public String getFk_utente()
    {
        return fk_utente;
    }

    public void setFk_utente(String fk_utente)
    {
        this.fk_utente = fk_utente;
    }
}
