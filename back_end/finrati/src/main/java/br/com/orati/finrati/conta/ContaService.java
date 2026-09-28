package br.com.orati.finrati.conta;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.orati.finrati.conta.dto.CreateContaDto;
import br.com.orati.finrati.conta.dto.PatchContaDto;
import br.com.orati.finrati.conta.dto.ResponseContaDto;
import br.com.orati.finrati.conta.dto.UpdateContaDto;
import br.com.orati.finrati.shared.exception.RecursoNaoEncontradoException;

@Service
public class ContaService {
    private final ContaRepository contaRepository;
    private final ContaMapper contaMapper;

    public ContaService(ContaRepository contaRepository, ContaMapper contaMapper) {
        this.contaRepository = contaRepository;
        this.contaMapper = contaMapper;
    }

    public ResponseContaDto criar(CreateContaDto contaDto) {
        Conta contaEncontrada = contaRepository.save(contaMapper.toEntity(contaDto));
        return contaMapper.toResponse(contaEncontrada);
    }

    public List<ResponseContaDto> buscarTodos() {
        List<Conta> contas = contaRepository.findAll();
        return contas.stream()
                .map(conta -> contaMapper.toResponse(conta))
                .toList();
    }

    public ResponseContaDto buscarPorId(UUID id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe conta para o ID indicado."));
        return contaMapper.toResponse(conta);
    }

    public ResponseContaDto atualizarParcial(UUID id, PatchContaDto patchContaDto) {
        Conta contaEncontrada = contaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe conta para o ID indicado."));

        var nomeUpdate = patchContaDto.getNome();
        var tipoContaUpdate = patchContaDto.getTipoConta();

        if (nomeUpdate != null) {
            contaEncontrada.setNome(nomeUpdate);
        }
        if (tipoContaUpdate != null) {
            contaEncontrada.setTipoConta(tipoContaUpdate);
        }

        contaRepository.save(contaEncontrada);

        return contaMapper.toResponse(contaEncontrada);
    }

    public ResponseContaDto atualizarTudo(UUID id, UpdateContaDto updateContaDto) {
        Conta contaEncontrada = contaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe conta para o ID indicado."));

        contaEncontrada.setNome(updateContaDto.getNome());
        contaEncontrada.setTipoConta(updateContaDto.getTipoConta());

        contaRepository.save(contaEncontrada);

        return contaMapper.toResponse(contaEncontrada);
    }

    public void deletar(UUID id) {
        Conta contaLocalizado = contaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe usuario para o ID indicado."));
        contaRepository.delete(contaLocalizado);
    }
}
