# Finrati — Guia de Desenvolvimento

> Documento de referência único. Consulte aqui antes de perguntar — a resposta pra "onde crio o DTO", "qual status HTTP usar", "como nomeio a exception" provavelmente já está nos Padrões Gerais.

---

## Padrões Gerais (valem para TODO módulo, sem exceção)

### Camadas e responsabilidade
| Camada | Faz | Nunca faz |
|---|---|---|
| Entity | Representa a tabela, campos, relacionamentos JPA | Regra de negócio complexa |
| Repository | `JpaRepository`, no máximo `@Query` customizada | Regra de negócio |
| Service | Toda regra de negócio, orquestra outros Services | Lidar com `HttpServletRequest/Response` |
| Controller | Traduz HTTP ↔ Java, chama Service | Acessar Repository direto, regra de negócio |
| Mapper | Converte Entity ↔ DTO | — |

### Regra de dependência entre módulos
Um módulo só acessa o **Repository** dele mesmo. Precisa de dado de outro módulo → chama o **Service** dele, nunca o Repository. Direção sempre: módulo "de cima" conhece o "de baixo", nunca o contrário.

### Estrutura de pastas por módulo (padrão fixo)
```
modulo/
├── Modulo.java              (Entity)
├── ModuloRepository.java
├── ModuloService.java
├── ModuloMapper.java
├── ModuloController.java
├── EnumDoModulo.java        (se houver)
└── dto/
    ├── CreateModuloDto.java
    ├── UpdateModuloDto.java
    ├── PatchModuloDto.java
    └── ResponseModuloDto.java
```

### DTOs — regra por verbo
- **Create**: tudo obrigatório que fizer sentido (`@NotNull`/`@NotBlank`)
- **Update (PUT)**: mesma obrigatoriedade do Create — é substituição total
- **Patch**: nenhum campo obrigatório, sem `@NotNull`/`@NotBlank` (mas pode ter `@Size` — só valida se vier preenchido)
- **Response**: sem validação, só os campos de saída

### Status HTTP por verbo
| Verbo | Status |
|---|---|
| POST (criar) | `201 CREATED` (usar `ResponseEntity.created(location)`) |
| GET | `200 OK` |
| PUT / PATCH | `200 OK` |
| DELETE | `204 NO CONTENT` (sem corpo) |

### Exceptions
- Genéricas em `shared/exception`: `RecursoNaoEncontradoException`, `RegraDeNegocioException`
- A classe é genérica; a **mensagem** (passada no `throw`) é específica: `new RecursoNaoEncontradoException("Conta não encontrada com id: " + id)`
- Só cria exceção específica de módulo se precisar de **comportamento** diferente (status HTTP diferente, dado extra) — nunca só por mensagem diferente
- `GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza o tratamento, devolvendo `ApiError { status, mensagem, timestamp }`

### Timestamps (todo módulo)
```java
@Column(name = "criado_em", nullable = false, updatable = false)
private LocalDateTime criadoEm;

@Column(name = "atualizado_em", nullable = false)
private LocalDateTime atualizadoEm;

@PrePersist
protected void aoCriar() { criadoEm = LocalDateTime.now(); atualizadoEm = LocalDateTime.now(); }

@PreUpdate
protected void aoAtualizar() { atualizadoEm = LocalDateTime.now(); }
```

### Migrations (Flyway)
- Nome: `V{numero}__descricao_com_underscore.sql` (**dois** underscores depois do número)
- Nunca editar uma migration já aplicada — sempre criar uma nova (`V2__...`, `V3__...`)
- `id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid()`
- Enum → `VARCHAR(n) NOT NULL CHECK (campo IN (...))`, nunca `enum(...)` (sintaxe MySQL, não roda no Postgres)
- `@Enumerated(EnumType.STRING)` sempre nos campos enum da Entity

### Atualização parcial (PATCH) sem lib externa
```java
if (dto.getCampo() != null) {
    entidadeExistente.setCampo(dto.getCampo());
}
```
Buscar a entidade existente primeiro (`findById`), nunca criar uma entidade nova do zero no update (senão o Hibernate insere uma linha nova ao invés de atualizar).

---

## Modelo de Dados Final

```
Usuario
  id, nome, email, criadoEm, atualizadoEm

Renda
  id, usuarioId (FK), nome, valor, diaRecebimento, criadoEm, atualizadoEm

Categoria
  id, usuarioId (FK), nome, criadoEm, atualizadoEm

Conta
  id, usuarioId (FK), nomeBanco, tipo (CORRENTE/POUPANCA/DINHEIRO/INVESTIMENTO),
  saldo               (dinheiro acumulado real — cresce a cada fechamento de período)
  saldoInvestimento   (dinheiro guardado pra investir — cresce a cada fechamento)
  criadoEm, atualizadoEm

Cartao
  id, contaId (FK, NOT NULL), nome, limite, diaFechamento, diaVencimento,
  criadoEm, atualizadoEm

Custo
  id, usuarioId (FK), nome,
  tipo (FIXO/VARIAVEL/ALEATORIO/INVESTIMENTO),
  categoriaId (FK, NOT NULL), cartaoId (FK, opcional),
  valor, situacao (PENDENTE/ATUAL/PAGO),
  ativo (boolean, default true — permite "pausar" um custo num mês sem apagar o cadastro,
         ex: não cortou o cabelo esse mês; também usado pra "arquivar" um Variável já quitado
         por completo, mantendo o histórico sem aparecer nas somas ativas),
  dataReferencia (mês/ano a que o custo pertence),
  grupoParcelamentoId (opcional, liga parcelas da mesma compra Variável),
  parcelaAtual, parcelaTotal (opcional, só Variável),
  criadoEm, atualizadoEm

Simulacao (SEM TABELA — cálculo em tempo real, ver Fase 7)

--- Adiado para depois da Fase 10 (Auth) ---
CustoDivisao
  id, custoId (FK), usuarioId (FK), percentual, valorCota

ContaConjunta (ideia registrada, ver Backlog de Features Futuras no final do documento)
```

---

## Roadmap de Fases

| Fase | Módulo | Status |
|---|---|---|
| 0 | Setup, Docker, Flyway | ✅ Feito |
| **1** | **Usuario** | → Próxima |
| 2 | Conta (ajuste: FK real, remover CREDITO, adicionar saldo/saldoInvestimento) | Pendente ajuste |
| 3 | Cartao | A fazer |
| 4 | Categoria | A fazer |
| 5 | Custo (incluindo campo `ativo` e `PeriodoService.fecharPeriodo()`) | A fazer |
| 6 | Renda | A fazer |
| 7 | Simulacao | A fazer |
| 8 | Orcamento | A fazer |
| 9 | Meta | A fazer |
| 10 | Auth (JWT) | Pausado, retomar aqui |
| 11 | CustoDivisao | Depende da 10 |
| 12 | Testes automatizados | — |
| 13 | Frontend | — |
| 14 | Kafka (eventos) | — |
| 15 | Observabilidade | — |
| 16 | Deploy | — |
| 17 | Extrair microsserviço | — |
| 18 | Kubernetes | — |
| 19 | ContaConjunta (contas compartilhadas entre usuários) | Backlog, depende da 10 e 11 |

---

## FASE 1 — Usuario

**Objetivo:** entidade de domínio (ficha cadastral), sem login/Security ainda. Auth entra só na Fase 10, por cima disso.

**Banco:**
```sql
CREATE TABLE usuarios (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);
```

**Pastas:** `usuario/` seguindo o padrão geral. Sem `UserDetails`, sem nada de Security.

**Tasks:**
1. Migration `V1__criar_tabela_usuarios.sql`
2. Entity `Usuario.java`
3. `UsuarioRepository`
4. `UsuarioService` (CRUD completo)
5. `UsuarioMapper`
6. DTOs: `CreateUsuarioDto`, `UpdateUsuarioDto`, `PatchUsuarioDto`, `ResponseUsuarioDto`
7. `UsuarioController` (6 endpoints: POST, GET/{id}, GET all, PUT, PATCH, DELETE)

**Critério de pronto:** CRUD completo funcionando no Swagger.

---

## FASE 2 — Ajustar Conta

**Objetivo:** conectar `Conta` a `Usuario` de verdade (FK), remover `CREDITO` do enum (crédito agora vive em `Cartao`), e adicionar os campos de saldo acumulado.

**Banco:**
```sql
-- V{N}__ajustar_tabela_contas.sql
ALTER TABLE contas
    ADD CONSTRAINT fk_conta_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    ADD COLUMN saldo DECIMAL(15,2) NOT NULL DEFAULT 0,
    ADD COLUMN saldo_investimento DECIMAL(15,2) NOT NULL DEFAULT 0;

-- Se existir alguma constraint de CHECK com 'CREDITO' no tipo_conta, recriar sem esse valor
```

**Tasks:**
1. Nova migration ajustando a FK, o CHECK do `tipo_conta`, e adicionando `saldo`/`saldoInvestimento`
2. Atualizar `TipoConta.java`: remover `CREDITO`
3. Atualizar `Conta.java`: novos campos `saldo`, `saldoInvestimento`
4. Revisar DTOs de `Conta`

**Critério de pronto:** criar um `Usuario`, criar uma `Conta` referenciando o `usuarioId` dele, confirmar que a FK barra um `usuarioId` inexistente.

---

## FASE 3 — Cartao

**Objetivo:** cada `Conta` (banco) pode ter vários cartões, cada um com seu próprio limite.

**Banco:**
```sql
CREATE TABLE cartoes (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    conta_id UUID NOT NULL REFERENCES contas(id),
    nome VARCHAR(100) NOT NULL,
    limite DECIMAL(15,2) NOT NULL,
    dia_fechamento INTEGER,
    dia_vencimento INTEGER,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);
```

**Pastas:** `cartao/` seguindo o padrão. Pensar se `contaId` é `UUID` simples ou `@ManyToOne` pra `Conta`.

**Tasks:** mesmas 7 tasks do padrão CRUD (migration, Entity, Repository, Service, Mapper, DTOs, Controller).

Extra: no `CartaoService.criar`, validar que a `Conta` referenciada existe — chamar `ContaService.buscarPorId(contaId)`, nunca `ContaRepository` direto.

**Critério de pronto:** criar uma Conta, criar 2-3 Cartões vinculados a ela, listar cartões de uma conta específica, editar, excluir.

---

## FASE 4 — Categoria

**Objetivo:** agrupamento livre e reutilizável ("Carro", "Casa") — mais simples que o resto.

**Banco:**
```sql
CREATE TABLE categorias (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    nome VARCHAR(100) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);
```

**Pastas:** `categoria/` — sem enum de tipo (Categoria é só nome livre).

**Tasks:** padrão CRUD (7 tasks).

**Critério de pronto:** CRUD completo no Swagger.

---

## FASE 5 — Custo (o núcleo do sistema)

**Objetivo:** a entidade central — cada linha é um custo, com tipo, categoria, cartão opcional, situação, e agora também controle de ativo/inativo e virada de período.

**Banco:**
```sql
CREATE TABLE custos (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    nome VARCHAR(150) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('FIXO','VARIAVEL','ALEATORIO','INVESTIMENTO')),
    categoria_id UUID NOT NULL REFERENCES categorias(id),
    cartao_id UUID REFERENCES cartoes(id),
    valor DECIMAL(15,2) NOT NULL,
    situacao VARCHAR(20) NOT NULL CHECK (situacao IN ('PENDENTE','ATUAL','PAGO')),
    ativo BOOLEAN NOT NULL DEFAULT true,
    data_referencia DATE NOT NULL,
    grupo_parcelamento_id UUID,
    parcela_atual INTEGER,
    parcela_total INTEGER,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);
```

**Pastas:**
```
custo/
├── Custo.java
├── TipoCusto.java
├── SituacaoCusto.java
├── CustoRepository.java
├── CustoService.java           (CRUD básico + orquestração)
├── ParcelamentoService.java    (gera N linhas de VARIAVEL automaticamente)
├── PeriodoService.java         (fecharPeriodo — ver regra abaixo)
├── CustoMapper.java
├── CustoController.java
└── dto/
    ├── CreateCustoDto.java              (custo simples: FIXO, ALEATORIO, INVESTIMENTO)
    ├── CreateCustoParceladoDto.java     (VARIAVEL: valor total + número de parcelas)
    ├── UpdateCustoDto.java
    ├── PatchCustoDto.java
    └── ResponseCustoDto.java
```

**Tasks:**
1. Migration
2. Enums `TipoCusto`, `SituacaoCusto`
3. Entity `Custo.java` (incluindo `ativo`)
4. `CustoRepository` — método de busca filtrada: `findByUsuarioIdAndDataReferenciaBetween(...)`, variantes por `tipo`/`categoriaId`/`cartaoId`/`ativo`
5. `CustoService`: CRUD de custo simples (FIXO, ALEATORIO, INVESTIMENTO); toda consulta de simulação/listagem padrão filtra `ativo = true`
6. `ParcelamentoService`: recebe valor total + nº de parcelas, calcula valor de cada uma, gera as N `Custo` (tipo VARIAVEL) com `dataReferencia` incrementando mês a mês, mesmo `grupoParcelamentoId`
7. Validações no `CustoService.criar`: `categoriaId` existe (via `CategoriaService`), `cartaoId` existe se informado (via `CartaoService`)
8. Mapper, DTOs, Controller — incluir endpoint específico `POST /custos/parcelados` além do `POST /custos` normal
9. Endpoint de filtro: `GET /custos?tipo=FIXO&mes=10&ano=2026&categoriaId=...&ativo=true`
10. **`PeriodoService.fecharPeriodo(usuarioId)`** — ação explícita (`POST /custos/fechar-periodo`), disparada manualmente pelo usuário ao receber o salário. Regra por tipo:
    - **Fixo**: todo `Custo` com `situacao = PAGO` volta pra `situacao = ATUAL` (pronto pra ser pago de novo no novo ciclo)
    - **Variável**: a parcela do mês que estava `ATUAL` vira `PAGO`; a parcela do próximo mês (que estava `PENDENTE`) vira `ATUAL`. Se essa era a última parcela do `grupoParcelamentoId`, marcar `ativo = false` nela (mantém histórico, some da lista ativa)
    - **Aleatório**: tudo que estava `ATUAL` vira `PAGO` (mantém histórico); não precisa "limpar" nada — um novo ciclo só ganha `Aleatório` novo quando o usuário lançar um gasto novo
    - Campo `ativo = false` também serve para "pausar" um Fixo num mês específico sem apagar o cadastro (ex: não cortou o cabelo esse mês) — nesse caso o usuário marca manualmente, não é automático no fechamento

**Critério de pronto:** criar um custo Fixo, um Aleatório, uma compra parcelada em 6x, disparar `fechar-periodo` e confirmar que as situações mudam conforme a regra.

---

## FASE 6 — Renda

**Objetivo:** entrada de dinheiro esperada, com dia de recebimento (define o período de cálculo).

**Banco:**
```sql
CREATE TABLE rendas (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    nome VARCHAR(100) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    dia_recebimento INTEGER NOT NULL CHECK (dia_recebimento BETWEEN 1 AND 31),
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);
```

**Pastas:** `renda/` — padrão CRUD simples.

**Tasks:** padrão CRUD (7 tasks).

**Critério de pronto:** CRUD completo no Swagger.

---

## FASE 7 — Simulacao

**Objetivo:** o serviço de cálculo central — sem tabela própria, sempre recalculado na hora.

**Pastas:**
```
simulacao/
├── SimulacaoService.java
├── SimulacaoController.java
└── dto/
    └── ResponseSimulacaoDto.java
```

**Conceito de período:** calculado por data, sem persistir nada — do dia de recebimento do salário do mês corrente até o dia de recebimento do mês seguinte. Não existe "fechar período" como estado salvo; é sempre uma janela calculada com base em "hoje" e no `diaRecebimento` da `Renda`.

**Fórmulas (validadas):**
```
salarioPeriodo = soma(Renda ativas do usuário) + saldo acumulado da(s) Conta(s) escolhida(s)
                 (o saldo que sobrou de fechamentos anteriores entra somado aqui)

gastosComprometidos = somaFixos + somaVariaveis     (apenas custos com ativo = true)

orcamentoLivreInicial = salarioPeriodo - gastosComprometidos - investimentoPretendido

excedente = MAX(0, somaAleatorios - orcamentoLivreInicial)

investimentoAtual = MAX(0, investimentoPretendido - excedente)

livreAtual = MAX(0, orcamentoLivreInicial - somaAleatorios)

deficit = MAX(0, excedente - investimentoPretendido)
```

**Tasks:**
1. `SimulacaoService.calcularPeriodo(usuarioId, mesReferencia)`:
   - Busca `Renda` do usuário (via `RendaService`), soma `salarioPeriodo`
   - Soma `saldo` das `Conta`s do usuário (via `ContaService`) ao `salarioPeriodo`
   - Busca `Custo` ativos do usuário filtrado por `dataReferencia` dentro do período (via `CustoService`), separa por `tipo`, soma cada grupo
   - Aplica as fórmulas acima
2. DTO de resposta com todos os campos calculados
3. Controller: `GET /simulacao?mes=10&ano=2026`

**Critério de pronto:** rodar a simulação com dados reais cadastrados e confirmar que os números batem manualmente.

---

## FASE 8 — Orcamento

**Objetivo:** "quanto planejei gastar em cada Categoria" vs. realizado (soma de `Custo` daquela categoria).

**Banco:**
```sql
CREATE TABLE orcamentos (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    categoria_id UUID NOT NULL REFERENCES categorias(id),
    mes INTEGER NOT NULL,
    ano INTEGER NOT NULL,
    valor_planejado DECIMAL(15,2) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);
```

**Tasks:** padrão CRUD + método `getStatus(categoriaId, mes, ano)` que compara planejado vs. soma real de `Custo` daquela categoria/período (via `CustoService`).

**Critério de pronto:** cadastrar orçamento de "Carro" pro mês, lançar custos de Carro, ver comparação planejado x realizado.

---

## FASE 9 — Meta

**Objetivo:** metas de acúmulo ("juntar R$10k até dezembro"), independente de categoria.

**Banco:**
```sql
CREATE TABLE metas (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    nome VARCHAR(150) NOT NULL,
    valor_alvo DECIMAL(15,2) NOT NULL,
    valor_atual DECIMAL(15,2) NOT NULL DEFAULT 0,
    prazo DATE,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT now()
);
```

**Tasks:** padrão CRUD + método de progresso (`valorAtual / valorAlvo * 100`).

**Critério de pronto:** CRUD completo, endpoint de progresso funcionando.

---

## FASE 10 — Auth (retomar)

**Objetivo:** adicionar login/JWT **por cima** do `Usuario` que já existe — sem recriar a entidade.

**Tasks (retomando o que já foi construído antes):**
1. Adicionar campo `senha` em `Usuario` (nova migration)
2. `Usuario implements UserDetails`
3. `JwtService` (já tínhamos feito — revisar se ainda serve)
4. `JwtAuthFilter`
5. `SecurityConfig` — trocar `permitAll()`/`httpBasic()` por validação JWT real
6. `AuthController`: `/auth/register`, `/auth/login`
7. Trocar todo `usuarioId` que hoje vem no corpo do DTO pra vir do token autenticado (`SecurityContextHolder`)

**Critério de pronto:** registrar usuário, logar, receber token, usar token pra acessar endpoints protegidos — endpoints sem token devem ser barrados.

---

## FASE 11 — CustoDivisao

**Objetivo:** dividir um custo entre dois usuários reais do sistema (ex: aluguel 45/55).

**Banco:**
```sql
CREATE TABLE custo_divisoes (
    id UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    custo_id UUID NOT NULL REFERENCES custos(id),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    percentual DECIMAL(5,2) NOT NULL,
    valor_cota DECIMAL(15,2) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT now()
);
```

**Tasks:** a detalhar quando chegar — depende de decisões de UX que ainda não tomamos (quem cria a divisão, como o outro usuário "aceita" a cota, etc).

---

## FASES 12+ (visão geral, detalhar quando chegar)

- **Testes:** JUnit + Mockito (unitário nos Services), Testcontainers (integração com Postgres real)
- **Frontend:** React + TypeScript, consumindo a API módulo por módulo
- **Kafka:** evento `OrcamentoEstouradoEvent` disparando notificação (dentro do monólito primeiro)
- **Observabilidade:** Spring Actuator + Prometheus + Grafana
- **Deploy:** Dockerfile da aplicação, Railway/Render, Vercel pro front
- **Microsserviço:** extrair `notification-service` (reagindo ao Kafka)
- **Kubernetes:** orquestrar monólito + microsserviço localmente (Minikube/Kind)

---

## Backlog de Features Futuras (registradas, não esquecer)

Ideias válidas que surgiram ao longo do desenvolvimento, mas que dependem de peças ainda não construídas (principalmente Auth) ou que decidimos adiar conscientemente pra não desenhar em cima de areia:

1. **`CustoDivisao`** (Fase 11) — divisão percentual de um custo entre dois usuários reais (ex: aluguel 45/55). Depende do Auth (Fase 10) estar pronto.

2. **`ContaConjunta`** — uma "conta compartilhada" entre dois ou mais usuários, que soma as Rendas de todos e permite dividir Custos entre eles, com sua própria Simulação de grupo (não só individual). Ideia: quando dois usuários fecham período e sobra saldo de um custo dividido, esse saldo pode ir pra essa conta conjunta em vez de pra conta individual de cada um — "economizando juntos". Depende de Auth (Fase 10) e provavelmente de `CustoDivisao` (Fase 11) estarem prontos primeiro. Ainda não tem schema desenhado — desenhar só quando chegar nessa fase.

3. **Integração Open Banking** — os campos `tipo`, `saldo` que já existem em `Conta` foram pensados com essa possibilidade futura em mente: capturar transações reais de débito automaticamente de um banco real, não só controlar o lado do crédito. Não é escopo hoje, mas o modelo já deixa espaço pra isso sem precisar de reforma estrutural.

4. **Campos `instituicao` e `objetivo` em `Conta`** — cogitados em algum momento (ex: `objetivo: RESERVA_EMERGENCIA`), mas adiados porque se sobrepõem conceitualmente com o módulo `Meta`. Revisitar só depois que `Meta` estiver madura, pra decidir se ainda fazem sentido ou se ficariam redundantes.

5. **MapStruct** — cogitado como alternativa aos mappers manuais (`if (dto.getCampo() != null)` repetidos no Patch). Adiado porque, pra `Conta`/`Categoria`/`Cartao` (poucos campos), o ganho não compensa o custo de configuração (+ a pegadinha de compatibilidade com Lombok). Reavaliar quando `Custo` (mais campos) estiver em desenvolvimento — pode compensar mais ali.

---

## Como usar este guia no dia a dia

1. Antes de começar uma fase, releia a seção dela aqui
2. Tente sozinho, seguindo as tasks em ordem
3. Se travar em algo que **não está** nos Padrões Gerais nem na fase específica, aí sim me chama
4. Ao terminar uma fase, confirma o "Critério de pronto" antes de marcar como concluída e avançar
