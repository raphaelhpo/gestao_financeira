package br.com.orati.finrati.usuario;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.orati.finrati.shared.exception.RecursoNaoEncontradoException;
import br.com.orati.finrati.usuario.dto.CreateUsuarioDto;
import br.com.orati.finrati.usuario.dto.PatchUsuarioDto;
import br.com.orati.finrati.usuario.dto.ResponseUsuarioDto;
import br.com.orati.finrati.usuario.dto.UpdateUsuarioDto;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public ResponseUsuarioDto criar(CreateUsuarioDto createUsuarioDto) {
        Usuario novoUsuario = usuarioRepository.save(usuarioMapper.toEntity(createUsuarioDto));
        return usuarioMapper.toResponse(novoUsuario);
    };

    public List<ResponseUsuarioDto> buscarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> usuarioMapper.toResponse(usuario))
                .toList();
    };

    public ResponseUsuarioDto buscarPorId(UUID id) {
        return usuarioMapper.toResponse(
                usuarioRepository.findById(id)
                        .orElseThrow(
                                () -> new RecursoNaoEncontradoException("Não existe usuario para o ID indicado.")));
    }

    public ResponseUsuarioDto atualizarParcial(UUID id, PatchUsuarioDto patchUsuarioDto) {
        Usuario usuarioLocalizado = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe usuario para o ID indicado."));

        String nomeUpdate = patchUsuarioDto.getNome();
        String emailUpdate = patchUsuarioDto.getEmail();

        if (nomeUpdate != null) {
            usuarioLocalizado.setNome(nomeUpdate);
        }
        if (emailUpdate != null) {
            usuarioLocalizado.setEmail(emailUpdate);
        }

        return usuarioMapper.toResponse(usuarioLocalizado);

    };

    public ResponseUsuarioDto atualizarTudo(UUID id, UpdateUsuarioDto updateUsuarioDto) {
        Usuario usuarioLocalizado = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe usuario para o ID indicado."));

        usuarioLocalizado.setNome(updateUsuarioDto.getNome());
        usuarioLocalizado.setEmail(updateUsuarioDto.getEmail());

        ResponseUsuarioDto novoUsuario = usuarioMapper.toResponse(usuarioLocalizado);

        return novoUsuario;

    };

    public void deletar(UUID id) {
        Usuario usuarioLocalizado = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe usuario para o ID indicado."));
        usuarioRepository.delete(usuarioLocalizado);
    };
}
