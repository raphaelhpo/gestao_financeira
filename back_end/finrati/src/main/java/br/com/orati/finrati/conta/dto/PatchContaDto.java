package br.com.orati.finrati.conta.dto;

import java.math.BigDecimal;

import br.com.orati.finrati.conta.TipoConta;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatchContaDto {
    @Size(max = 255, message = "é muito grande")
    private String nome;
    private TipoConta tipoConta;
}
