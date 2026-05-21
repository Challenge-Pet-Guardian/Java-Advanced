# 🐾 PetGuardian API

> **Challenge FIAP — Java Advanced (Spring Boot)**
>
> Plataforma colaborativa de gerenciamento de cuidados de pets com mecânica de gamificação baseada em Day Streak.

---

## 📖 Sobre o Projeto

O **PetGuardian** é uma API REST desenvolvida com **Spring Boot** que resolve o problema da falta de organização nos cuidados diários de animais domésticos em lares com múltiplos responsáveis.

A plataforma é um **sistema colaborativo de gerenciamento de cuidados de pets com gamificação baseada em Day Streak**, semelhante ao **Duolingo**, permitindo que **usuários de uma mesma família** cuidem de seus pets em conjunto.

### 🎮 Gamificação: Day Streak + Pontuação + Ranking

O sistema incentiva o engajamento nos cuidados do pet através de três mecânicas:

- **Pontos por tarefa**: O criador da tarefa atribui uma pontuação a ela, encorajando os membros da família a concluí-la. Quem conclui a tarefa recebe os pontos
- **Day Streak**: O sistema rastreia a sequência de dias consecutivos em que a família completou tarefas — quanto mais dias seguidos, maior o streak
- **Ranking familiar**: Os pontos acumulados por cada membro permitem montar um ranking dentro da família, incentivando uma competição saudável entre os responsáveis

### 🏠 Modelo Familiar

- Cada **usuário** pertence a uma **família**
- Os **pets** são associados à família através da relação `usuario_pet` (N:N)
- Múltiplos usuários podem cuidar do mesmo pet
- Um **responsável principal** pode ser definido para cada pet

### ✅ Tarefas

As tarefas representam atividades como alimentar, dar banho, passear, administrar remédio ou levar ao veterinário. Qualquer membro da família pode **criar** uma tarefa e atribuir uma **pontuação** a ela. Qualquer outro membro também pode **concluí-la**, porém cada tarefa só pode ser concluída **uma única vez**. As tarefas possuem status (Pendente, Concluída, Expirada) controlados em tabela própria.

### 🏥 Atendimentos Veterinários

O sistema registra atendimentos como consultas, vacinas, cirurgias, banhos e outros procedimentos. Cada atendimento está vinculado a um pet, uma veterinária, um tipo de atendimento e um status.

---

## 🏗️ Arquitetura

```
src/main/java/fiap/com/br/petguardian/
├── config/              # Configurações (Swagger, Cache)
├── exception/           # Tratamento centralizado de erros
├── validation/          # Validações customizadas (CEP, DDD, Enum)
│
├── familia/             # Entidade Família (CRUD + Cache)
├── usuario/             # Entidade Usuário (CRUD + Paginação)
├── usuariopet/          # Relação N:N entre Usuário e Pet
├── pet/                 # Entidade Pet (CRUD + Paginação)
│   └── raca/            # Entidade Raça do Pet
│
├── tarefa/              # Entidade Tarefa (CRUD + Conclusão)
├── status/              # Entidade Status (Pendente/Concluída/Expirada)
├── sequencia/           # Entidade Sequência (Day Streak)
│
├── veterinaria/         # Entidade Veterinária (CRUD)
├── atendimento/         # Entidade Atendimento (CRUD)
│   └── tipoatendimento/ # Tipo de Atendimento (Consulta/Vacina/etc.)
│
├── endereco/            # Entidade Endereço (integração ViaCEP)
│   ├── bairro/          # Entidade Bairro
│   ├── cidade/          # Entidade Cidade
│   └── estado/          # Entidade Estado
│
└── telefone/            # Entidade Telefone
```

Cada pacote segue o padrão **Controller → Service → Repository**, com DTOs separados em subpacotes `dto/` contendo Records Java de `Request` e `Response`.

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|---|---|---|
| **Java** | 17 | Linguagem principal |
| **Spring Boot** | 4.0.6 | Framework principal |
| **Spring Data JPA** | - | Persistência e ORM |
| **Spring Validation** | - | Bean Validation |
| **Spring Cache** | - | Cache de requisições |
| **Spring Actuator** | - | Monitoramento |
| **SpringDoc OpenAPI** | 3.0.3 | Documentação Swagger |
| **H2 Database** | - | Banco de dados em memória |
| **Lombok** | - | Redução de boilerplate |
| **Maven** | - | Gerenciamento de dependências |

---

---

## 🔗 Endpoints da API

Todos os endpoints utilizam **DTOs** (Records Java) para Request/Response, possuem **Bean Validation** nos campos de entrada, e estão documentados via **Swagger** (`@Tag` + `@Operation`). Os endpoints de listagem de Pets e Usuários suportam **paginação**, **ordenação** (`?sort=nome,desc`) e **busca por parâmetros** (`?nome=...`).

### 👥 Usuários (`/usuarios`)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/usuarios` | Listar todos (paginado, com filtro por `?nome=`) |
| `GET` | `/usuarios/{id}` | Buscar por ID |
| `POST` | `/usuarios` | Criar usuário |
| `PUT` | `/usuarios/{id}` | Atualizar usuário |
| `DELETE` | `/usuarios/{id}` | Deletar usuário |

### 🏠 Famílias (`/familias`)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/familias` | Listar todas (com cache) |
| `GET` | `/familias/{id}` | Buscar por ID (com cache) |
| `POST` | `/familias` | Criar família |
| `PUT` | `/familias/{id}` | Atualizar família |
| `DELETE` | `/familias/{id}` | Deletar família |

### 🐶 Pets (`/pets`)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/pets` | Listar todos (paginado, com filtro por `?nome=`) |
| `GET` | `/pets/{id}` | Buscar por ID |
| `POST` | `/pets` | Criar pet |
| `PUT` | `/pets/{id}` | Atualizar pet |
| `DELETE` | `/pets/{id}` | Deletar pet |
| `POST` | `/pets/{id}/usuarios/{usuarioId}` | Vincular um usuário a um pet (N:N) |
| `DELETE` | `/pets/{id}/usuarios/{usuarioId}` | Desvincular um usuário de um pet (N:N) |

### ✅ Tarefas (`/tarefas`)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/tarefas?familiaId={id}` | Listar tarefas da família |
| `GET` | `/tarefas/{id}` | Buscar por ID |
| `GET` | `/tarefas/familias/{familiaId}/{id}` | Buscar por família e ID |
| `POST` | `/tarefas` | Criar tarefa |
| `PUT` | `/tarefas/{id}` | Atualizar tarefa |
| `PATCH` | `/tarefas/{id}/concluir` | Concluir tarefa |
| `DELETE` | `/tarefas/{id}` | Deletar tarefa |

### 🏥 Atendimentos (`/atendimentos`)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/atendimentos?familiaId={id}` | Listar atendimentos da família |
| `GET` | `/atendimentos/{id}` | Buscar por ID |
| `POST` | `/atendimentos` | Criar atendimento |
| `PUT` | `/atendimentos/{id}` | Atualizar atendimento |
| `DELETE` | `/atendimentos/{id}` | Deletar atendimento |

### 🏪 Veterinárias (`/veterinarias`)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/veterinarias` | Listar todas |
| `GET` | `/veterinarias/{id}` | Buscar por ID |
| `POST` | `/veterinarias` | Criar veterinária |
| `PUT` | `/veterinarias/{id}` | Atualizar veterinária |
| `DELETE` | `/veterinarias/{id}` | Deletar veterinária |

### 📍 Endereços (`/enderecos`)

Endereços são resolvidos automaticamente via integração com a API do **ViaCEP**. Ao enviar apenas o CEP e o número, o sistema busca e normaliza o endereço completo (rua, bairro, cidade, estado).

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/enderecos` | Listar todos |
| `GET` | `/enderecos/{id}` | Buscar por ID |
| `POST` | `/enderecos` | Criar endereço (resolve via ViaCEP) |
| `PUT` | `/enderecos/{id}` | Atualizar endereço |
| `DELETE` | `/enderecos/{id}` | Deletar endereço |

### 🔥 Sequências / Day Streak (`/sequencias`)

Controla o sistema de streak da família. O streak é atualizado automaticamente com base nas tarefas concluídas.

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/sequencias/familia/{familiaId}` | Consultar streak da família |
| `PUT` | `/sequencias/familia/{familiaId}` | Atualizar streak da família |

---

## ▶️ Como Executar

### Pré-requisitos

- **Java 17** ou superior instalado
- **Maven** (ou utilize o wrapper `mvnw` incluído)

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/SEU_USUARIO/Java-Advanced.git

# 2. Acesse o diretório
cd Java-Advanced

# 3. Execute a aplicação
./mvnw spring-boot:run
```

### Acessos

| Recurso | URL |
|---|---|
| **API** | `http://localhost:8080` |
| **Swagger UI** | `http://localhost:8080/swagger-ui.html` |
| **H2 Console** | `http://localhost:8080/h2-console` |
| **Actuator** | `http://localhost:8080/actuator` |

> **Configurações do H2:** JDBC URL: `jdbc:h2:mem:petguardian` | User: `sa` | Password: *(vazio)*

---

## 📂 Estrutura de Pastas

```
Java-Advanced/
├── src/
│   └── main/
│       ├── java/fiap/com/br/petguardian/
│       │   ├── PetGuardianApplication.java      # Classe principal (@EnableCaching)
│       │   ├── config/                          # SwaggerConfig, DataMockConfig
│       │   ├── exception/                       # GlobalExceptionHandler, ResourceNotFoundException
│       │   ├── validation/                      # @CepValidation, @DddValidation, @EnumValidation
│       │   │
│       │   ├── familia/                         # Familia
│       │   ├── usuario/                         # Usuario
│       │   ├── usuariopet/                      # UsuarioPet - Relação N:N
│       │   ├── pet/                             # Pet
│       │   │   └── raca/                        # Raca
│       │   ├── tarefa/                          # Tarefa
│       │   ├── status/                          # Status
│       │   ├── sequencia/                       # Sequencia
│       │   ├── veterinaria/                     # Veterinaria
│       │   ├── atendimento/                     # Atendimento
│       │   │   └── tipoatendimento/             # TipoAtendimento
│       │   ├── endereco/                        # Endereco
│       │   │   ├── bairro/                      # Bairro
│       │   │   ├── cidade/                      # Cidade
│       │   │   └── estado/                      # Estado
│       │   └── telefone/                        # Telefone
│       │
│       └── resources/
│           └── application.properties           # Configurações (H2, Swagger, Cache, Erros)
├── documentos/                                  # Diagramas, cronograma, exportações Postman
├── pom.xml                                      # Dependências Maven
├── mvnw / mvnw.cmd                              # Maven Wrapper
└── README.md                                    # Este arquivo
```

---

## 🔒 Tratamento de Erros

A API nunca expõe stack traces ou informações internas do servidor. Todas as respostas de erro seguem o formato padronizado:

```json
{
  "timestamp": "2026-05-20T22:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Mensagem amigável para o usuário",
  "path": "/endpoint"
}
```

### Erros tratados:

| Tipo | Status | Descrição |
|---|---|---|
| Validação de campos | `400` | Campos inválidos no body da requisição |
| Regra de negócio | `400` | Violação de regras do domínio |
| JSON malformado | `400` | Tipos de dados incorretos no payload |
| Integridade de dados | `400` | Violação de constraints do banco |
| Recurso não encontrado | `404` | Entidade não existe no banco |
| Erro interno | `500` | Fallback seguro sem exposição de detalhes |

---

## Integrantes

<table>
<tr>
<th>Nome</th>
<th>RM</th>
<th>Turma</th>
<th>GitHub</th>
<th>LinkedIn</th>
</tr>

<tr>
<td>Enzo Okuizumi</td>
<td>561432</td>
<td>2TDSPG</td>
<td><a href="https://github.com/EnzoOkuizumiFiap">EnzoOkuizumiFiap</a></td>
<td><a href="https://www.linkedin.com/in/enzo-okuizumi-b60292256/">Enzo Okuizumi</a></td>
</tr>

<tr>
<td>Lucas Barros Gouveia</td>
<td>566422</td>
<td>2TDSPG</td>
<td><a href="https://github.com/LuzBGouveia">LuzBGouveia</a></td>
<td><a href="https://www.linkedin.com/in/lucas-barros-gouveia-09b147355/">Lucas Barros Gouveia</a></td>
</tr>

<tr>
<td>Milton Marcelino</td>
<td>564836</td>
<td>2TDSPG</td>
<td><a href="https://github.com/MiltonMarcelino">MiltonMarcelino</a></td>
<td><a href="http://linkedin.com/in/milton-marcelino-250298142">Milton Marcelino</a></td>
</tr>

<tr>
<td>Luna de Carvalho Guimarães</td>
<td>562290</td>
<td>2TDSPG</td>
<td><a href="https://github.com/lunaguima">lunaguima</a></td>
<td><a href="https://www.linkedin.com/in/luna-m-guimar%C3%A3es-1850ab173/">Luna M. Guimarães</a></td>
</tr>

<tr>
<td>Gustavo Okada</td>
<td>563428</td>
<td>2TDSPG</td>
<td><a href="https://github.com/Gdev3356">GustavoOkada7268</a></td>
<td><a href="https://www.linkedin.com/in/gustavo-okada-53a3b8359/">Gustavo Okada</a></td>
</tr>

</table>