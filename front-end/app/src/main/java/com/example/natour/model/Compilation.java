package com.example.natour.model;

import java.util.LinkedList;
import java.util.List;

public class Compilation
{
    private String nome;
    private String descrizione;
    private List<Itinerario> itinerariCompilation = new LinkedList<>();
    private String idUtente;
    private int idCompilation;

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getDescrizione()
    {
        return descrizione;
    }

    public void setDescrizione(String descrizione)
    {
        this.descrizione = descrizione;
    }

    public List<Itinerario> getItinerariCompilation()
    {
        return itinerariCompilation;
    }

    public void setItinerariCompilation(List<Itinerario> itinerariCompilation)
    {
        this.itinerariCompilation = itinerariCompilation;
    }

    public String getIdUtente()
    {
        return idUtente;
    }

    public void setIdUtente(String idUtente)
    {
        this.idUtente = idUtente;
    }

    public int getIdCompilation()
    {
        return idCompilation;
    }

    public void setIdCompilation(int idCompilation)
    {
        this.idCompilation = idCompilation;
    }
}
