# AGENT.md

Ultima atualizacao: 2026-05-21T15:59

## 1) Contexto do Challenge (o que este projeto precisa provar)
Projeto acadêmico FIAP (Java Advanced) com Spring Boot para resolver um problema real com:
- persistência relacional (H2/Oracle),
- modelagem orientada a objetos com JPA,
- regras de negócio além de CRUD,
- API REST madura e documentada,
- validação, tratamento de erros, paginação, ordenação, busca, cache e testes de endpoints.

Este arquivo é o guia de continuidade para próximas IAs.

---

## 2) Essência de produto (north star)
**Pet Guardian** é uma plataforma de cuidado colaborativo centrada no **Pet**.

Princípios de domínio:
1. Um pet pode ter múltiplos cuidadores (`UsuarioPet`).
2. Existe um responsável principal (`responsavelPrincipal=true`) com poder administrativo.
3. Veterinários prescrevem tarefas de cuidado.
4. Cuidadores executam tarefas e pontuam.
5. Histórico clínico do pet = atendimentos + tarefas concluídas.
6. Rede de Cuidado: visão agregada ("família dinâmica") calculada a partir do grafo `UsuarioPet`.

Se uma mudança não melhora colaboração, adesão ao tratamento ou visibilidade do histórico, revisar antes de implementar.

---

## 3) Estado real atual do código (não confiar no README legado)

### 3.1 Stack e arquitetura
- Java 17
- Spring Boot 4.0.6
- Spring Data JPA + H2 (`jdbc:h2:mem:petguardian`)
- OpenAPI/Swagger (`springdoc-openapi`)
- Estrutura padrão: `Controller -> Service -> Repository -> Entity/DTO`
- Regras de negócio concentradas em `Service`
- Tratamento global de erro em `GlobalExceptionHandler`

### 3.2 Config importante
Arquivo: `src/main/resources/application.properties`
- `spring.jpa.hibernate.ddl-auto=create-drop`
- `spring.jpa.show-sql=true`
- `@EnableCaching` ativo na aplicação

### 3.3 Modelo de domínio vigente
- `Pet` <-> `Usuario` via `UsuarioPet` (N:N)
- `UsuarioPet.responsavelPrincipal` define governança do pet
- `Tarefa` -> `Pet` (obrigatório), `Veterinario` (obrigatório), `Usuario` executor (opcional até concluir)
- `Atendimento` -> `Pet` + `Veterinario` (**sem** `Clinica` no Java neste momento)
- `Veterinario` -> `Clinica` opcional

---

## 4) Regras de negócio já implementadas

### 4.1 Tarefas prescritas por veterinário
- Criação de tarefa **sem executor** (`usuarioId` não pode ser enviado na criação).
- Conclusão só pode ser feita por usuário vinculado ao pet (`UsuarioPet`).
- Feed do cuidador traz tarefas pendentes de todos os pets vinculados.
- Soma de pontos por usuário disponível por query agregada.

Arquivos-chave:
- `src/main/java/fiap/com/br/petguardian/tarefa/TarefaService.java`
- `src/main/java/fiap/com/br/petguardian/tarefa/TarefaRepository.java`

### 4.2 Rede de co-cuidadores
- Convite de cuidador exige que quem convida seja responsável principal.
- Convite duplicado é bloqueado.
- Desvincular responsável principal é bloqueado.
- Convite por ID e por e-mail já existe no service.

Arquivos-chave:
- `src/main/java/fiap/com/br/petguardian/pet/PetService.java`
- `src/main/java/fiap/com/br/petguardian/usuariopet/UsuarioPetRepository.java`
- `src/main/java/fiap/com/br/petguardian/usuario/UsuarioRepository.java`

### 4.3 Histórico consolidado do pet
- `PetService.getConsolidatedHistory(petId)` agrega:
  - atendimentos por data desc,
  - tarefas concluídas do pet.

Arquivos-chave:
- `src/main/java/fiap/com/br/petguardian/pet/PetService.java`
- `src/main/java/fiap/com/br/petguardian/pet/dto/PetHistoryResponse.java`
- `src/main/java/fiap/com/br/petguardian/atendimento/AtendimentoRepository.java`

### 4.4 Atendimento sem clínica
- `Atendimento` não referencia `Clinica` atualmente.
- DTOs e service de atendimento já foram ajustados para isso.

Arquivos-chave:
- `src/main/java/fiap/com/br/petguardian/atendimento/Atendimento.java`
- `src/main/java/fiap/com/br/petguardian/atendimento/dto/AtendimentoRequest.java`
- `src/main/java/fiap/com/br/petguardian/atendimento/dto/AtendimentoResponse.java`
- `src/main/java/fiap/com/br/petguardian/atendimento/AtendimentoService.java`

### 4.5 Rede de Cuidado (Care Circle)
- Substitui a antiga entidade rígida `Familia` por uma visão agregada dinâmica.
- Dado um `usuarioId`, percorre o grafo `UsuarioPet` e retorna:
  - Todos os pets vinculados ao usuario (com IDs de tarefas e atendimentos de cada pet).
  - Todos os co-cuidadores desses pets (com quais pets compartilham).
  - Totalizadores: tarefas pendentes, concluídas, atendimentos, pontos acumulados.
- `PetResumo` e `CuidadorResumo` são **inner records** dentro de `RedeCuidadoResponse` (sem DTOs separados).
- Endpoint: `GET /usuarios/{id}/rede-cuidado`.

Arquivos-chave:
- `src/main/java/fiap/com/br/petguardian/usuario/UsuarioService.java` (método `getRedeCuidado`)
- `src/main/java/fiap/com/br/petguardian/usuario/dto/RedeCuidadoResponse.java`
- `src/main/java/fiap/com/br/petguardian/usuario/UsuarioController.java`
- `src/main/java/fiap/com/br/petguardian/usuariopet/UsuarioPetRepository.java` (queries `findAllByUsuarioId`, `findAllByPetIdIn`)

---

## 5) Matriz de aderência aos requisitos do Challenge

Status: `OK` | `PARCIAL` | `PENDENTE`

### 5.1 Funcionais/técnicos principais
- Aplicação Java + Spring Boot: **OK**
- Persistência relacional com JPA: **OK**
- Regras além de CRUD (prescrição, conclusão, permissões): **OK**
- DTOs de request/response: **OK**
- Bean Validation: **OK**
- Tratamento global de exceções: **OK**
- Swagger/OpenAPI: **OK**
- JPQL/Query Methods: **OK**
- Paginação: **OK** (adicionada a todos os domínios principais: Usuario, Pet, Clinica, Veterinario, Atendimento, Tarefa)
- Ordenação: **OK** (via pageable/sort)
- Busca com parâmetros: **OK** (nome em `Pet`, `Usuario`, `Clinica` e `Veterinario`; email em `Usuario` e `Veterinario`; consultas específicas por ID/Usuario em outros domínios)
- Cache: **PARCIAL** (presente em `StatusService` e `TipoAtendimentoService`; cobertura pode ser ampliada)
- Coesao/desacoplamento: **OK** (tags Swagger alinhadas, naming consistente em PT-BR sem acentos)
- Testes de endpoints (prova para professor): **PENDENTE** no repositório atual
 
### 5.2 Entregáveis de avaliação (pontuação)
- Cronograma + responsáveis: **PENDENTE**
- Diagramas (arquitetura, classes, DER coerente): **PENDENTE**
- Implementação de entidades: **OK/PARCIAL** (existem, mas validar coerência final com novo modelo)
- Maturidade REST: **OK** (endpoints de convite, historico consolidado e pontos expostos)
- Gestão de configuração no GitHub: **PENDENTE DE EVIDÊNCIA no projeto**
- Link público do GitHub: **PENDENTE DE EVIDÊNCIA no projeto**
- Evidência de testes (Postman/Insomnia export): **OK** (Insomnia_2026-05-21.yaml atualizado e completo)

---

## 6) Gaps críticos atuais (prioridade alta)
1. `README.md` esta desatualizado e contradiz o dominio atual.
2. ~~`SwaggerConfig` ainda cita tags legadas~~ **CORRIGIDO** — Tags atualizadas para dominios reais.
3. ~~Endpoints novos do service nao expostos~~ **CORRIGIDO** — `GET /pets/{id}/historico`, `POST /pets/{id}/convidar`, `POST /pets/{id}/convidar-email`, `GET /tarefas/pontos` expostos.
4. Falta suite minima de testes de integracao para as regras de negocio.
5. Falta pasta `documentos/` com os artefatos exigidos para correcao academica.
6. Mock data alinhado com regra de negocio (tarefas pendentes sem executor).

---

## 7) Backlog orientado por entrega (próximas IAs)

## P0 (obrigatório para nota)
1. ~~Expor endpoints~~ **FEITO**:
   - `POST /pets/{id}/convidar` e `POST /pets/{id}/convidar-email` — convite de co-cuidador.
   - `GET /pets/{id}/historico` — historico consolidado do pet.
   - `GET /tarefas/pontos?usuarioId=` — consulta de pontos totais do cuidador.
   - `GET /usuarios/{id}/rede-cuidado` — rede de cuidado ("familia dinamica").
2. Criar testes de integração (mínimo):
   - criação de tarefa com executor deve falhar,
   - conclusão por usuário não vinculado deve falhar,
   - convite por não principal deve falhar,
   - tentativa de remover principal deve falhar.
3. Criar pasta `documentos/` com:
   - `cronograma.md`,
   - `responsabilidades-equipe.md`,
   - `arquitetura.png|drawio`,
   - `DER.png|drawio`,
   - `diagrama-classes-entidades.png|drawio`,
   - export Postman/Insomnia (`.json` / `.yaml`) — **FEITO** (Insomnia_2026-05-21.yaml atualizado e completo)
   - `evidencias-testes.md`.

## P1 (forte melhoria de consistência)
1. Atualizar `README.md` para refletir modelo pet-centric.
2. ~~Ajustar `SwaggerConfig` para tags reais atuais~~ **FEITO** — tags atualizadas.
3. Revisar mensagens/encoding para remover caracteres corrompidos.
4. Revisar nomenclatura uniforme PT-BR vs EN em mensagens e docs.

## P2 (refino técnico)
1. Expandir cache para consultas de referência de baixo churn.
2. ~~Aumentar cobertura de paginação/ordenação para outros endpoints listáveis~~ **FEITO** (Clinica, Veterinario, Atendimento, Tarefa paginados).
3. Revisar contratos REST para consistência de query params e paths.

---

## 8) Decisões arquiteturais abertas (não alterar sem alinhamento)
1. **Atendimento x Clínica**:
   - Código atual: atendimento não guarda clínica.
   - Narrativa de negócio às vezes afirma que atendimento ocorre em clínica.
   - Tratar como decisão formal (ADR) antes de reintroduzir FK/campo.
2. **Pontuação**:
   - Atual: calculada por soma de tarefas concluídas (consistente).
   - Alternativa futura: saldo materializado em `Usuario` (exige estratégia transacional e reconciliação).

---

## 9) Checklist de Definition of Done (para qualquer nova regra)
1. Regra implementada no Service.
2. Query/repository alinhado (se necessário).
3. Endpoint documentado no controller + Swagger.
4. DTO de entrada/saída coerente e validado.
5. Erros tratados com resposta consistente.
6. Teste de integração cobrindo caso de sucesso + negação.
7. AGENT.md e README atualizados se a regra alterar comportamento de domínio.

---

## 10) Nota operacional para próximas IAs
- Não reverter mudanças de terceiros.
- Evitar refactor amplo junto com mudança funcional.
- Preservar compatibilidade de endpoints existentes quando possível.
- Priorizar entregáveis que impactam diretamente os critérios de pontuação do professor.
