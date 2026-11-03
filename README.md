# 🔗 Encurtador de URL & Gerador QRCode

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![Apache Cassandra](https://img.shields.io/badge/Cassandra-1287B1?style=for-the-badge&logo=apache-cassandra&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![gRPC](https://img.shields.io/badge/gRPC-244c5a?style=for-the-badge&logo=grpc&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

> **Serviço de Redirecionamento de Links de Alta Performance, Geração Nativa de QR Codes e Arquitetura Distribuída**

Este projeto representa um objeto de estudo e aprendizado através do desenvolvimento de uma solução completa para encurtamento de URLs, focada em **latência ultrabaixa** (sub-milissegundos) para redirecionamento, resiliência distribuída e processamento de dados em baixo nível.

* **Frontend:** Construído em **Angular**, otimizado para escalabilidade, manutenibilidade e responsividade.
* **Backend:** Construído com **Spring Boot** (Multi-módulos Maven), suportando APIs REST e **gRPC/Protobuf**, orquestrando uma arquitetura de cache em múltiplas camadas.

---

## 🚀 O Motor Nativo de QR Code

Um dos maiores diferenciais técnicos deste projeto é a implementação de um **motor de geração de QR Code totalmente nativo**, construído do zero no backend, sem depender de bibliotecas de terceiros (como ZXing).

O motor lida diretamente com a especificação técnica do padrão **ISO/IEC 18004**, incluindo:
* **Cálculo Dinâmico de Versões:** Adaptação automática da matriz (de 21x21 até 177x177) baseada no payload da URL encurtada.
* **Gestão de CodeWords:** Alocação precisa de blocos de dados.
* **Mascaramento de Dados (Data Masking):** Avaliação de penalidades e aplicação de padrões de máscara para otimizar a leitura óptica.
* **Correção de Erros (Reed-Solomon):** Implementação matemática para cálculo de polinômios geradores e aritmética em Corpos de Galois ($GF(2^8)$) com manipulação bit a bit, garantindo que o QR Code seja legível mesmo se parcialmente danificado.

---

## 🏛️ Arquitetura e Fluxo de Dados

O sistema foi desenhado visando alta disponibilidade e tempo de resposta na casa dos sub-milissegundos para a operação mais crítica: o redirecionamento.

### Fluxo Operacional:
1. **Criação da URL:** O usuário envia a URL original (via REST ou gRPC). O sistema gera um alias curto (formato Base62 com identificador único) e persiste os metadados no **cluster Apache Cassandra** com replicação configurada.
2. **Redirecionamento Rápido:** Ao acessar o alias, a aplicação consulta primeiramente o **Redis** (`redis-cache-url`).
3. **Fallback e Sincronização:** Em caso de *cache miss*, o sistema busca o registro persistente no cluster Cassandra, popula o Redis imediatamente e realiza o redirecionamento HTTP 302.

---

## 📂 Estrutura do Repositório

O projeto é estruturado em monorepo com módulos Maven desacoplados:

```text
├── backend/
│   ├── nucleo/              # Biblioteca compartilhada
│   ├── encurta-url/         # Serviço principal (REST & gRPC, Cassandra, Redis)
│   └── qrcode/              # Motor autônomo de geração matemática de QR Code
├── frontend/
│   └── encurta-url/         # Aplicação SPA desenvolvida em Angular
├── k6/                      # Scripts de testes de estresse e carga de alta concorrência
└── run-app/                 # Orquestração com Docker Compose, Nginx e Observabilidade
    └── env/                 # Localização dos arquivos .env (obtidos via script -- SOMENTE contribuidores)
    └── observalidade/       # Arquivos utilizados para configurar as ferramentas de observabilidade
```

---

## 🔌 Especificação das APIs

### REST Endpoints

| Método | Endpoint | Descrição | Payload / Resposta |
| :--- | :--- | :--- | :--- |
| `POST` | `/shorten` | Encurta uma URL original | Body: `{"urlOriginal": "https://..."}`<br>Retorno: `201 Created` |
| `GET` | `/{hash}` | Redireciona para a URL original | Retorno: `302 Found` (Header `Location`) |
| `POST` | `/qrcode` | Gera a imagem PNG do QR Code nativo | Body: `{"url": "https://..."}`<br>Retorno: `200 OK` (`image/png`) |

### gRPC Service
Para integração de alta performance entre microserviços, o módulo `encurta-url` disponibiliza a interface gRPC `EncurtaService`:
* **Método RPC:** `encurtarURL(EncurtaRequest) returns (EncurtaResponse)`

---

## 📊 Observabilidade & Testes de Carga

* **Métricas e Logs:** Monitoramento integrado com **Prometheus**, **Loki** e dashboards provisionados no **Grafana** (porta `3000`).
* **Instrumentação:** Uso do Micrometer (`@Observed`) em controllers e services para rastreabilidade de latência.
* **Testes de Carga com k6:** Cenários de validação de taxa de transferência e degradação sob carga:
  - `ramping-arrival-rate.js`: Aumento progressivo de requisições por segundo.
  - `constant-arrival-rate.js`: Estresse constante para validação de estabilidade e consumo de memória.

---

## 🐳 Como Executar Localmente

### Pré-requisitos
* [Docker](https://docs.docker.com/get-docker/) & [Docker Compose](https://docs.docker.com/compose/)
* [Node.js](https://nodejs.org/) (v18+) e Angular CLI (`npm install -g @angular/cli`)

### Inicialização Rápida

O repositório conta com scripts automatizados para build do frontend e inicialização coordenada do ambiente:

```bash
# 1. Clone o repositório
git clone https://github.com/mpcamargo/encurta-url.git
cd encurta-url

# 2. Acesse a pasta de orquestração e execute o script de inicialização
cd run-app
./start.sh
```

O script cuidará de:
1. Executar o build de produção da aplicação Angular (`frontend/encurta-url`).
2. Mapear os artefatos estáticos para o Nginx.
3. Subir o cluster de 3 nós do Cassandra aguardando o *quorum* e a execução automática do `init.cql`.
4. Inicializar as instâncias dedicadas de Redis, Prometheus, Loki, Grafana e os microserviços Spring Boot.

### Acesso aos Serviços

* **Aplicação Web (Frontend & Proxy):** [http://localhost](http://localhost)
* **Grafana Dashboards:** [http://localhost:3000](http://localhost:3000)
* **Prometheus Metrics:** [http://localhost:9090](http://localhost:9090)
