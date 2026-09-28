package br.com.orati.finrati.conta.dto;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.orati.finrati.conta.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateContaDto {
    private UUID idUsuario;

    @NotBlank(message = "é obrigatório")
    @Size(max = 255, message = "é muito grande")
    private String nome;

    @NotNull(message = "é obrigatório")
    private TipoConta tipoConta;
}
