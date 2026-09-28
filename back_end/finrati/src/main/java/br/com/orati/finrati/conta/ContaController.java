package br.com.orati.finrati.conta;

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

import br.com.orati.finrati.conta.dto.CreateContaDto;
import br.com.orati.finrati.conta.dto.PatchContaDto;
import br.com.orati.finrati.conta.dto.ResponseContaDto;
import br.com.orati.finrati.conta.dto.UpdateContaDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contas")
public class ContaController {
    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ResponseContaDto> criarConta(@RequestBody @Valid CreateContaDto createContaDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contaService.criar(createContaDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseContaDto> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(contaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseContaDto>> buscarTodos() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(contaService.buscarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        contaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseContaDto> atualizarContaTotal(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateContaDto updateContaDto) {
        ResponseContaDto responseContaDto = contaService.atualizarTudo(id, updateContaDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseContaDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseContaDto> atualizarContaParcial(
            @PathVariable UUID id,
            @RequestBody @Valid PatchContaDto patchContaDto) {
        ResponseContaDto responseContaDto = contaService.atualizarParcial(id, patchContaDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseContaDto);
    }

}
