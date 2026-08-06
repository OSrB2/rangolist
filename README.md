# rangolist

API RESTful para gerenciamento de restaurantes, produtos (itens de cardápio) e promoções, construída com Java e Spring Boot.

O objetivo do projeto é servir de backend para um sistema de cardápio digital / delivery: cada restaurante cadastra seus produtos, organizados por categoria, e pode aplicar promoções com preço, dias e horários específicos.

## Índice

- [Funcionalidades](#funcionalidades)
- [Stack técnica](#stack-técnica)
- [Modelo de dados](#modelo-de-dados)
- [Endpoints da API](#endpoints-da-api)
- [Como rodar o projeto](#como-rodar-o-projeto)
    - [Pré-requisitos](#pré-requisitos)
    - [Opção 1: banco em memória (H2) — mais rápido para desenvolver](#opção-1-banco-em-memória-h2--mais-rápido-para-desenvolver)
    - [Opção 2: PostgreSQL com Docker](#opção-2-postgresql-com-docker)
    - [Variáveis de ambiente](#variáveis-de-ambiente)
- [Testes](#testes)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Roadmap / próximos passos](#roadmap--próximos-passos)

## Funcionalidades

- **Restaurantes**: cadastro com nome, endereço (rua, cidade, estado, CEP), horário de funcionamento e imagem (upload de arquivo).
- **Produtos**: cadastro vinculado a um restaurante, com nome, preço, categoria e imagem.
- **Promoções**: desconto aplicado a um produto, com descrição, preço promocional e dias/horários em que a promoção é válida.
- CRUD completo (criar, listar, buscar por nome, editar e excluir) para as três entidades.
- Upload de imagens via `multipart/form-data`.
- Tratamento de erros centralizado, com exceptions específicas (ex: restaurante não encontrado, produto sem promoções, etc).
- Migrações de banco de dados versionadas com Flyway.
- Suíte de testes cobrindo controllers, services, repositories e mappers.

## Stack técnica

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.3 (Web, Data JPA, Validation) |
| Banco de dados | PostgreSQL (produção/dev via Docker) e H2 (testes / execução local rápida) |
| Migrações | Flyway |
| Mapeamento objeto-DTO | MapStruct |
| Redução de boilerplate | Lombok |
| Build | Maven (via Maven Wrapper `mvnw`) |
| Variáveis de ambiente | dotenv-java |

## Modelo de dados

```
Restaurant (tb_restaurants)
├── id, image, name, address (embutido), openingHours
├── status, createdAt, updatedAt
└── products (1:N) ──► Product (tb_products)
                        ├── id, image, name, price, category
                        ├── status, createdAt, updatedAt
                        ├── restaurant (N:1)
                        └── promotions (1:N) ──► Promotion (tb_promotions)
                                                  ├── id, description
                                                  ├── promoPrice, promoDays, promoHours
                                                  ├── status, createdAt, updatedAt
                                                  └── product (N:1)
```

O `Address` (rua, cidade, estado, CEP) é um objeto `@Embeddable`, ou seja, é salvo como colunas dentro da própria tabela de restaurantes, não em uma tabela separada.

## Endpoints da API

Base path: `/api`

### Restaurantes — `/api/restaurant`

| Método | Rota | Descrição | Corpo |
|---|---|---|---|
| `POST` | `/api/restaurant` | Cria um restaurante | `multipart/form-data` (campos do restaurante + `file` opcional) |
| `GET` | `/api/restaurant` | Lista todos os restaurantes | — |
| `GET` | `/api/restaurant/{id}` | Busca um restaurante por ID | — |
| `GET` | `/api/restaurant/find?name=` | Busca restaurantes por nome | — |
| `PUT` | `/api/restaurant/{id}` | Atualiza um restaurante | `application/json` |
| `DELETE` | `/api/restaurant/{id}` | Remove um restaurante | — |

### Produtos — `/api/product`

| Método | Rota | Descrição | Corpo |
|---|---|---|---|
| `POST` | `/api/product` | Cria um produto | `multipart/form-data` (campos do produto + `file` opcional) |
| `GET` | `/api/product` | Lista todos os produtos (com dados do restaurante) | — |
| `GET` | `/api/product/all` | Lista produtos sem os dados do restaurante | — |
| `GET` | `/api/product/{id}` | Busca um produto por ID | — |
| `GET` | `/api/product/find?name=` | Busca produtos por nome | — |
| `PUT` | `/api/product/{id}` | Atualiza um produto | `application/json` |
| `DELETE` | `/api/product/{id}` | Remove um produto | — |

### Promoções — `/api/promotion`

| Método | Rota | Descrição | Corpo |
|---|---|---|---|
| `POST` | `/api/promotion` | Cria uma promoção | `application/json` |
| `GET` | `/api/promotion` | Lista todas as promoções | — |
| `GET` | `/api/promotion/{id}` | Busca uma promoção por ID | — |
| `PUT` | `/api/promotion/{id}` | Atualiza uma promoção | `application/json` |
| `DELETE` | `/api/promotion/{id}` | Remove uma promoção | — |

> Atenção: os endpoints de restaurante e produto recebem os dados via `@ModelAttribute`, então o `POST`/`PUT` de cadastro precisa ser enviado como `FormData` (não JSON) quando houver upload de imagem. Já o endpoint de promoção usa `@RequestBody`, então espera JSON puro.

## Como rodar o projeto

### Pré-requisitos

- [Java 17+](https://adoptium.net/)
- Maven (opcional — o projeto já inclui o Maven Wrapper, `mvnw`/`mvnw.cmd`)
- [Docker](https://www.docker.com/) (apenas se for usar PostgreSQL local)

### Opção 1: banco em memória (H2) — mais rápido para desenvolver

Não requer Docker nem instalação de banco de dados. Os dados são perdidos ao reiniciar a aplicação.

```bash
# clonar o repositório
git clone https://github.com/OSrB2/rangolist.git
cd rangolist

# rodar a aplicação
./mvnw spring-boot:run     # Linux/macOS
mvnw.cmd spring-boot:run   # Windows
```

A aplicação sobe por padrão em `http://localhost:8080`.

O console do H2 fica disponível em `http://localhost:8080/h2`, com:
- JDBC URL: `jdbc:h2:mem:~/rangolist`
- Usuário: `sa`
- Senha: *(em branco)*

### Opção 2: PostgreSQL com Docker

1. Suba o banco de dados com o Docker Compose incluso no projeto:

   ```bash
   docker compose up -d
   ```

   Isso cria um container PostgreSQL 15 na porta `5432`, com:
    - Usuário: `docker`
    - Senha: `docker`
    - Banco: `rangolist`

2. Configure o `src/main/resources/application.properties` para apontar para o PostgreSQL (descomente/ajuste as linhas de `spring.datasource.*` referentes ao Postgres e comente as do H2), e habilite o Flyway (`spring.flyway.enabled=true`) para que as migrações em `src/main/resources/db/migration` sejam aplicadas automaticamente.

3. Rode a aplicação normalmente:

   ```bash
   ./mvnw spring-boot:run
   ```

### Variáveis de ambiente

Copie o arquivo de exemplo e ajuste os valores:

```bash
cp .envExample .env
```

| Variável | Descrição |
|---|---|
| `UPLOAD_DIR` | Caminho local onde as imagens enviadas (restaurantes e produtos) serão salvas em disco. |

## Testes

O projeto tem cobertura de testes para controllers, services, repositories e mappers, usando o banco H2 em memória.

```bash
./mvnw test
```

## Estrutura do projeto

```
src/main/java/com/pedrooliveira/rangolist/
├── controller/     # Endpoints REST (Restaurant, Product, Promotion)
├── service/        # Regras de negócio
├── repository/     # Interfaces Spring Data JPA
├── model/          # Entidades JPA (Restaurant, Product, Promotion, Address)
├── dto/            # Objetos de transferência de dados expostos pela API
├── mapper/         # Conversão entidade <-> DTO (MapStruct)
├── exception/      # Exceptions customizadas e handler global
└── utils/          # Utilitários de upload de arquivos

src/main/resources/
├── application.properties
└── db/migration/   # Scripts de migração Flyway (criação das tabelas)

src/test/java/...   # Testes espelhando a estrutura de main
```

## Roadmap / próximos passos

- [ ] Autenticação e autorização (hoje a API é totalmente aberta)
- [ ] Configuração de CORS para permitir consumo por um front-end em outra origem
- [ ] Documentação interativa da API (Swagger/OpenAPI)
- [ ] Front-end (app do cliente + painel administrativo)

## Licença

# rangolist
