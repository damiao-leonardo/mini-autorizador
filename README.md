# Mini Autorizador - API REST com Spring Boot

Este projeto é uma API REST desenvolvida com **Java 17** e **Spring Boot**, que simula operações de cartões, como criação, consulta de saldo e transações financeiras.

---

## Tecnologias Utilizadas

- **Java 17**  
  Versão moderna da linguagem Java, trazendo novos recursos e melhorias de performance.

- **Spring Boot**  
  Framework que simplifica o desenvolvimento de aplicações Spring com configurações mínimas.

- **Spring Cloud Sleuth**  
  Adiciona identificadores de rastreamento (traceId e spanId) aos logs, facilitando a depuração e análise de chamadas distribuídas.

- **Spring Security**  
  Proporciona autenticação e autorização seguras para os endpoints da aplicação.

- **Spring Retry**  
  Permite adicionar lógica de retentativa automática para lidar com falhas temporárias e concorrência em transações.

- **Flyway**  
  Gerencia o versionamento e a execução de scripts de migração no banco de dados.

- **MySQL**  
  Banco de dados relacional robusto e amplamente utilizado em ambientes de produção.

- **H2 Database**  
  Banco de dados em memória utilizado durante testes de integração, garantindo rapidez e isolamento.

- **JUnit**  
  Framework de testes unitários para validação de funcionalidades isoladas da aplicação.

- **Mockito**  
  Biblioteca para criação de mocks e simulação de comportamentos em testes.

- **MockMvc**  
  Ferramenta para testes de integração da camada web (controllers) com o Spring MVC.

- **Swagger (OpenAPI)**  
  Gera documentação interativa da API, permitindo explorar e testar os endpoints diretamente no navegador.
---

## Decisões Técnicas

### Banco de Dados
O banco **MySQL** foi escolhido por sua ampla adoção, robustez e familiaridade com bancos relacionais.

###  Padrão de Projeto - Composite
Foi utilizado o padrão **Composite** para estruturar e aplicar múltiplas validações de transações de forma modular, reutilizável e escalável, tratando diferentes regras de forma uniforme.

###  Transações Simultâneas
Utilizamos **Spring Retry** com controle de versão otimista via `@Version` para garantir integridade em atualizações concorrentes de saldo, evitando conflitos de transações simultâneas.

###  Segurança da Aplicação
Com o **Spring Security**, os endpoints são protegidos por autenticação básica, garantindo controle de acesso e segurança na exposição da API.

###  Documentação da API
A documentação foi feita com **Swagger (OpenAPI)**, permitindo:
- Visualização dos endpoints com seus métodos e parâmetros;
- Testes diretamente via interface web;
- Geração automática a partir das anotações do código (`@Operation`, `@ApiResponse`, etc).

Acesse: [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)

###  Padronização de Logs com Sleuth
Com o **Spring Cloud Sleuth**, cada requisição é rastreada por identificadores únicos (`traceId` e `spanId`), facilitando a análise de logs em ambientes distribuídos.

###  Tratamento Global de Erros
Utilizamos `@ControllerAdvice` para capturar exceções lançadas pelos controllers e retornar mensagens padronizadas para o cliente de forma centralizada e desacoplada.

---

##  Endpoints

###  Criar Cartão

| Método | Endpoint                     | Descrição                                                |
|--------|------------------------------|----------------------------------------------------------|
| POST   | `/cartoes`                   | Cria um novo cartão com saldo inicial de R$ 500,00       |

**Body (JSON)**:
```json
{
  "numeroCartao": "6549873025634501",
  "senha": "1234"
}
```

**Curl**:
```bash
curl -X POST http://localhost:8080/cartoes   -H "Content-Type: application/json"   -d '{"numeroCartao":"654987302563459","senha":"1234"}'
```

---

###  Consultar Saldo

| Método | Endpoint                        | Descrição                                 |
|--------|----------------------------------|------------------------------------------|
| GET    | `/cartoes/{numeroCartao}`       | Retorna o saldo disponível do cartão      |

**Curl**:
```bash
curl http://localhost:8080/cartoes/6549873025634501"
```

---

###  Realizar Transação

| Método | Endpoint           | Descrição                                                                                             |
|--------|--------------------|-------------------------------------------------------------------------------------------------------|
| POST   | `/transacoes`      | Efetua uma transação. Retorna erro se o cartão não existir, saldo for insuficiente ou senha inválida. |

**Body (JSON)**:
```json
{
  "numeroCartao": "654987302563459",
  "senhaCartao": "1234",
  "valor": 10.00
}
```

**Curl**:
```bash
curl -X POST http://localhost:8080/transacoes   -H "Content-Type: application/json"   -d '{"numeroCartao":"654987302563459","senhaCartao":"1234","valor":10.00}'
```