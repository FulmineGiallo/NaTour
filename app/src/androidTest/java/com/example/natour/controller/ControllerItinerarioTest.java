package com.example.natour.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.example.natour.exception.IllegalPositionException;
import com.example.natour.model.Immagine;

import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class ControllerItinerarioTest
{

    /*
     * Questo metodo determina se la distanza tra l'immagine inserita e i punti dell'itinerario
     * sono effettivamente vicini (100 metri di differenza) tra loro.
     * */

    /*
     * Immagine ha dei campi latitudine e longitudine
     *
     * I waypoints sono una lista di geopoints con latitudine e longitudine
     * */

    /*il metodo preso in esame verrà testato con la metodologia whitebox con
     *
     * strategia di */

    /**
     * Branch esaminato: lancia l'eccezione dopo aver confrontato almeno un geopoint
     * correttamente con la posizione dell'immagine
     * */

    @Test
    public void isImageValid1(){
        Immagine immagine = new Immagine();
        immagine.setLatitude(15);
        immagine.setLongitude(34);
        GeoPoint geoPoint = new GeoPoint(10f,35f);
        GeoPoint geoPoint1 = new GeoPoint(-300f, -300f);
        ControllerItinerario controllerItinerario = new ControllerItinerario(null,null,null);
        List<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(geoPoint);
        waypoints.add(geoPoint1);
        try{
            controllerItinerario.isImageValid(immagine, waypoints);
            fail();
        }catch (IllegalPositionException e){
            e.printStackTrace();
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /**
     * branch esaminato: dopo aver esaminato i geopoints nella lista e averli trovati corretti
     * lancia l'eccezione per la latitudine e longitudine invalidi dell'immagine
     * */

    @Test
    public void isImageValid2()
    {

        Immagine immagine = new Immagine();
        immagine.setLatitude(-300);
        immagine.setLongitude(300);
        GeoPoint geoPoint = new GeoPoint(15f,26f);
        ControllerItinerario controllerItinerario = new ControllerItinerario(null,null,null);
        List<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(geoPoint);
        try{
            controllerItinerario.isImageValid(immagine, waypoints);
            fail();
        }catch (IllegalPositionException e){
            e.printStackTrace();
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

/**
 * TODO: da testare
 * in questo branch: dopo aver controllato che gli indirizzi sono inseriti correttamente
 * verifica che il secondo punto è più vicino e lo selezione come distanza minima.
 * In fine restituisce true in quanto la distanza tra il punto minore e il punto dell'immagine è
 * inferiore a 300 metri
 * */

    @Test
    public void isImageValid3()
    {

        Immagine immagine = new Immagine();
        immagine.setLatitude(40.899628001261995);
        immagine.setLongitude(14.092006225335721);
        GeoPoint geoPoint = new GeoPoint(40.89839534981892, 14.092006225335721);
        GeoPoint geoPoint1 = new GeoPoint(40.89969534981892,14.092006225335731);
        ControllerItinerario controllerItinerario = new ControllerItinerario(null,null,null);
        List<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(geoPoint);
        waypoints.add(geoPoint1);
        assertTrue(controllerItinerario.isImageValid(immagine, waypoints));
    }
}