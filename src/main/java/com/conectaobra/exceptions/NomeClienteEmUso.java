package com.conectaobra.exceptions;

public class NomeClienteEmUso extends RuntimeException {
    public NomeClienteEmUso(String message) {
        super(message);
    }
}
