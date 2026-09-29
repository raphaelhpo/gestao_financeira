package br.com.orati.finrati.conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contas")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "usuario_id", nullable = true)
    private UUID idUsuario;
    @Column(name = "nome")
    private String nome;
    @Column(name = "tipo_conta")
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Column(name = "saldo_investimento")
    private BigDecimal saldoInvestimento;
    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void aoCriar() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void aoAtualizar() {
        this.atualizadoEm = LocalDateTime.now();
    }

}
