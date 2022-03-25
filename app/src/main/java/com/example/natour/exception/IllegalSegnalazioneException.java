package com.example.natour.exception;

public class IllegalSegnalazioneException extends RuntimeException
{
    public IllegalSegnalazioneException(){
        super();
    }

    public IllegalSegnalazioneException(String messaggio){
        super(messaggio);
    }
}
