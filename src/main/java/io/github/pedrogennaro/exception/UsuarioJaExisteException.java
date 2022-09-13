package io.github.pedrogennaro.exception;

public class UsuarioJaExisteException extends RuntimeException{
    public UsuarioJaExisteException() {
        super("Login já existe");
    }
}
