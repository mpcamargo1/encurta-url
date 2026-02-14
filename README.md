# 🔗 Encurtador de URL (URL Shortener API)

> **Serviço de Redirecionamento de Links**
  - Frontend: Construído com **Angular**, focando na escalabilidade, manutenibilidade e responsividade.
  - Backend: Construído com **Spring Boot**. O foco é garantir **latência ultrabaixa** nas operações de leitura (redirecionamento).

---

## 🏛️ Arquitetura

O sistema foi desenhado visando disponibilidade e tempo de resposta na casa dos sub-milissegundos para o redirecionamento.

1. **Criação da URL:** O usuário envia a URL original. O sistema gera um alias (ex: Base62) e salva no Apache Cassandra.
2. **Redirecionamento:** Quando um alias é acessado, o sistema busca primeiro no **Redis**. Se ocorrer um *cache miss*, ele busca no Cassandra, atualiza o Redis e redireciona o usuário para o destino final.

---

## 🛠️ Stack Principal

* **Linguagem & Framework:** Java 17+ e **Spring Boot 3.x**
* **Banco de Dados:** Apache **Cassandra** (NoSQL distribuído)
* **Cache:** **Redis** (Camada em memória para sub-milissegundo de latência)
* **Build Tool:** Maven
