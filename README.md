# Mini Autorizador - API REST com Spring Boot

Este projeto � uma API REST desenvolvida com **Java 17** e **Spring Boot**, que simula opera��es de cart�es, como cria��o, consulta de saldo e transa��es financeiras.

---

## Tecnologias Utilizadas

- **Java 17**  
  Vers�o moderna da linguagem Java, trazendo novos recursos e melhorias de performance.

- **Spring Boot**  
  Framework que simplifica o desenvolvimento de aplica��es Spring com configura��es m�nimas.

- **Spring Cloud Sleuth**  
  Adiciona identificadores de rastreamento (traceId e spanId) aos logs, facilitando a depura��o e an�lise de chamadas distribu�das.

- **Spring Security**  
  Proporciona autentica��o e autoriza��o seguras para os endpoints da aplica��o.

- **Spring Retry**  
  Permite adicionar l�gica de retentativa autom�tica para lidar com falhas tempor�rias e concorr�ncia em transa��es.

- **Flyway**  
  Gerencia o versionamento e a execu��o de scripts de migra��o no banco de dados.

- **MySQL**  
  Banco de dados relacional robusto e amplamente utilizado em ambientes de produ��o.

- **H2 Database**  
  Banco de dados em mem�ria utilizado durante testes de integra��o, garantindo rapidez e isolamento.

- **JUnit**  
  Framework de testes unit�rios para valida��o de funcionalidades isoladas da aplica��o.

- **Mockito**  
  Biblioteca para cria��o de mocks e simula��o de comportamentos em testes.

- **MockMvc**  
  Ferramenta para testes de integra��o da camada web (controllers) com o Spring MVC.

- **Swagger (OpenAPI)**  
  Gera documenta��o interativa da API, permitindo explorar e testar os endpoints diretamente no navegador.
---

## Decis�es T�cnicas

### Banco de Dados
O banco **MySQL** foi escolhido por sua ampla ado��o, robustez e familiaridade com bancos relacionais.

###  Padr�o de Projeto - Composite
Foi utilizado o padr�o **Composite** para estruturar e aplicar m�ltiplas valida��es de transa��es de forma modular, reutiliz�vel e escal�vel, tratando diferentes regras de forma uniforme.

###  Transa��es Simult�neas
Utilizamos **Spring Retry** com controle de vers�o otimista via `@Version` para garantir integridade em atualiza��es concorrentes de saldo, evitando conflitos de transa��es simult�neas.

###  Seguran�a da Aplica��o
Com o **Spring Security**, os endpoints s�o protegidos por autentica��o b�sica, garantindo controle de acesso e seguran�a na exposi��o da API.

###  Documenta��o da API
A documenta��o foi feita com **Swagger (OpenAPI)**, permitindo:
- Visualiza��o dos endpoints com seus m�todos e par�metros;
- Testes diretamente via interface web;
- Gera��o autom�tica a partir das anota��es do c�digo (`@Operation`, `@ApiResponse`, etc).

Acesse: [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)

###  Padroniza��o de Logs com Sleuth
Com o **Spring Cloud Sleuth**, cada requisi��o � rastreada por identificadores �nicos (`traceId` e `spanId`), facilitando a an�lise de logs em ambientes distribu�dos.

###  Tratamento Global de Erros
Utilizamos `@ControllerAdvice` para capturar exce��es lan�adas pelos controllers e retornar mensagens padronizadas para o cliente de forma centralizada e desacoplada.

---

##  Endpoints

###  Criar Cart�o

| M�todo | Endpoint                     | Descri��o                                                |
|--------|------------------------------|----------------------------------------------------------|
| POST   | `/cartoes`                   | Cria um novo cart�o com saldo inicial de R$ 500,00       |

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

| M�todo | Endpoint                        | Descri��o                                 |
|--------|----------------------------------|------------------------------------------|
| GET    | `/cartoes/{numeroCartao}`       | Retorna o saldo dispon�vel do cart�o      |

**Curl**:
```bash
curl http://localhost:8080/cartoes/6549873025634501"
```

---

###  Realizar Transa��o

| M�todo | Endpoint           | Descri��o                                                                                             |
|--------|--------------------|-------------------------------------------------------------------------------------------------------|
| POST   | `/transacoes`      | Efetua uma transa��o. Retorna erro se o cart�o n�o existir, saldo for insuficiente ou senha inv�lida. |

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