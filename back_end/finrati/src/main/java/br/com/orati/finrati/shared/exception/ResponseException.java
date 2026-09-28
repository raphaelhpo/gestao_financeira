package br.com.orati.finrati.shared.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseException {
    private int status;
    private String mensagem;
    private LocalDateTime timestamp;
}
