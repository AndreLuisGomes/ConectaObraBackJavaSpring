package com.conectaobra.exceptions;

public class NomeIndisponivelException extends RuntimeException {

    public String campo;

    public NomeIndisponivelException(String message, String campo) {
        super(message);
        this.campo = campo;
    }
}
