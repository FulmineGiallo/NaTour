package com.example.natour;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.natour.controller.ControllerCerca;
import com.example.natour.model.Itinerario;
import com.example.natour.view.Tab.CercaFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest extends ActivityIns {

    public ExampleInstrumentedTest()
    {

    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        CercaFragment cercaFragment = CercaFragment.newInstance("","");
        FragmentManager fragmentManager = cercaFragment.getParentFragmentManager();
        ArrayList<Itinerario> list = new ArrayList<>();
        Itinerario itinerario1 = new Itinerario();
        list.add(itinerario1);
        ControllerCerca controllerCerca = new ControllerCerca(fragmentManager, cercaFragment, list, null);
        controllerCerca.filtraItinerarioWithFilter("napoli", 2, "13", false);
        assertEquals(0, controllerCerca.getCopiaLista().size());
        assertEquals("com.example.natour", appContext.getPackageName());

    }
}