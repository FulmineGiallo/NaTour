package com.example.natour.controller;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.natour.model.Itinerario;

import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class ControllerCercaTest
{

    /*
    * Uno stub del catch non può essere verificato in
    * quanto sarebbe necessario imporre al geocoder di non funzionare
    * oppure mettere la modalità aereo,
    * cosa non possibile in fase di test.
    * Nel caso in cui il geocoder dovrebbe fallire si comporta come
    * se non ci fossero itinerari corrispondenti
    * all'address ricercato
    * */

    /* il punto 40.85157508957582, 14.26479657279487 è stato preso
    * con servizi esterni e indica la posizione di napoli*/

    @Test
    public void testfilterResultCorretto()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Itinerario> itinerari = new ArrayList<>();
        Itinerario itinerario = new Itinerario();
        itinerario.setNome("Napoli che bella");
        itinerario.setDifficoltà(3);
        itinerario.setDescrizione("una bella passeggiata a napoli");
        itinerario.setDurata("13");
        itinerario.setAccessibilitaDisabili(false);
        GeoPoint punto = new GeoPoint(40.85157508957582, 14.26479657279487);
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(punto);
        itinerario.setWaypoints(waypoints);
        itinerari.add(itinerario);
        ControllerCerca controllerCerca = new ControllerCerca(null, null, itinerari, null);
        Boolean exist;
        exist = controllerCerca.filterResult("napoli",3,"13",false, appContext);
        assertTrue(exist);

    }

    @Test
    public void testfilterResultWithoutAddress()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Itinerario> itinerari = new ArrayList<>();
        Itinerario itinerario = new Itinerario();
        itinerario.setNome("Napoli che bella");
        itinerario.setDifficoltà(3);
        itinerario.setDescrizione("una bella passeggiata a napoli");
        itinerario.setDurata("13");
        itinerario.setAccessibilitaDisabili(false);
        GeoPoint punto = new GeoPoint(40.85157508957582, 14.26479657279487);
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(punto);
        itinerario.setWaypoints(waypoints);
        itinerari.add(itinerario);
        ControllerCerca controllerCerca = new ControllerCerca(null, null, itinerari, null);
        Boolean exist;
        exist = controllerCerca.filterResult("",3,"13",false, appContext);
        assertTrue(exist);

    }

    @Test
    public void testfilterResultWrongAddress()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Itinerario> itinerari = new ArrayList<>();
        Itinerario itinerario = new Itinerario();
        itinerario.setNome("Napoli che bella");
        itinerario.setDifficoltà(3);
        itinerario.setDescrizione("una bella passeggiata a napoli");
        itinerario.setDurata("13");
        itinerario.setAccessibilitaDisabili(false);
        GeoPoint punto = new GeoPoint(40.85157508957582, 14.26479657279487);
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(punto);
        itinerario.setWaypoints(waypoints);
        itinerari.add(itinerario);
        ControllerCerca controllerCerca = new ControllerCerca(null, null, itinerari, null);
        Boolean exist;
        exist = controllerCerca.filterResult("xxxxxxxx",3,"13",false, appContext);
        assertFalse(exist);

    }

    @Test
    public void testfilterResultWithoutDurata()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Itinerario> itinerari = new ArrayList<>();
        Itinerario itinerario = new Itinerario();
        itinerario.setNome("Napoli che bella");
        itinerario.setDifficoltà(3);
        itinerario.setDescrizione("una bella passeggiata a napoli");
        itinerario.setDurata("13");
        itinerario.setAccessibilitaDisabili(false);
        GeoPoint punto = new GeoPoint(40.85157508957582, 14.26479657279487);
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(punto);
        itinerario.setWaypoints(waypoints);
        itinerari.add(itinerario);
        ControllerCerca controllerCerca = new ControllerCerca(null, null, itinerari, null);
        Boolean exist;
        exist = controllerCerca.filterResult("napoli",3,"",false, appContext);
        assertTrue(exist);

    }

    @Test
    public void testfilterResultWithoutDurataNorAddress()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Itinerario> itinerari = new ArrayList<>();
        Itinerario itinerario = new Itinerario();
        itinerario.setNome("Napoli che bella");
        itinerario.setDifficoltà(3);
        itinerario.setDescrizione("una bella passeggiata a napoli");
        itinerario.setDurata("13");
        itinerario.setAccessibilitaDisabili(false);
        GeoPoint punto = new GeoPoint(40.85157508957582, 14.26479657279487);
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(punto);
        itinerario.setWaypoints(waypoints);
        itinerari.add(itinerario);
        ControllerCerca controllerCerca = new ControllerCerca(null, null, itinerari, null);
        Boolean exist;
        exist = controllerCerca.filterResult("",3,"",false, appContext);
        assertTrue(exist);

    }

    @Test
    public void testfilterResultNoWaypoint()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Itinerario> itinerari = new ArrayList<>();
        Itinerario itinerario = new Itinerario();
        itinerario.setNome("Napoli che bella");
        itinerario.setDifficoltà(3);
        itinerario.setDescrizione("una bella passeggiata a napoli");
        itinerario.setDurata("13");
        itinerario.setAccessibilitaDisabili(false);
        itinerari.add(itinerario);
        ControllerCerca controllerCerca = new ControllerCerca(null, null, itinerari, null);
        try{
            controllerCerca.filterResult("napoli",3,"13",false, appContext);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

}