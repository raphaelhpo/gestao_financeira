CREATE TABLE cartoes (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid (),
    conta_id UUID NOT NULL REFERENCES contas (id),
    nome VARCHAR(100) NOT NULL,
    limite DECIMAL(15, 2) NOT NULL,
    dia_fechamento INTEGER,
    dia_vencimento INTEGER,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);