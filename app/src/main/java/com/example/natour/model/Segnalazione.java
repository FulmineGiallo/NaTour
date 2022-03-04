package com.example.natour.model;

public class Segnalazione
{
    private String titolo;
    private String descrizione;
    private String utente;


    public String getUtente()
    {
        return utente;
    }

    public void setUtente(String utente)
    {
        this.utente = utente;
    }

    public String getTitolo()
    {
        return titolo;
    }

    public void setTitolo(String titolo)
    {
        this.titolo = titolo;
    }

    public String getDescrizione()
    {
        return descrizione;
    }

    public void setDescrizione(String descrizione)
    {
        this.descrizione = descrizione;
    }

}
