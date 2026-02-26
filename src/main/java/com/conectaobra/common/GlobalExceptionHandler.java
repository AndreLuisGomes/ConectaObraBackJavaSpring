package com.conectaobra.common;

import com.conectaobra.common.exceptions.NomeIndisponivelException;
import com.conectaobra.common.exceptions.RoleInexistenteException;
import com.conectaobra.common.exceptions.UsuarioSenhaInvalidosException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse MethoArgumentNotValidException(MethodArgumentNotValidException e){
        return new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "O formulário possui campos inválidos!",
                e.getFieldErrors().stream().map(fe -> new ErrorFields(fe.getField(), fe.getDefaultMessage())).toList());
    }

    @ExceptionHandler(RoleInexistenteException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse RoleInexistente(RoleInexistenteException e){
        return new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage(),
                new ArrayList<>());
    }

    @ExceptionHandler(UsuarioSenhaInvalidosException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse UsuarioSenhaInvalidos(UsuarioSenhaInvalidosException e){
        return new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage(),
                new ArrayList<>());
    }

    @ExceptionHandler(NomeIndisponivelException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse NomeIndisponivel(NomeIndisponivelException e){
        return new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage(),
                new ArrayList<>(Collections.singleton(new ErrorFields(e.campo, e.getMessage()))));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse HttpMessageNotReadableException(HttpMessageNotReadableException e){
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                new ArrayList<ErrorFields>()
                );
    }
}