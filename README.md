# Wishlist Service

## O objetivo é que você desenvolva um serviço HTTP resolvendo a funcionalidade de Wishlist do cliente. Esse serviço deve atender os seguintes requisitos:
- Adicionar um produto na Wishlist do cliente;
- Remover um produto da Wishlist do cliente;
- Consultar todos os produtos da Wishlist do cliente;
- Consultar se um determinado produto está na Wishlist do
  cliente;

## Tecnologias Utilizadas
- Java 21
- Spring Boot (versão mais recente)
- MongoDB (banco de dados NoSQL)
- Maven (gerenciador de dependências)
- Arquitetura Hexagonal (Clean Architecture)
- BDD com Cucumber para testes
- Testcontainers para testes integrados

## Estrutura do Projeto
O projeto segue a arquitetura hexagonal (ports and adapters) com as seguintes camadas:

### Domain
- Entidades core do negócio
- Regras de negócio
- Portas (interfaces)

### Application
- Implementação dos casos de uso
- Coordenação dos objetos de domínio

### Adapters
- **Primary/Driving Adapters**: Controllers REST
- **Secondary/Driven Adapters**: Repositórios MongoDB

## Swagger:
```
http://localhost:8080/api/swagger-ui.html
```

# Execução da aplicação:

## Pré-requisitos
- Java 21 JDK
- Maven 3.8+
- Docker e Docker Compose
- MongoDB (se não utilizar docker-compose)

### Usando Maven
1. Clone o repositório
2. Navegue até a pasta raiz do projeto
3. Execute:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Usando Docker
1. Clone o repositório
2. Navegue até a pasta raiz do projeto
3. Execute:
   ```bash
   docker-compose up -d
   ```

### Endpoints

1. **Adicionar Produto à Wishlist**
   - POST /api/v1/customers/{customerId}/wishlist/products
   - Body:
     ```json
     {
       "id": "product123",
       "name": "Smartphone XYZ"
     }
     ```

2. **Remover Produto da Wishlist**
   - DELETE /api/v1/customers/{customerId}/wishlist/products/{productId}

3. **Obter Todos os Produtos da Wishlist**
   - GET /api/v1/customers/{customerId}/wishlist/products

4. **Verificar se Produto está na Wishlist**
   - GET /api/v1/customers/{customerId}/wishlist/products/{productId}
   - Retorna 200 OK se o produto estiver na wishlist, 404 Not Found caso contrário