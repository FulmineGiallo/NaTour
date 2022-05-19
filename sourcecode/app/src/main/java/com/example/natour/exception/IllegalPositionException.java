package com.example.natour.exception;

public class IllegalPositionException extends RuntimeException
{
    public IllegalPositionException(){
        super();
    }

    public IllegalPositionException(String messaggio){
        super(messaggio);
    }
}
