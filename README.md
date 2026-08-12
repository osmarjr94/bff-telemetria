# 🚗 BFF Telemetria

Backend For Frontend (BFF) desenvolvido em **Java** e **Spring Boot** para orquestração, consulta de dados de telemetria veicular e aplicação de padrões avançados de resiliência e observabilidade.

---

## 🛠️ Tecnologias e Ferramentas Utilizadas

* **Java 21**
* **Spring Boot** (Web, Actuator)
* **Resilience4j** (Circuit Breaker & Retry)
* **Maven** (Gerenciamento de dependências)

---

## 🏗️ Arquitetura e Decisões de Design

* **Comunicação Síncrona (REST):** Focado em consultas e orquestração sob demanda, garantindo resposta imediata ao cliente com alta performance.
* **Resiliência com Resilience4j:** Proteção contra falhas em cascata utilizando **Retry** (tentativas automáticas) e **Circuit Breaker** (abertura de circuito para evitar esgotamento de threads em caso de instabilidade externa).
* **Tratamento Global de Exceções:** Separação clara entre erros de negócio (ex: `404 Not Found` para veículos inexistentes) e falhas de infraestrutura, impedindo falsos positivos no disjuntor.

---

## 🔌 Endpoints Disponíveis

### 1. Telemetria
* **GET `/api/telemetria/{placa}`**
    * Retorna o status de velocidade e dados de telemetria do veículo consultado.
    * *Exemplo de Resposta (200 OK):*
      ```json
      {
        "placa": "ABC1234",
        "status": "ALERTA: Velocidade excedida!",
        "dataConsulta": "2026-08-12T11:40:21.58193"
      }
      ```
    * *Exemplo de Resposta (404 Not Found - Veículo não encontrado):*
      ```json
      {
        "erro": "Não Encontrado",
        "mensagem": "Veículo não encontrado para a placa: XYZ9876",
        "timestamp": "2026-08-12T11:42:00.000",
        "status": 404
      }
      
      ### 📅 Consultar Histórico por Data

Retorna uma lista com os eventos de telemetria de um veículo específico filtrados por uma data de referência.

* **URL:** `/api/telemetria/{placa}/historico`
* **Método:** `GET`
* **Parâmetro de Caminho (`Path Parameter`):**
    * `placa` (String): Placa do veículo (Ex: `ABC1234`)
* **Parâmetro de Consulta (`Query Parameter`):**
    * `data` (LocalDate): Data no formato `yyyy-MM-dd` (Ex: `2026-08-12`)

#### 📥 Exemplo de Requisição
```http
GET http://localhost:8080/api/telemetria/ABC1234/historico?data=2026-08-12
      ```

### 2. Observabilidade (Spring Boot Actuator)
* **GET `/actuator/health`**
    * Verifica a saúde geral da aplicação, uso de disco e estados de *Liveness* / *Readiness*.
* **GET `/actuator/circuitbreakers`**
    * Exibe o estado em tempo real dos Circuit Breakers configurados (ex: taxa de falhas, chamadas em buffer e se o circuito está `CLOSED` ou `OPEN`).

---

## ⚙️ Configurações de Resiliência (`application.properties`)

As regras do Resilience4j estão configuradas para monitorar a taxa de falhas e proteger o serviço:

```properties
# Configuração do Circuit Breaker para a telemetria
resilience4j.circuitbreaker.instances.telemetriaService.failureRateThreshold=50
resilience4j.circuitbreaker.instances.telemetriaService.slowCallRateThreshold=50
resilience4j.circuitbreaker.instances.telemetriaService.slowCallDurationThreshold=2s
resilience4j.circuitbreaker.instances.telemetriaService.slidingWindowType=COUNT_BASED
resilience4j.circuitbreaker.instances.telemetriaService.slidingWindowSize=10
resilience4j.circuitbreaker.instances.telemetriaService.minimumNumberOfCalls=5
resilience4j.circuitbreaker.instances.telemetriaService.waitDurationInOpenState=10s
resilience4j.circuitbreaker.instances.telemetriaService.permittedNumberOfCallsInHalfOpenState=3

# Actuator & Métricas
management.endpoints.web.exposure.include=health,metrics,circuitbreakers,circuitbreakerevents
management.endpoint.health.show-details=always
resilience4j.circuitbreaker.metrics.enabled=true