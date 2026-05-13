# Calorias

API REST em **Spring Boot** (Java 21) para cadastro e consulta de usuários, com suporte a banco **H2** (desenvolvimento local) e **Oracle** (FIAP), migrações com **Flyway** e persistência **JPA**.

## Requisitos

- Java 21  
- Maven 3.9+

## Perfis Spring

| Perfil | Banco | Flyway | `ddl-auto` | Uso |
|--------|--------|--------|------------|-----|
| `local` (padrão) | H2 em memória | Desligado | `create-drop` | Insomnia / testes sem Oracle |
| `oracle` | Oracle FIAP | Ligado | `validate` | Schema deve coincidir com as migrações |

O perfil ativo vem de `SPRING_PROFILES_ACTIVE` ou `--spring.profiles.active`. O padrão está em `src/main/resources/application.properties` (`local`).

### Rodar em modo local (H2)

```bash
mvn spring-boot:run
```

Explícito:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Rodar contra o Oracle (FIAP)

1. Ative o perfil: `SPRING_PROFILES_ACTIVE=oracle` (ou `-Dspring-boot.run.profiles=oracle`).
2. Informe a **senha** do schema (a mesma do SQL Developer), **sem** versionar no Git:
   - **Recomendado:** variável de ambiente `SPRING_DATASOURCE_PASSWORD`.
   - **Alternativa local:** copie `application-oracle-local.properties.example` para `application-oracle-local.properties` na **raiz do projeto** (pasta do `pom.xml`) e defina `spring.datasource.password` (este arquivo está no `.gitignore`).

URL e usuário padrão estão em `src/main/resources/application-oracle.properties`. Podem ser sobrescritos por `SPRING_DATASOURCE_URL` e `SPRING_DATASOURCE_USERNAME` (útil em laboratório com outro RM ou túnel).

**Windows (PowerShell):**

```powershell
$env:SPRING_PROFILES_ACTIVE = "oracle"
$env:SPRING_DATASOURCE_PASSWORD = "sua_senha_aqui"
mvn spring-boot:run
```

**Linux / macOS:**

```bash
export SPRING_PROFILES_ACTIVE=oracle
export SPRING_DATASOURCE_PASSWORD='sua_senha'
mvn spring-boot:run
```

Base URL padrão: `http://localhost:8080` (porta alterável com `server.port`).

---

## Segurança e boas práticas

### Credenciais e repositório

- **Nunca** faça commit de `application-oracle-local.properties`, `.env` com senhas, nem prints de conexão com senha visível.
- O arquivo `application-oracle-local.properties` está listado no `.gitignore`. Antes de um `git add -A`, confira com `git status`.
- Em **CI/CD** ou servidor, use segredos do provedor (GitHub Actions secrets, variáveis do painel da nuvem, etc.), não arquivos no repositório.
- Se uma senha vazou no histórico do Git, **troque a senha no Oracle** e trate o commit (por exemplo com ajuda do instrutor ou `git filter-repo` em repositórios controlados).

### API e exposição

- Esta API **não implementa autenticação nem autorização** (sem login, JWT, etc.). Trate como **apenas laboratório / rede confiável**. Para uso real na internet, coloque atrás de um gateway com HTTPS, autenticação e rate limiting.
- Respostas de **listagem, busca e atualização** de usuário usam `UsuarioExibicaoDTO` (**sem** campo `senha`). O corpo do **POST** e **PUT** ainda envia/recebe senha no JSON de entrada conforme o contrato abaixo — use **HTTPS** em produção e evite logar corpos de requisição.
- O perfil **`local`** usa H2 em memória: não use esse modo com dados reais sensíveis.

### Flyway e banco compartilhado

- Em schema compartilhado (laboratório), coordene migrações com o time; `baseline-on-migrate` ajuda em bases antigas, mas não substitui backup e revisão de scripts.
- Se uma migração falhar no meio (objeto criado à metade), pode ser necessário ajuste manual no banco e alinhamento da tabela `flyway_schema_history` com orientação do instrutor.

---

## Migrações Flyway (Oracle)

Scripts em `src/main/resources/db/migration/`:

| Versão | Conteúdo |
|--------|----------|
| **V1** | Sequência `SEQ_USUARIOS`, tabela `TBL_USUARIOS` |
| **V2** | Sequência `SEQ_ALIMENTOS`, tabela `TBL_ALIMENTOS` |

No perfil `oracle`, `spring.flyway.baseline-on-migrate=true` reduz atrito quando o schema já existia antes do Flyway.

---

## Endpoints (prefixo `/api`)

| Método | Caminho | Descrição |
|--------|---------|-----------|
| GET | `/api/hello` | Retorno HTML de teste |
| GET | `/api/ola` | Retorno HTML de teste |
| POST | `/api/usuarios` | Cadastro de usuário |
| GET | `/api/usuarios` | Lista todos (sem senha) |
| GET | `/api/usuarios/{usuarioId}` | Busca por id (`404` se não existir) |
| PUT | `/api/usuarios` | Atualização (corpo com `usuarioId` obrigatório) |
| DELETE | `/api/usuarios/{usuarioId}` | Exclusão por id |

### Cabeçalhos

Para **POST** e **PUT** com JSON:

```http
Content-Type: application/json
Accept: application/json
```

### Exemplos de corpo (Insomnia, Postman, cURL)

**POST `/api/usuarios`** — cadastro (`usuarioId` pode ser omitido ou `null` para novo registro; o banco gera o id pela sequência).

```json
{
  "usuarioId": null,
  "nome": "Maria Silva",
  "email": "maria@exemplo.com",
  "senha": "SenhaForte123"
}
```

**Resposta** `201 Created` — `UsuarioExibicaoDTO` (sem `senha`):

```json
{
  "usuarioId": 1,
  "nome": "Maria Silva",
  "email": "maria@exemplo.com"
}
```

**GET `/api/usuarios`** — `200 OK`, lista de `UsuarioExibicaoDTO`.

**GET `/api/usuarios/1`** — `200 OK` ou `404` sem corpo.

**PUT `/api/usuarios`** — atualização (todos os campos que forem persistidos devem ir no JSON; `usuarioId` obrigatório).

```json
{
  "usuarioId": 1,
  "nome": "Maria S. Atualizada",
  "email": "maria.novo@exemplo.com",
  "senha": "NovaSenhaSegura456"
}
```

**Resposta** `200 OK` — `UsuarioExibicaoDTO` (sem `senha`).

**DELETE `/api/usuarios/1`** — `204 No Content` em sucesso; erro de não encontrado pode retornar `404` com corpo de erro do Spring.

### cURL (local, perfil padrão)

```bash
curl -s -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Teste\",\"email\":\"teste@exemplo.com\",\"senha\":\"abc123\"}"
```

```bash
curl -s http://localhost:8080/api/usuarios
```

---

## DTOs e modelo (referência)

| Tipo | Uso |
|------|-----|
| `UsuarioCadastroDTO` | Entrada no **POST** `/api/usuarios` |
| `UsuarioExibicaoDTO` | Saída nas listagens, GET por id e **PUT** (sem senha) |
| `AlimentoCadastroDTO` / `AlimentoExibicaoDTO` | Preparados para evolução da API; migração **V2** já cria `TBL_ALIMENTOS` no Oracle |

Campos principais da entidade `Usuario` no **PUT**: `usuarioId`, `nome`, `email`, `senha` (conforme `Usuario.java`).

---

## Build e testes

```bash
mvn clean compile
mvn test
```

## Estrutura de pacotes (`br.com.evelyn.calorias`)

- `controller` — REST (`UsuarioController`, `HelloWorldController`)  
- `service` — regras (`UsuarioService`)  
- `repository` — Spring Data JPA  
- `model` — entidades (`Usuario`, `Alimento`)  
- `dto` — contratos HTTP (`UsuarioCadastroDTO`, `UsuarioExibicaoDTO`, DTOs de alimento)

---

## Manutenção deste README

Ao mudar perfis, Flyway, rotas, variáveis de ambiente ou contratos JSON, atualize este arquivo para o time e para avaliação acadêmica permanecerem alinhados.
