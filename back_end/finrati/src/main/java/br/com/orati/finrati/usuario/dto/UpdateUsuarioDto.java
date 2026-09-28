package br.com.orati.finrati.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUsuarioDto {
    @NotBlank(message = "é Obrigatório.")
    @Size(message = "é muito grande.", max = 150)
    private String nome;
    @NotBlank(message = "é obrigatório.")
    @Size(message = "é muito grande.", max = 150)
    @Email(message = "é inválido.")
    private String email;
}
