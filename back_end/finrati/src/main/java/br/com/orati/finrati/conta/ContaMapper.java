package br.com.orati.finrati.conta;

import org.springframework.stereotype.Component;

import br.com.orati.finrati.conta.dto.CreateContaDto;
import br.com.orati.finrati.conta.dto.ResponseContaDto;
import br.com.orati.finrati.conta.dto.UpdateContaDto;

@Component
public class ContaMapper {

    public Conta toEntity(CreateContaDto createContaDto) {
        Conta conta = new Conta();
        conta.setIdUsuario(createContaDto.getIdUsuario());
        conta.setNome(createContaDto.getNome());
        conta.setTipoConta(createContaDto.getTipoConta());
        return conta;
    }

    public Conta toEntity(UpdateContaDto updateContaDto) {
        Conta conta = new Conta();
        conta.setNome(updateContaDto.getNome());
        conta.setTipoConta(updateContaDto.getTipoConta());
        return conta;
    }

    public ResponseContaDto toResponse(Conta conta) {
        return new ResponseContaDto(
                conta.getId(),
                conta.getIdUsuario(),
                conta.getNome(),
                conta.getTipoConta(),
                conta.getSaldo(),
                conta.getSaldoInvestimento(),
                conta.getCriadoEm(),
                conta.getAtualizadoEm());
    }
}
