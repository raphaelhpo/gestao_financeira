package br.com.orati.finrati.cartao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRespository extends JpaRepository<Cartao, UUID> {

}
