package com.conectaobra.exceptions;

public class ContatoClienteEmUso extends RuntimeException {
    public ContatoClienteEmUso(String message) {
        super(message);
    }
}
