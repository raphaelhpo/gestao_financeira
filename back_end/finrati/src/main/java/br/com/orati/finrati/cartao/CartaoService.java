package br.com.orati.finrati.cartao;

import org.springframework.stereotype.Service;

@Service
public class CartaoService {
    private final CartaoRespository cartaoRespository;

    public CartaoService(CartaoRespository cartaoRespository) {
        this.cartaoRespository = cartaoRespository;
    }
}
