# language: pt
Funcionalidade: Gerenciamento de Wishlist
  Como um cliente da loja online
  Eu quero gerenciar minha lista de desejos (wishlist)
  Para que eu possa salvar produtos que tenho interesse em comprar no futuro

  Cenário: Adicionar um produto à wishlist
    Dado que eu sou um cliente com ID "customer-123"
    E que eu tenho um produto com ID "product-123" e nome "Smartphone"
    Quando eu adicionar o produto à minha wishlist
    Então o produto deve ser adicionado com sucesso

  Cenário: Remover um produto da wishlist
    Dado que eu sou um cliente com ID "customer-123"
    E que eu tenho um produto com ID "product-123" e nome "Smartphone"
    E eu adicionar o produto à minha wishlist
    Quando eu remover o produto da minha wishlist
    Então o produto deve ser removido com sucesso

  Cenário: Consultar todos os produtos da wishlist
    Dado que eu sou um cliente com ID "customer-123"
    E que eu tenho um produto com ID "product-123" e nome "Smartphone"
    E eu adicionar o produto à minha wishlist
    Quando eu consultar todos os produtos da minha wishlist
    Então eu devo ver 1 produtos na minha wishlist

  Cenário: Verificar se um produto está na wishlist
    Dado que eu sou um cliente com ID "customer-123"
    E que eu tenho um produto com ID "product-123" e nome "Smartphone"
    E eu adicionar o produto à minha wishlist
    Quando eu verificar se o produto está na minha wishlist
    Então o produto deve estar na minha wishlist

  Cenário: Verificar se um produto não está na wishlist
    Dado que eu sou um cliente com ID "customer-123"
    E que eu tenho um produto com ID "product-404" e nome "not found"
    Quando eu verificar se o produto está na minha wishlist
    Então o produto não deve estar na minha wishlist

  Cenário: Limitar a wishlist a 20 produtos
    Dado que eu sou um cliente com ID "customer-123"
    E que eu já tenho 20 produtos na minha wishlist
    E que eu tenho um produto com ID "product-extra" e nome "Produto Extra"
    Quando eu adicionar o produto à minha wishlist
    Então devo receber um erro indicando que a wishlist está cheia