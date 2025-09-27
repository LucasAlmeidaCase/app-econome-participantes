# 👥 App Econome - Participantes API

API REST em Java 21 com Spring Boot para gerenciamento de **Participantes** (clientes, fornecedores, colaboradores, etc.). Oferece operações CRUD completas com validação, mapeamento DTO ↔ entidade via MapStruct, migrações de banco com Liquibase, documentação OpenAPI/Swagger UI e tratamento centralizado de erros.

---

## 🧰 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.x (Web, Data JPA, Validation)
- MySQL 8
- Liquibase (migrações)
- MapStruct (mapeamento DTO)
- Lombok
- springdoc-openapi (Swagger UI)
- Maven
- Docker & Docker Compose

---

## ✅ Pré-requisitos

- JDK 21
- Maven 3.8+ (ou wrapper `mvnw`)
- Docker + Docker Compose

---

## 🐳 Execução Rápida (Docker / Compose)

### Passo 1: Subir serviços (MySQL + API)

```bash
docker compose up -d --build
```

O healthcheck do MySQL garante que a API só inicia após o banco estar pronto; Liquibase aplica o schema automaticamente.

### Passo 2: Acessar documentação

- Swagger UI: <http://localhost:8081/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8081/v3/api-docs>

### Logs (opcional)

```bash
docker compose logs -f app-econome-participantes
```

### Parar

```bash
docker compose down
```

### Remover volumes (reset de dados)

```bash
docker compose down -v
```

---

## 💻 Execução Local (Opcional Sem Docker do App)

1. Subir apenas o MySQL com Compose (ou usar instância própria):

```bash
docker compose up -d mysql-econome-participantes
```

1. Executar aplicação via Maven:

```bash
./mvnw spring-boot:run
```

1. Acessar Swagger:

- <http://localhost:8081/swagger-ui.html>
- <http://localhost:8081/v3/api-docs>

> Caso altere a porta ou credenciais, ajuste seu `.env` ou variáveis de ambiente antes de iniciar.

---

## 🐳 Serviços Orquestrados (Compose)

| Serviço | Porta Host | Interna | Descrição |
|---------|-----------|---------|-----------|
| mysql-econome-participantes | 3309 | 3306 | MySQL 8 com DB `econome_db_participantes` |
| app-econome-participantes | 8081 | 8081 | API Spring Boot |

> O compose inclui healthcheck no MySQL e `depends_on` com condição `service_healthy`.

## 🔐 Variáveis de Ambiente (.env)

| Variável | Papel | Exemplo |
|----------|-------|---------|
| SPRING_DATASOURCE_URL | JDBC do MySQL | jdbc:mysql://mysql-econome-participantes:3306/econome_db_participantes |
| SPRING_DATASOURCE_USERNAME | Usuário DB | root |
| SPRING_DATASOURCE_PASSWORD | Senha DB | 12345 |
| SPRING_JPA_HIBERNATE_DDL_AUTO | Estratégia DDL | validate |
| SPRING_LIQUIBASE_ENABLED | Ativa Liquibase | true |
| SPRING_PROFILES_ACTIVE | Profile | default |

Valores padrão são resolvidos via `application.yml` + placeholders; sobrescreva conforme necessário.

---

## 🧱 Estrutura do Projeto

```text
app-econome-participantes/
├── src/main/java/com/econome/
│   ├── domain/                     # Entidade Participante (em módulo compartilhado do projeto principal)
│   └── participantes/
│       ├── config/                 # OpenAPI, JPA scan
│       ├── controller/             # REST Controllers (+ advice para erros)
│       ├── dto/                    # DTOs (records) + Mapper (MapStruct)
│       ├── enums/                  # Enums de domínio expostos na API
│       ├── exception/              # Exceções + payload ProblemDetails
│       ├── service/                # Interface de serviço
│       └── service/impl/           # Implementação com regras de unicidade
├── src/main/resources/config/
│   ├── application.yml             # Configurações
│   └── liquibase/                  # Changelogs YAML
├── pom.xml
├── docker-compose.yml
├── Dockerfile
└── README.md
```

---

## 🧠 Modelagem (Resumo)

Participante:

- id (Long)
- codigo (String, único)
- cpfCnpj (String, único; sem formatação)
- nome (String)
- tipoPessoa (enum: FISICA | JURIDICA)
- tipoParticipante (enum: CLIENTE | FORNECEDOR | TRANSPORTADORA | COLABORADOR | ASSISTENCIA_TECNICA | ADMINISTRADORA | CONSULTORIA | CONTABILIDADE | OUTROS)
- dataHoraCadastro (ZonedDateTime – default preenchido no serviço se ausente)

Regras:

- Unicidade de `codigo` e `cpfCnpj` validada em nível de serviço (e constraints no banco via Liquibase).
- Timestamp preenchido automaticamente caso não informado.

---

## 🔄 Endpoints (Resumo)

Base URL: `/api/participantes`

| Método | Caminho | Descrição | Status Sucesso |
|--------|---------|-----------|----------------|
| GET | /api/participantes | Listar todos | 200 |
| GET | /api/participantes/{id} | Buscar por ID | 200 (404 se não encontrado) |
| POST | /api/participantes | Criar participante | 201 (Location header) |
| PUT | /api/participantes/{id} | Atualizar participante | 200 |
| DELETE | /api/participantes/{id} | Remover participante | 204 |

---

## 📄 Exemplo de Payloads

### Criar Participante (POST /api/participantes)

```json
{
  "codigo": "PART-001",
  "cpfCnpj": "12345678901",
  "nome": "Empresa Exemplo LTDA",
  "tipoPessoa": "JURIDICA",
  "tipoParticipante": "FORNECEDOR",
  "dataHoraCadastro": "2025-09-24T10:15:30.000-03:00"
}
```

Resposta (201):

```json
{
  "id": 1,
  "codigo": "PART-001",
  "cpfCnpj": "12345678901",
  "nome": "Empresa Exemplo LTDA",
  "tipoPessoa": "JURIDICA",
  "tipoParticipante": "FORNECEDOR",
  "dataHoraCadastro": "2025-09-24T10:15:30.000-03:00"
}
```

### Erro de validação (400)

```json
{
  "timestamp": "2025-09-24T13:22:11.123Z",
  "status": 400,
  "error": "Requisição inválida",
  "message": "Erros de validação encontrados",
  "path": "/api/participantes",
  "fieldErrors": [
    {"field": "codigo", "message": "codigo é obrigatório"}
  ]
}
```

### Conflito de unicidade (409)

```json
{
  "timestamp": "2025-09-24T13:22:11.123Z",
  "status": 409,
  "error": "Conflito",
  "message": "Valor já utilizado para campo 'codigo': PART-001",
  "path": "/api/participantes",
  "fieldErrors": []
}
```

### Não encontrado (404)

```json
{
  "timestamp": "2025-09-24T13:22:11.123Z",
  "status": 404,
  "error": "Recurso não encontrado",
  "message": "Participante de id=99 não encontrado",
  "path": "/api/participantes/99",
  "fieldErrors": []
}
```

---

## 🧪 Build & Testes

Compilar sem testes:

```bash
mvn -DskipTests clean package
```

Executar testes (quando adicionados):

```bash
mvn test
```

Gerar imagem Docker manualmente (alternativa ao compose):

```bash
docker build -t econome/participantes:latest .
```

---

## 🏗️ Arquitetura & Padrões

- Organização por domínio (controller, service, dto, exception, etc.)
- DTOs (records) isolam a API da entidade JPA
- MapStruct para mapeamentos consistentes e performáticos
- Regras de unicidade encapsuladas no serviço (Single Responsibility)
- Transações declarativas (@Transactional) por método de serviço
- Tratamento de erros centralizado com payload consistente (ProblemDetails)
- OpenAPI/Swagger para contrato vivo
- Liquibase garantindo versionamento de schema

---

## 🚀 Próximos Passos (Sugestões)

- Eventos de domínio para integração (ex.: criação automática de transações)
- Testes unitários e de integração (service/controller)
- Nomear explicitamente constraints únicas no changelog Liquibase
- Campos de auditoria (updated_at) e soft delete opcional
- Filtros/paginação e busca por documento/código

---

> Parte do ecossistema **EconoMe** (Pedidos, Transações, Participantes e Front-end React).

---

## 👤 Autor

- Desenvolvido por **Lucas Almeida**.
