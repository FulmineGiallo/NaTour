package com.example.natour.model;

public class Segnalazione
{
    private String titolo;
    private String descrizione;
    private String utente;
    private String fk_utente;

    public String getFk_utente()
    {
        return fk_utente;
    }

    public void setFk_utente(String fk_utente)
    {
        this.fk_utente = fk_utente;
    }

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
