package com.conectaobra.exceptions;

public class ContatoClienteEmUsoException extends RuntimeException {
    public ContatoClienteEmUsoException(String message) {
        super(message);
    }
}
