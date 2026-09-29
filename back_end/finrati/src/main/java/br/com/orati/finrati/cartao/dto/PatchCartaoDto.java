package br.com.orati.finrati.cartao.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchCartaoDto {
    private UUID idConta;
    private String nome;
    private BigDecimal limite;
    private int diaFechamento;
    private int diaVencimento;
}
