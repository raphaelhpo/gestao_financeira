package br.com.orati.finrati.shared.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException() {
        super();
    }

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}
