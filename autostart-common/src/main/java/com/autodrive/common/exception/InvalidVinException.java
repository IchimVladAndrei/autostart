package com.autodrive.common.exception;

public class InvalidVinException extends RuntimeException{
    public InvalidVinException (String message){
        super(message);
    }
}
