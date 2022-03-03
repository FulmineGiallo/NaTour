package com.example.natour.model;

public class Recensione
{
    private String testo;
    private int valutazione;
    private String utente;

    public String getUtente()
    {
        return utente;
    }

    public void setUtente(String utente)
    {
        this.utente = utente;
    }

    public String getTesto()
    {
        return testo;
    }

    public void setTesto(String testo)
    {
        this.testo = testo;
    }

    public int getValutazione()
    {
        return valutazione;
    }

    public void setValutazione(int valutazione)
    {
        this.valutazione = valutazione;
    }
}
