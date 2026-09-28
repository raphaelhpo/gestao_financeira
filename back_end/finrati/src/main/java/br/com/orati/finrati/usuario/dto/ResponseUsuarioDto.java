package br.com.orati.finrati.usuario.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUsuarioDto {
    private UUID id;
    private String nome;
    private String email;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
