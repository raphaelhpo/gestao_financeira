package br.com.orati.finrati.cartao;

import java.util.List;
import java.util.UUID;

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

import br.com.orati.finrati.cartao.dto.ResponseCartaoDto;

@RestController
@RequestMapping("/cartao")
public class CartaoController {
    private final CartaoService cartaoService;

    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    @PostMapping("path")
    public ResponseEntity<ResponseCartaoDto> postMethodName(@RequestBody String entity) {
        // TODO: process POST request
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseCartaoDto>> getMethodName() {
        // TODO: process GET_ALL request
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCartaoDto> getMethodName(@PathVariable UUID id) {
        // TODO: process GET request
        return ResponseEntity.ok().build();
    }

    @PutMapping("path/{id}")
    public ResponseEntity<ResponseCartaoDto> putMethodName(@PathVariable String id, @RequestBody String entity) {
        // TODO: process PUT request

        return ResponseEntity.ok().build();
    }

    @PatchMapping("path/{id}")
    public ResponseEntity<ResponseCartaoDto> patchMethodName(@PathVariable String id, @RequestBody String entity) {
        // TODO: process PATCH request

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteMethodName(@PathVariable UUID ID) {
        // TODO: process DELETE request
    }

}
