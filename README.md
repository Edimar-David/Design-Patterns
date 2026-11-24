# Design Patterns

Repositório dedicado ao estudo e à implementação prática dos padrões de projeto (Design Patterns) utilizando Java.

## Pacotes (`com.gerenciador.financeiro`)

| Pacote | Responsabilidade |
|--------|------------------|
| `config` | Configurações do Spring (CORS) |
| `controller` | Endpoints REST; só recebem/devolvem DTOs e delegam ao serviço |
| `dto` | Objetos de entrada/saída da API (com validação) |
| `entity` | Entidades JPA |
| `exception` | Exceções de domínio e tratamento global de erros (`@RestControllerAdvice`) |
| `patterns` | Padrões de projeto exigidos |
| `repository` | Acesso a dados (Spring Data JPA) |
| `service` | Regras de negócio |

## Padrões implementados

| Padrão | Categoria | Descrição |
|---|---|---|
| Singleton | Criacional | Garante uma única instância de uma classe. |
| Decorator | Estrutural | Adiciona funcionalidades a objetos dinamicamente. |
| Strategy | Comportamental | Permite alternar entre diferentes algoritmos. |

## Tecnologias

- Java
- Spring Boot
- PostgreSQL
- Maven

## Configuração

`src/main/resources/application.properties` — valores sobrescritíveis por variáveis de ambiente:
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `CORS_ALLOWED_ORIGINS`.

 ## Executar

```bash
# 1) banco
configure o postgreSQL no propriets

# 2) aplicação
mvn spring-boot:run
```

Servidor em <http://localhost:8080>. Teste: `GET /api/status`.

## Objetivo

Compreender os conceitos dos padrões de projeto e demonstrar suas aplicações práticas por meio de implementações em projetos Java.
