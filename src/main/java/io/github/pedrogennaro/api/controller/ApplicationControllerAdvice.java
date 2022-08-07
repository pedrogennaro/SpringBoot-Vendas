package io.github.pedrogennaro.api.controller;

import io.github.pedrogennaro.api.ApiErrors;
import io.github.pedrogennaro.exception.PedidoNaoEncontradoException;
import io.github.pedrogennaro.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handlerRegraNegocioException(RegraNegocioException ex){
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handlerPedidoNaoEncontradoException(PedidoNaoEncontradoException ex){

        return new ApiErrors(ex.getMessage());
    }
}
