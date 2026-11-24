# Backend — Gerenciador de Finanças Pessoais

API REST em **Java 21 + Spring Boot** (Maven) com **PostgreSQL**.
Responsável por todas as regras de negócio e pela persistência.

## Executar

```bash
# 1) banco (opcional, via Docker)
docker compose up -d

# 2) aplicação
mvn spring-boot:run
```

Servidor em <http://localhost:8080>. Teste: `GET /api/status`.

## Configuração

`src/main/resources/application.properties` — valores sobrescritíveis por variáveis de ambiente:
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `CORS_ALLOWED_ORIGINS`.

## Pacotes (`com.gerenciador.financeiro`)

| Pacote | Responsabilidade |
|--------|------------------|
| `config` | Configurações do Spring (CORS) |
| `controller` | Endpoints REST; só recebem/devolvem DTOs e delegam ao serviço |
| `dto` | Objetos de entrada/saída da API (com validação) |
| `entity` | Entidades JPA |
| `exception` | Exceções de domínio e tratamento global de erros (`@RestControllerAdvice`) |
| `patterns` | Padrões de projeto exigidos (a implementar após validação da proposta) |
| `repository` | Acesso a dados (Spring Data JPA) |
| `service` | Regras de negócio |

## Testes

```bash
mvn test
```

Os testes dos padrões de projeto serão adicionados junto com a implementação.
