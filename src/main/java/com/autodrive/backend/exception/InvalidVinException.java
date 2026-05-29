package com.autodrive.backend.exception;

public class InvalidVinException extends RuntimeException{
    public InvalidVinException (String message){
        super(message);
    }
}
