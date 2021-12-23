package com.example.natour.model;

import java.util.Date;

public class Utente
{
    private String email;
    private String password;
    private String nome;
    private String cognome;
    private Date dataDiNascita;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
