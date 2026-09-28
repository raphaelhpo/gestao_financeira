ALTER TABLE contas (
    ADD CONSTRAINT fk_conta_usuario FOREIGN KEY (usuario_id) usuarios (id),
    ADD COLUMN saldo DECIMAL(15, 2) NOT NULL DEFAULT 0,
    ADD COLUMN saldo_investimento DECIMAL(15, 2) NOT NULL DEFAULT 0
)