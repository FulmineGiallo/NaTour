package com.example.natour.model;

public class Recensione
{
    private String testo;
    private float valutazione;
    private String utente;
    private String fk_itinerario;

    public String getFk_itinerario()
    {
        return fk_itinerario;
    }

    public void setFk_itinerario(String fk_itinerario)
    {
        this.fk_itinerario = fk_itinerario;
    }

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

    public float getValutazione()
    {
        return valutazione;
    }

    @Override
    public String toString()
    {
        return "Recensione{" +
                "testo='" + testo + '\'' +
                ", valutazione=" + valutazione +
                ", utente='" + utente + '\'' +
                ", fk_itinerario='" + fk_itinerario + '\'' +
                '}';
    }

    public void setValutazione(float valutazione)
    {
        this.valutazione = valutazione;
    }
}
