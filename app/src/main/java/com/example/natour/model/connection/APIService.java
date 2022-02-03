package com.example.natour.model.connection;

import com.example.natour.model.Utente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface APIService
{
    @POST("users")
    Call<List<Utente>> listUtente();

}
