package com.example.natour.controller;

import android.content.Context;

import com.example.natour.model.Itinerario;

import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ControllerCercaTest extends TestCase
{
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
    }

    public void testFiltraItinerarioWithFilter()
    {
        ArrayList<Itinerario> list = new ArrayList<>();
        Itinerario itinerario1 = new Itinerario();
        list.add(itinerario1);

        ControllerCerca controllerCerca = new ControllerCerca(null,null, list,null);
        controllerCerca.filtraItinerarioWithFilter("Napoli",1,"10",false);

        assertEquals(0, controllerCerca.getCopiaLista().size());
    }


}