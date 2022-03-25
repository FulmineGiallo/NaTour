package com.example.natour.controller;

import static org.junit.Assert.*;

import com.example.natour.exception.IllegalSegnalazioneException;
import com.example.natour.model.Itinerario;
import com.example.natour.model.Segnalazione;
import com.example.natour.model.Utente;

import org.junit.Test;

public class ControllerVisualizzaItinerarioTest
{

    //il metodo testato esamina i token dell'utente dei vari oggetti
    //esamina anche se l'utente loggato sia l'admin

    /*
    * Le classi di equivalenza prese in esame sono le possibile uguaglianze tra
    * l'utente loggato, l'utente dell'itinerario e l'utente della segnalazione,
    * si sono inoltre considerati i casi in cui l'utente è admin oppure no
    * */

    /** Per il metodo seguente:
     * utente = utente itinerario = utente segnalazione
     * utente non è admin **/
    @Test
    public void validateSegnalazione1()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(utente.getToken());
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(utente.getToken());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (IllegalSegnalazioneException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente itinerario != utente segnalazione
     * utente non è admin **/
    @Test
    public void validateSegnalazione2()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(utente.getToken());
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente("token2");
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        assertFalse(controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione));
    }

    /** Per il metodo seguente:
     * utente != utente itinerario = utente segnalazione
     * utente non è admin **/
    @Test
    public void validateSegnalazione3()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(itinerario.getFk_utente());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (IllegalSegnalazioneException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }    }

    /** Per il metodo seguente:
     * utente != utente itinerario != utente segnalazione
     * utente non è admin **/
    @Test
    public void validateSegnalazione4()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente("token3");
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        assertFalse(controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione));
    }

    /** Per il metodo seguente:
     * utente = utente segnalazione != utente itinerario
     * utente non è admin **/
    @Test
    public void validateSegnalazione5()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(utente.getToken());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        assertTrue(controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione));
    }

    /** Per il metodo seguente:
     * utente = utente itinerario = utente segnalazione
     * utente è admin **/
    @Test
    public void validateSegnalazione6()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(utente.getToken());
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(utente.getToken());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (IllegalSegnalazioneException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente itinerario != utente segnalazione
     * utente è admin **/
    @Test
    public void validateSegnalazione7()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(utente.getToken());
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente("token2");
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        assertTrue(controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione));
    }

    /** Per il metodo seguente:
     * utente != utente itinerario = utente segnalazione
     * utente è admin **/
    @Test
    public void validateSegnalazione8()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(itinerario.getFk_utente());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (IllegalSegnalazioneException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }    }

    /** Per il metodo seguente:
     * utente != utente itinerario != utente segnalazione
     * utente è admin **/
    @Test
    public void validateSegnalazione9()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente("token3");
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        assertTrue(controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione));
    }

    /** Per il metodo seguente:
     * utente = utente segnalazione != utente itinerario
     * utente è admin **/
    @Test
    public void validateSegnalazione10()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(utente.getToken());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        assertTrue(controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione));
    }

    /** Per il metodo seguente:
     * utente = utente itinerario = utente segnalazione
     * utente non è admin
     * il suo token è null**/
    @Test
    public void tokenUtenteNull1()
    {
        Utente utente = new Utente();
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(utente.getToken());
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(utente.getToken());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente itinerario != utente segnalazione
     * utente non è admin
     * il suo token è null**/
    @Test
    public void tokenUtenteNull2()
    {
        Utente utente = new Utente();
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(utente.getToken());
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente("token2");
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente != utente itinerario = utente segnalazione
     * utente non è admin
     * il suo token è null**/
    @Test
    public void tokenUtenteNull3()
    {
        Utente utente = new Utente();
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(itinerario.getFk_utente());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente != utente itinerario != utente segnalazione
     * utente non è admin
     * il suo token è null**/
    @Test
    public void tokenUtenteNull4()
    {
        Utente utente = new Utente();
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente("token3");
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente segnalazione != utente itinerario
     * utente non è admin
     * il suo token è null**/
    @Test
    public void tokenUtenteNull5()
    {
        Utente utente = new Utente();
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(utente.getToken());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente itinerario = utente segnalazione
     * utente è admin
     * il suo token è null**/
    @Test
    public void tokenUtenteNull6()
    {
        Utente utente = new Utente();
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(utente.getToken());
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(utente.getToken());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente itinerario != utente segnalazione
     * utente è admin
     * il suo token è null**/
    @Test
    public void tokenUtenteNull7()
    {
        Utente utente = new Utente();
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(utente.getToken());
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente("token2");
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente != utente itinerario = utente segnalazione
     * utente è admin
     * il suo token è null**/
    @Test
    public void tokenUtenteNull8()
    {
        Utente utente = new Utente();
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(itinerario.getFk_utente());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente != utente itinerario != utente segnalazione
     * utente è admin
     * il suo token è null**/
    @Test
    public void tokenUtenteNull9()
    {
        Utente utente = new Utente();
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente("token3");
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente segnalazione != utente itinerario
     * utente è admin
     * il suo token è null**/
    @Test
    public void tokenUtenteNull10()
    {
        Utente utente = new Utente();
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(utente.getToken());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }


    /** Per il metodo seguente:
     * utente != utente itinerario = utente segnalazione
     * utente non è admin
     * fk utente itinerario è null **/
    @Test
    public void tokenUtenteItinerarioNull1()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(itinerario.getFk_utente());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente != utente itinerario != utente segnalazione
     * utente non è admin
     * fk utente itinerario è null **/
    @Test
    public void tokenUtenteItinerarioNull2()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente("token3");
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente segnalazione != utente itinerario
     * utente non è admin
     * fk utente itinerario è null **/
    @Test
    public void tokenUtenteItinerarioNull3()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(null);
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(utente.getToken());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }


    /** Per il metodo seguente:
     * utente != utente itinerario = utente segnalazione
     * utente è admin
     * fk utente itinerario è null **/
    @Test
    public void tokenUtenteItinerarioNull4()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        utente.setAdmin(true);
        Itinerario itinerario = new Itinerario();
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(itinerario.getFk_utente());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente != utente itinerario != utente segnalazione
     * utente è admin
     * fk utente itinerario è null **/
    @Test
    public void tokenUtenteItinerarioNull5()
    {
        Utente utente = new Utente();
        utente.setAdmin(true);
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente("token3");
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente segnalazione != utente itinerario
     * utente è admin
     * fk utente itinerario è null **/
    @Test
    public void tokenUtenteItinerarioNull6()
    {
        Utente utente = new Utente();
        utente.setAdmin(true);
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setFk_utente(utente.getToken());
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente itinerario != utente segnalazione
     * utente non è admin
     * fk utente di segnalazione è null **/
    @Test
    public void fkUtenteSegnalazioneNull1()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(utente.getToken());
        Segnalazione segnalazione = new Segnalazione();
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente != utente itinerario != utente segnalazione
     * utente non è admin
     * fk utente di segnalazione è null **/
    @Test
    public void fkUtenteSegnalazioneNull2()
    {
        Utente utente = new Utente();
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }

    /** Per il metodo seguente:
     * utente = utente itinerario != utente segnalazione
     * utente è admin
     * fk utente di segnalazione è null **/
    @Test
    public void fkUtenteSegnalazioneNull3()
    {
        Utente utente = new Utente();
        utente.setAdmin(true);
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente(utente.getToken());
        Segnalazione segnalazione = new Segnalazione();
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }


    /** Per il metodo seguente:
     * utente != utente itinerario != utente segnalazione
     * utente è admin
     * fk utente di segnalazione è null **/
    @Test
    public void fkUtenteSegnalazioneNull4()
    {
        Utente utente = new Utente();
        utente.setAdmin(true);
        utente.setToken("token1");
        Itinerario itinerario = new Itinerario();
        itinerario.setFk_utente("token2");
        Segnalazione segnalazione = new Segnalazione();
        ControllerVisualizzaItinerario controllerVisualizzaItinerario = new ControllerVisualizzaItinerario(null, itinerario, utente.getToken());
        try{
            controllerVisualizzaItinerario.validateDeleteSegnalazione(utente,itinerario,segnalazione);
            fail();
        }catch (NullPointerException e){
            assertTrue(true);
        }catch (Exception e){
            fail();
        }
    }
}