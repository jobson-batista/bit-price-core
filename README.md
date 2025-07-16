# Bit Price Core - Backend

Projeto desenvolvido com **Spring Boot**, **Java 17** e **PostgreSQL**.

## Requisitos

- Java 17
- PostgreSQL
- Gradle

## Configuração

1. Configure o banco de dados no arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bitprice
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
````

2. Execute a aplicação:

```bash
./gradlew bootRun
```

## Estrutura básica

* `src/main/java`: código-fonte da aplicação
* `src/main/resources`: arquivos de configuração
* `target/` ou `build/`: arquivos gerados (não versionados)

