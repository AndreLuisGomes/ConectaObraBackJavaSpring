package com.conectaobra.exceptions;

public class NomeClienteEmUsoException extends RuntimeException {
    public NomeClienteEmUsoException(String message) {
        super(message);
    }
}
