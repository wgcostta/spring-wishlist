package com.lzb.wishlist.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzb.wishlist.adapter.in.web.dto.ProductRequest;
import com.lzb.wishlist.adapter.out.persistence.repository.MongoWishlistRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WishlistSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoWishlistRepository wishlistRepository;

    private String customerId;
    private String productId;
    private String productName;
    private ResultActions resultActions;

    @Before
    public void setUp() {
        wishlistRepository.deleteAll();
    }

    @After
    public void tearDown() {
        wishlistRepository.deleteAll();
    }

    @Dado("que eu sou um cliente com ID {string}")
    public void queEuSouUmClienteComID(String customerId) {
        this.customerId = customerId;
    }

    @Dado("que eu tenho um produto com ID {string} e nome {string}")
    public void queEuTenhoUmProdutoComIDENome(String productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    @Quando("eu adicionar o produto à minha wishlist")
    public void euAdicionarOProdutoÀMinhaWishlist() throws Exception {
        ProductRequest productRequest = new ProductRequest(productId, productName);

        resultActions = mockMvc.perform(post("/v1/customers/{customerId}/wishlist/products", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));
    }

    @Quando("eu remover o produto da minha wishlist")
    public void euRemoverOProdutoDaMinhaWishlist() throws Exception {
        resultActions = mockMvc.perform(delete("/v1/customers/{customerId}/wishlist/products/{productId}",
                customerId, productId));
    }

    @Quando("eu consultar todos os produtos da minha wishlist")
    public void euConsultarTodosOsProdutosDaMinhaWishlist() throws Exception {
        resultActions = mockMvc.perform(get("/v1/customers/{customerId}/wishlist/products", customerId));
    }

    @Quando("eu verificar se o produto está na minha wishlist")
    public void euVerificarSeOProdutoEstáNaMinhaWishlist() throws Exception {
        resultActions = mockMvc.perform(get("/v1/customers/{customerId}/wishlist/products/{productId}",
                customerId, productId));
    }

    @Então("o produto deve ser adicionado com sucesso")
    public void oProdutoDeveSerAdicionadoComSucesso() throws Exception {
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.products", hasSize(1)))
                .andExpect(jsonPath("$.products[0].id").value(productId));
    }

    @Então("o produto deve ser removido com sucesso")
    public void oProdutoDeveSerRemovidoComSucesso() throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(0)));
    }

    @Então("eu devo ver {int} produtos na minha wishlist")
    public void euDevoVerProdutosNaMinhaWishlist(int count) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(count)));
    }

    @Então("o produto deve estar na minha wishlist")
    public void oProdutoDeveEstarNaMinhaWishlist() throws Exception {
        resultActions.andExpect(status().isOk());
    }

    @Então("o produto não deve estar na minha wishlist")
    public void oProdutoNãoDeveEstarNaMinhaWishlist() throws Exception {
        resultActions.andExpect(status().isNotFound());
    }

    @Então("devo receber um erro indicando que a wishlist está cheia")
    public void devoReceberUmErroIndicandoQueAWishlistEstáCheia() throws Exception {
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Wishlist has reached the maximum limit of products"));
    }

    @Dado("que eu já tenho {int} produtos na minha wishlist")
    public void queEuJáTenhoProdutosNaMinhaWishlist(int count) throws Exception {
        for (int i = 1; i <= count; i++) {
            ProductRequest productRequest = new ProductRequest("product-" + i, "Product " + i);

            mockMvc.perform(post("/v1/customers/{customerId}/wishlist/products", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(productRequest)))
                    .andExpect(status().isCreated());
        }
    }
}