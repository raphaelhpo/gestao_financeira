package br.com.orati.finrati.shared.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ResponseException> recursoNaoEncontradoHandler(
            RecursoNaoEncontradoException recursoNaoEncontradoException) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ResponseException response = new ResponseException(
                status.value(),
                recursoNaoEncontradoException.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseException> MethodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        String message = methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                .map(erro -> "O Campo " + erro.getField() + " " + erro.getDefaultMessage())
                .collect(Collectors.joining("; "));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseException response = new ResponseException(
                status.value(),
                message,
                LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }
}
