CREATE TABLE contas (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid (),
    usuario_id UUID,
    nome VARCHAR(255) NOT NULL,
    tipo_conta VARCHAR(20) NOT NULL,
    saldo_inicial DECIMAL(15, 2) NOT NULL CHECK (
        tipo_conta IN (
            'CORRENTE',
            'CREDITO',
            'DINHEIRO',
            'INVESTIMENTO'
        )
    ),
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);