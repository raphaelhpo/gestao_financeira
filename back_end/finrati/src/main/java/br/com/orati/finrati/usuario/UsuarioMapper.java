package br.com.orati.finrati.usuario;

import org.springframework.stereotype.Component;

import br.com.orati.finrati.usuario.dto.CreateUsuarioDto;
import br.com.orati.finrati.usuario.dto.ResponseUsuarioDto;

@Component
public class UsuarioMapper {

    public Usuario toEntity(CreateUsuarioDto createUsuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setNome(createUsuarioDto.getNome());
        usuario.setEmail(createUsuarioDto.getEmail());
        return usuario;
    }

    public ResponseUsuarioDto toResponse(Usuario usuario) {
        return new ResponseUsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCriadoEm(),
                usuario.getAtualizadoEm());
    }
}
