package com.example.natour.model;

public class Itinerario {
    private String nome;
    private int durata;
    private boolean accessibilitaDisabili;
    private int difficoltà; //va da 1 a 5
    private String descrizione;
    private String percorsoInizio;
    private String percorsoFine;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public boolean isAccessibilitaDisabili() {
        return accessibilitaDisabili;
    }

    public void setAccessibilitaDisabili(boolean accessibilitaDisabili) {
        this.accessibilitaDisabili = accessibilitaDisabili;
    }

    public int getDifficoltà() {
        return difficoltà;
    }

    public void setDifficoltà(int difficoltà) {
        if(difficoltà <= 5 || difficoltà >= 1)
            this.difficoltà = difficoltà;
        else if(difficoltà > 5) this.difficoltà = 5;
        else this.difficoltà = 1;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPercorsoInizio() {
        return percorsoInizio;
    }

    public void setPercorsoInizio(String percorsoInizio) {
        this.percorsoInizio = percorsoInizio;
    }

    public String getPercorsoFine() {
        return percorsoFine;
    }

    public void setPercorsoFine(String percorsoFine) {
        this.percorsoFine = percorsoFine;
    }

}
