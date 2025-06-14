# Simulador de Crédito

Projeto Spring Boot para simulação e gerenciamento de propostas de crédito, com cache inteligente, endpoints RESTful e arquitetura modular.

---

## 🚀 Instruções de Setup

### Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL
- H2 para testes)
- (Opcional) Docker para rodar banco local

### Configuração do Banco

1. Crie o banco de dados:
   ```sql
   CREATE DATABASE simulacaocreditodb;
   ```

2. Configure o usuário e senha no arquivo `src/main/resources/application.properties`:
   ```
   spring.datasource.url=jdbc:postgresql://localhost:5432/simulacaocreditodb
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

3. As migrações Flyway serão aplicadas automaticamente ao subir a aplicação.

### Rodando o Projeto

```bash
./mvnw spring-boot:run
```
ou
```bash
mvn spring-boot:run
```

A aplicação estará disponível em:  
`http://localhost:8080`

---

## 📚 Estrutura do Projeto

- **controllers/**: Endpoints REST (Simulação, Taxas, etc)
- **services/**: Regras de negócio, lógica de simulação, cache
- **dtos/**: Objetos de transferência de dados (request/response)
- **modelo/**: Entidades JPA (SimulacaoCredito, Usuario, TaxaPorFaixaEtariaConfiguracao)
- **repositorios/**: Interfaces Spring Data JPA
- **validadores/**: Validações de negócio
- **config/**: Configurações globais (ex: handler de exceções)
- **db/migration/**: Scripts de migração Flyway

### Decisões de Arquitetura

- **Spring Boot**: Framework principal para agilidade e robustez.
- **Spring Data JPA**: Persistência e queries automáticas.
- **Flyway**: Controle de versão do banco de dados.
- **Cache**: Utilização de `@Cacheable` para simulações, agrupando por faixa etária/taxa.
- **Validação**: Bean Validation (JSR-380) para requests.
- **Tratamento de erros**: `@RestControllerAdvice` para respostas padronizadas de erro.

---

## 🛠️ Exemplos de Requisições

### 1. Simular Crédito (sem armazenar)

**Endpoint:**  
`GET /api/v1/simulacoes`

**Body:**
```json
{
  "valorSolicitado": 10000.00,
  "dataNascimento": "1990-01-01",
  "prazoMeses": 12,
  "tipoCalculo": "PADRAO"
}
```

**Resposta:**
```json
{
  "valorTotal": 11000.00,
  "parcelaMensal": 916.67,
  "totalJuros": 1000.00
}
```

---

### 2. Criar Simulação (com armazenamento)

**Endpoint:**  
`POST /api/v1/simulacoes`

**Body:**
```json
{
  "idUsuario": 1,
  "valorSolicitado": 10000.00,
  "dataNascimento": "1990-01-01",
  "prazoMeses": 12,
  "tipoCalculo": "PADRAO"
}
```

**Resposta:**
```json
{
  "id": 123,
  "valorSolicitado": 10000.00,
  "valorTotal": 11000.00,
  "parcelaMensal": 916.67,
  "totalJuros": 1000.00,
  "taxaAnualAplicada": 0.05
}
```

---

### 3. Atualizar taxa anual de uma faixa etária

**Endpoint:**  
`PATCH /api/v1/taxas-faixa-etaria/{id}/taxa-anual`

**Body:**
```json
{
  "taxaAnual": 0.045
}
```

**Resposta:**
```json
{
  "id": 2,
  "idadeMinima": 30,
  "idadeMaxima": 39,
  "taxaAnual": 0.045
}
```

---

## 🧠 Observações

- O cache de simulação é agrupado por valor solicitado, prazo, taxa da faixa etária e tipo de cálculo.
- O tratamento de erros retorna mensagens detalhadas para validação e exceções de negócio.
- O projeto é facilmente extensível para novos tipos de cálculo ou regras de negócio.

---

## 📝 Testes

Para rodar os testes:
```bash
mvn test
```

---

## 📄 Licença

MIT
