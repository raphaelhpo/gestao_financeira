package br.com.orati.finrati.usuario;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.orati.finrati.usuario.dto.CreateUsuarioDto;
import br.com.orati.finrati.usuario.dto.PatchUsuarioDto;
import br.com.orati.finrati.usuario.dto.ResponseUsuarioDto;
import br.com.orati.finrati.usuario.dto.UpdateUsuarioDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<ResponseUsuarioDto> criarUsuario(@RequestBody @Valid CreateUsuarioDto createUsuarioDto) {
        ResponseUsuarioDto responseUsuario = usuarioService.criar(createUsuarioDto);
        URI location = URI.create("/usuario/" + responseUsuario.getId());
        return ResponseEntity.created(location).body(responseUsuario);
    }

    @GetMapping
    public ResponseEntity<List<ResponseUsuarioDto>> buscarTodosUsuarios() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> buscarUsuarioPorId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.buscarPorId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> atualizarContaParcial(
            @PathVariable UUID id,
            @RequestBody @Valid PatchUsuarioDto patchUsuarioDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.atualizarParcial(id, patchUsuarioDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> atualizarContaTotal(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUsuarioDto updateUsuarioDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.atualizarTudo(id, updateUsuarioDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) {
        usuarioService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
