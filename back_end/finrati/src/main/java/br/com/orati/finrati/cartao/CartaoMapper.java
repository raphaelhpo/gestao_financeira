package br.com.orati.finrati.cartao;

import org.springframework.stereotype.Component;

import br.com.orati.finrati.cartao.dto.CreateCartaoDto;
import br.com.orati.finrati.cartao.dto.ResponseCartaoDto;
import br.com.orati.finrati.cartao.dto.UpdateCartaoDto;

@Component
public class CartaoMapper {

    public Cartao toEntity(CreateCartaoDto createCartaoDto) {
        Cartao cartao = new Cartao();
        cartao.setIdConta(createCartaoDto.getIdConta());
        cartao.setNome(createCartaoDto.getNome());
        cartao.setLimite(createCartaoDto.getLimite());
        cartao.setDiaFechamento(createCartaoDto.getDiaFechamento());
        cartao.setDiaVencimento(createCartaoDto.getDiaVencimento());
        return cartao;
    }

    public Cartao toEntity(UpdateCartaoDto updateCartaoDto) {
        Cartao cartao = new Cartao();
        cartao.setIdConta(updateCartaoDto.getIdConta());
        cartao.setNome(updateCartaoDto.getNome());
        cartao.setLimite(updateCartaoDto.getLimite());
        cartao.setDiaFechamento(updateCartaoDto.getDiaFechamento());
        cartao.setDiaVencimento(updateCartaoDto.getDiaVencimento());
        return cartao;
    }

    public ResponseCartaoDto toResponse(Cartao cartao) {
        return new ResponseCartaoDto(
                cartao.getId(),
                cartao.getIdConta(),
                cartao.getNome(),
                cartao.getLimite(),
                cartao.getDiaFechamento(),
                cartao.getDiaVencimento(),
                cartao.getCriadoEm(),
                cartao.getAtualizadoEm());
    }
}
