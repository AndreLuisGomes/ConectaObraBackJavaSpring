package com.conectaobra.common.exceptions;

public class RoleInexistenteException extends RuntimeException{

    public String campo;

    public RoleInexistenteException(String message, String campo){
        super(message);
        this.campo = campo;
        ;}
}
