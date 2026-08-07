# 🔗 Encurtador de URL & Gerador QRCode
![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![Apache Cassandra](https://img.shields.io/badge/Cassandra-1287B1?style=for-the-badge&logo=apache-cassandra&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)

> **Serviço de Redirecionamento de Links de Alta Performance e Geração Nativa de QR Codes**

Este projeto representa um objeto de estudo e aprendizado através do desenvolvimento de uma solução completa para encurtamento de URLs, focada em **latência ultrabaixa** (sub-milissegundos) para redirecionamento e resiliência distribuída.

*   **Frontend:** Construído em **Angular**, otimizado para escalabilidade, manutenibilidade e responsividade.
*   **Backend:** Construído com **Spring Boot**, orquestrando uma arquitetura de cache em múltiplas camadas.

---

## 🚀 O Motor Nativo de QR Code

Um dos maiores diferenciais técnicos deste projeto é a implementação de um **motor de geração de QR Code totalmente nativo**, construído do zero no backend, sem depender de bibliotecas abstratas de terceiros. 

O motor lida diretamente com a especificação técnica do padrão ISO/IEC 18004, incluindo:
*   **Cálculo Dinâmico de Versões:** Adaptação automática da matriz (de 21x21 até 177x177) baseada no payload da URL encurtada.
*   **Gestão de CodeWords:** Alocação precisa de blocos de dados.
*   **Mascaramento de Dados (Data Masking):** Avaliação de penalidades e aplicação de padrões de máscara para otimizar a leitura óptica.
*   **Correção de Erros (Reed-Solomon):** Implementação matemática para cálculo de polinômios geradores e aritmética em Corpos de Galois ($GF(2^8)$) e manipulação bit a bit, garantindo que o QR Code seja legível mesmo se parcialmente danificado.

---

## 🏛️ Arquitetura e Fluxo de Dados

O sistema foi desenhado visando alta disponibilidade e tempo de resposta na casa dos sub-milissegundos para a operação mais crítica: o redirecionamento.

1.  **Criação da URL:** O usuário envia a URL original. O sistema gera um alias criptográfico (ex: formato Base62), processa o QR Code correspondente e salva os metadados no **Apache Cassandra**.
2.  **Redirecionamento Rápido:** Quando um alias é acessado, o sistema consulta primeiramente o **Redis** (camada em memória). 
3.  **Fallback e Sincronização:** Se ocorrer um *cache miss*, o sistema busca o registro persistente no Cassandra, atualiza o Redis imediatamente e redireciona o usuário de forma transparente.

---

## 🛠️ Stack Principal e Infraestrutura

*   **Linguagem & Framework:** Java 17+ e Spring Boot 3.x
*   **Banco de Dados:** Apache Cassandra (NoSQL distribuído)
*   **Cache:** Redis (Memória)
*   **Build Tool:** Maven

### 🐳 Orquestração com Docker Compose

O projeto utiliza Docker Compose para provisionar o ecossistema localmente, garantindo um ambiente de desenvolvimento reproduzível e pronto para uso, visando a facilidade para configurar o ambiente de desenvolvimento.

*  Provisionamento Integrado: Sobe simultaneamente as instâncias do Apache Cassandra, Redis e a aplicação Spring Boot.
*  Inicialização Resiliente: Conta com um processo automatizado que aguarda a formação completa do cluster do banco de dados distribuído antes de aplicar as configurações de keyspace e replicação, garantindo que o ambiente esteja estável, seguro e sincronizado.
