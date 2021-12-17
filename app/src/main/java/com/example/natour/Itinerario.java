package com.example.natour;

public class Itinerario {
    String nome;
    int durata;
    boolean accessibilitaDisabili;
    int difficoltà; //va da 1 a 5
    String descrizione;
    String percorsoInizio;
    String percorsoFine;

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
        this.difficoltà = difficoltà;
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
