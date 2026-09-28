package br.com.orati.finrati.conta.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.orati.finrati.conta.TipoConta;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseContaDto {
    private UUID id;
    private UUID idUsuario;
    private String nome;
    private TipoConta tipoConta;
    private BigDecimal saldo;
    private BigDecimal saldoInvestimento;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
