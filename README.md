# PetGuardian API

> **Challenge FIAP - Java Advanced (Spring Boot)**
>
> Plataforma colaborativa para gestao de cuidados de pets, com tarefas prescritas por veterinarios e execucao por cuidadores.

---

## Sobre o Projeto

O **PetGuardian** e uma API REST em Spring Boot para organizar o cuidado diario de pets em cenarios com varios cuidadores.

A plataforma e centrada no **Pet**:
- um pet pode ter varios cuidadores (`usuario_pet`);
- existe um responsavel principal por pet (`respon_princ`);
- veterinarios criam tarefas de cuidado;
- cuidadores concluem tarefas e acumulam pontos;
- atendimentos e tarefas concluidas formam o historico consolidado do pet.

### Gamificacao: Pontos por Conclusao

O sistema incentiva engajamento por meio de pontos:
- cada tarefa possui `pontos_tarefa`;
- ao concluir, o cuidador executor recebe esses pontos;
- os pontos sao calculados por soma das tarefas concluidas do usuario.

### Rede de Cuidado (care circle)

A antiga ideia de familia rigida foi substituida por uma rede dinamica por pet:
- vinculos entre usuarios e pets em `usuario_pet`;
- visao agregada por usuario em `/usuarios/{id}/rede-cuidado`;
- co-cuidadores e pets compartilhados sao calculados a partir desses vinculos.

### Tarefas

As tarefas representam cuidados como alimentar, medicar, passear, curativo etc.

Regras atuais:
- tarefa e criada pelo fluxo de prescricao (sem executor inicial);
- apenas usuario vinculado ao pet pode concluir;
- status controlado por tabela (`PENDENTE`, `CONCLUIDO`, `EXPIRADO`).

### Atendimentos Veterinarios

Cada atendimento esta vinculado a:
- um pet,
- um veterinario,
- um tipo de atendimento,
- um status.

No modelo atual de codigo, **atendimento nao referencia clinica diretamente**.

---

## Arquitetura

```
src/main/java/fiap/com/br/petguardian/
├── config/              # Configuracoes (Swagger, seed, cache)
├── exception/           # Tratamento centralizado de erros
├── validation/          # Validacoes customizadas (CEP, DDD, Enum)
│
├── usuario/             # Usuario (CRUD + paginação)
├── usuariopet/          # Relacao N:N Usuario x Pet
├── pet/                 # Pet (CRUD + paginacao + historico)
│   └── raca/            # Raca do pet
│
├── tarefa/              # Tarefa gamificada (prescricao/conclusao/pontos)
├── status/              # Status de dominio
├── atendimento/         # Atendimento veterinario
│   └── tipoatendimento/ # Tipo de atendimento
│
├── clinica/             # Clinica veterinaria
├── veterinario/         # Veterinario
├── endereco/            # Endereco
│   ├── bairro/
│   ├── cidade/
│   └── estado/
│
└── telefone/            # Telefone
```

Padrao adotado: **Controller -> Service -> Repository**, com DTOs (`Request`/`Response`) por modulo.

---

## Tecnologias Utilizadas

| Tecnologia | Versao | Finalidade |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 4.0.6 | Framework principal |
| Spring Data JPA | - | Persistencia e ORM |
| Spring Validation | - | Bean Validation |
| Spring Cache | - | Cache de consultas |
| Spring Actuator | - | Observabilidade |
| SpringDoc OpenAPI | 3.0.3 | Documentacao Swagger |
| H2 Database | - | Banco em memoria |
| Lombok | - | Reducao de boilerplate |
| Maven | - | Build e dependencias |

---

## Endpoints da API

Todos os endpoints usam DTOs, Bean Validation e documentacao Swagger.

### Usuarios (`/usuarios`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/usuarios` | Listar todos os usuários (paginado) |
| `GET` | `/usuarios/by-nome` | Buscar usuários por nome (paginado, `?nome=`) |
| `GET` | `/usuarios/by-email` | Buscar usuário por e-mail (`?email=`) |
| `GET` | `/usuarios/{id}` | Buscar usuário por ID |
| `GET` | `/usuarios/{id}/rede-cuidado` | Visao agregada da rede de cuidado (Care Circle) |
| `POST` | `/usuarios` | Criar usuário |
| `PUT` | `/usuarios/{id}` | Atualizar usuário |
| `DELETE` | `/usuarios/{id}` | Deletar usuário |

### Pets (`/pets`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/pets` | Listar todos os pets (paginado) |
| `GET` | `/pets/by-nome` | Buscar pets por nome (paginado, `?nome=`) |
| `GET` | `/pets/{id}` | Buscar pet por ID |
| `GET` | `/pets/{id}/historico` | Historico consolidado (atendimentos + tarefas concluidas) |
| `POST` | `/pets` | Criar pet |
| `PUT` | `/pets/{id}` | Atualizar pet |
| `DELETE` | `/pets/{id}` | Deletar pet |
| `POST` | `/pets/{id}/usuarios/{usuarioId}` | Vincular usuario ao pet (`?principal=true/false`) |
| `DELETE` | `/pets/{id}/usuarios/{usuarioId}` | Desvincular usuario do pet |
| `POST` | `/pets/{id}/convidar` | Convidar co-cuidador por ID (`?responsavelPrincipalId=&usuarioConvidadoId=`) |
| `POST` | `/pets/{id}/convidar-email` | Convidar co-cuidador por e-mail (`?responsavelPrincipalId=&email=`) |

### Tarefas (`/tarefas`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/tarefas` | Listar todas as tarefas (paginado) |
| `GET` | `/tarefas/by-usuario` | Listar tarefas pendentes por usuario (paginado, `?usuarioId=`) |
| `GET` | `/tarefas/{id}` | Buscar tarefa por ID |
| `GET` | `/tarefas/by-usuario/{usuarioId}/{id}` | Buscar tarefa por usuario e ID |
| `POST` | `/tarefas` | Criar tarefa |
| `PUT` | `/tarefas/{id}` | Atualizar tarefa |
| `PATCH` | `/tarefas/{id}/concluir` | Concluir tarefa (enviar `concluinteId` no body) |
| `GET` | `/tarefas/by-usuario/pontos` | Pontos totais do cuidador (`?usuarioId=`) |
| `DELETE` | `/tarefas/{id}` | Deletar tarefa |

### Atendimentos (`/atendimentos`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/atendimentos` | Listar todos os atendimentos (paginado) |
| `GET` | `/atendimentos/by-usuario` | Listar atendimentos por usuario (paginado, `?usuarioId=`) |
| `GET` | `/atendimentos/{id}` | Buscar atendimento por ID |
| `POST` | `/atendimentos` | Criar atendimento |
| `PUT` | `/atendimentos/{id}` | Atualizar atendimento |
| `DELETE` | `/atendimentos/{id}` | Deletar atendimento |

### Clinicas (`/clinicas`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/clinicas` | Listar todas as clinicas (paginado) |
| `GET` | `/clinicas/by-nome` | Buscar clinicas por nome (paginado, `?nome=`) |
| `GET` | `/clinicas/{id}` | Buscar clinica por ID |
| `POST` | `/clinicas` | Criar clinica |
| `PUT` | `/clinicas/{id}` | Atualizar clinica |
| `DELETE` | `/clinicas/{id}` | Deletar clinica |

### Veterinarios (`/veterinarios`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/veterinarios` | Listar todos os veterinarios (paginado) |
| `GET` | `/veterinarios/by-nome` | Buscar veterinarios por nome (paginado, `?nome=`) |
| `GET` | `/veterinarios/by-email` | Buscar veterinario por e-mail (`?email=`) |
| `GET` | `/veterinarios/{id}` | Buscar veterinario por ID |
| `POST` | `/veterinarios` | Criar veterinario |
| `PUT` | `/veterinarios/{id}` | Atualizar veterinario |
| `DELETE` | `/veterinarios/{id}` | Deletar veterinario |

### Enderecos (`/enderecos`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/enderecos` | Listar todos os enderecos (paginado) |
| `GET` | `/enderecos/{id}` | Buscar endereco por ID |
| `POST` | `/enderecos` | Criar endereco |
| `PUT` | `/enderecos/{id}` | Atualizar endereco |
| `DELETE` | `/enderecos/{id}` | Deletar endereco |

---

## Como Executar

### Pre-requisitos

- Java 17+
- Maven (ou Maven Wrapper)

### Passos

Linux/Mac:
```bash
./mvnw spring-boot:run
```

Windows:
```bat
mvnw.cmd spring-boot:run
```

### Acessos

| Recurso | URL |
|---|---|
| API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| H2 Console | `http://localhost:8080/h2-console` |
| Actuator | `http://localhost:8080/actuator` |

Configuracao H2:
- JDBC URL: `jdbc:h2:mem:petguardian`
- User: `sa`
- Password: vazio

---

## Estrutura de Pastas

```
Java-Advanced/
├── src/
│   ├── main/
│   │   ├── java/fiap/com/br/petguardian/
│   │   └── resources/application.properties
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── AGENT.md
└── README.md
```

> `documentos/` deve concentrar os artefatos de entrega (diagramas, cronograma e export de testes).

---

## Tratamento de Erros

A API usa handler global e respostas padronizadas.

Formato:
```json
{
  "timestamp": "2026-05-20T22:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Mensagem de erro",
  "path": "/endpoint"
}
```

Tipos tratados:
- validacao de campos (`400`)
- regra de negocio (`400`)
- JSON invalido (`400`)
- integridade de dados (`400`)
- recurso nao encontrado (`404`)
- erro inesperado (`500`)

---

## Integrantes

| Nome | RM | Turma | GitHub | LinkedIn |
|---|---:|---|---|---|
| Enzo Okuizumi | 561432 | 2TDSPG | [EnzoOkuizumiFiap](https://github.com/EnzoOkuizumiFiap) | [Perfil](https://www.linkedin.com/in/enzo-okuizumi-b60292256/) |
| Lucas Barros Gouveia | 566422 | 2TDSPG | [LuzBGouveia](https://github.com/LuzBGouveia) | [Perfil](https://www.linkedin.com/in/lucas-barros-gouveia-09b147355/) |
| Milton Marcelino | 564836 | 2TDSPG | [MiltonMarcelino](https://github.com/MiltonMarcelino) | [Perfil](http://linkedin.com/in/milton-marcelino-250298142) |
| Luna de Carvalho Guimaraes | 562290 | 2TDSPG | [lunaguima](https://github.com/lunaguima) | [Perfil](https://www.linkedin.com/in/luna-m-guimar%C3%A3es-1850ab173/) |
| Gustavo Okada | 563428 | 2TDSPG | [Gdev3356](https://github.com/Gdev3356) | [Perfil](https://www.linkedin.com/in/gustavo-okada-53a3b8359/) |
