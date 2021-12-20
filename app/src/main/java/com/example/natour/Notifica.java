package com.example.natour;

public class Notifica {

    public enum TipoNotifica{
        SEGNALAZIONE, //casomai l'utente dovesse ricevere una segnalazione
        MESSAGGIO,
        RECENSIONE,
        SISTEMA //notifiche di sistema
    }

    private TipoNotifica tipo;
    private String titolo;
    private String descrizione;
}
