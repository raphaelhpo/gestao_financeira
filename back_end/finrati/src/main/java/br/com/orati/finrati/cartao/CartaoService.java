package br.com.orati.finrati.cartao;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.orati.finrati.cartao.dto.CreateCartaoDto;
import br.com.orati.finrati.cartao.dto.PatchCartaoDto;
import br.com.orati.finrati.cartao.dto.ResponseCartaoDto;
import br.com.orati.finrati.cartao.dto.UpdateCartaoDto;
import br.com.orati.finrati.conta.ContaRepository;
import br.com.orati.finrati.shared.exception.RecursoNaoEncontradoException;

@Service
public class CartaoService {
    private final CartaoMapper cartaoMapper;
    private final CartaoRespository cartaoRespository;

    public CartaoService(CartaoRespository cartaoRespository,
            CartaoMapper cartaoMapper,
            ContaRepository contaRepository) {
        this.cartaoRespository = cartaoRespository;
        this.cartaoMapper = cartaoMapper;
    }

    public ResponseCartaoDto criar(CreateCartaoDto createCartaoDto) {
        Cartao novoCartao = cartaoRespository.save(cartaoMapper.toEntity(createCartaoDto));
        return cartaoMapper.toResponse(novoCartao);
    }

    public List<ResponseCartaoDto> buscarTodos() {
        return cartaoRespository.findAll().stream()
                .map(cartao -> cartaoMapper.toResponse(cartao)).toList();
    }

    public ResponseCartaoDto buscarPorId(UUID id) {
        Cartao cartao = cartaoRespository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não existe cartão para o ID indicado."));
        return cartaoMapper.toResponse(cartao);
    }

    public ResponseCartaoDto atualizarParcial(PatchCartaoDto patchCartaoDto, UUID id) {
        Cartao cartaoEncontrado = cartaoRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não existe cartão para o ID indicado."));

        UUID updateIdConta = patchCartaoDto.getIdConta();
        String updateNome = patchCartaoDto.getNome();
        BigDecimal updateLimite = patchCartaoDto.getLimite();
        Integer updateDiaVencimento = patchCartaoDto.getDiaVencimento();
        Integer updateDiaFechamento = patchCartaoDto.getDiaFechamento();

        if (updateIdConta != null) {
            cartaoEncontrado.setIdConta(updateIdConta);
        }
        if (updateNome != null) {
            cartaoEncontrado.setNome(updateNome);
        }
        if (updateLimite != null) {
            cartaoEncontrado.setLimite(updateLimite);
        }
        if (updateDiaVencimento != null) {
            cartaoEncontrado.setDiaVencimento(updateDiaVencimento);
        }
        if (updateDiaFechamento != null) {
            cartaoEncontrado.setDiaFechamento(updateDiaFechamento);
        }

        Cartao cartaoAtualizado = cartaoRespository.save(cartaoEncontrado);

        return cartaoMapper.toResponse(cartaoAtualizado);
    }

    public ResponseCartaoDto atualizarTudo(UpdateCartaoDto updateCartaoDto, UUID id) {
        Cartao cartaoEncontrado = cartaoRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não existe cartão para o ID indicado."));

        cartaoEncontrado.setIdConta(updateCartaoDto.getIdConta());
        cartaoEncontrado.setNome(updateCartaoDto.getNome());
        cartaoEncontrado.setLimite(updateCartaoDto.getLimite());
        cartaoEncontrado.setDiaVencimento(updateCartaoDto.getDiaVencimento());
        cartaoEncontrado.setDiaFechamento(updateCartaoDto.getDiaFechamento());

        Cartao cartaoAtualizado = cartaoRespository.save(cartaoEncontrado);

        return cartaoMapper.toResponse(cartaoAtualizado);

    }

    public void deletar(UUID id) {
        Cartao cartaoEncontrado = cartaoRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não existe cartão para o ID indicado."));

        cartaoRespository.delete(cartaoEncontrado);
    }
}
