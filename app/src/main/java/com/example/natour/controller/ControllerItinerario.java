package com.example.natour.controller;

import com.example.natour.model.Itinerario;

import java.io.File;
import java.util.List;

public class ControllerItinerario
{
    private String nomeItinerario;
    private String durataItinerario;
    private boolean disabiliItinerario;
    private int difficoltàItinerario; //Ha 5 possibili valori, da 1 a 5
    private File gpx;
    private String descrizioneItnerario;
    private List<String> listaImmagini; //Questa lista contiene tutti i link alle immagine inserite




    public Itinerario inserisciItinerario(String nome, String durata, boolean disabili, File gpx, String descrizione, List<String> immagini)
    {
        Itinerario itinerarioInserito = new Itinerario();
        /* INSERIMENTO DELL'ID ALL'INTERNO DEL DATABASE E DELLE SUE INFORMAZIONI DI BASE */





        return itinerarioInserito;
    }





}
