package bassi.itau_unibanco.exerc5_itau_unibanco.controller;

import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoRequest;
import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoStub;
import bassi.itau_unibanco.exerc5_itau_unibanco.mapper.ProdutoMapper;
import bassi.itau_unibanco.exerc5_itau_unibanco.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Epic("Gestão de Produtos")
@Feature("Controle de Produtos")
@SpringBootTest
@ActiveProfiles({"test"})
@AutoConfigureMockMvc(addFilters = false)
class ProdutoControllerTest {

    private static final String URI_BASE = "/v1/produto";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdutoMapper mapper;

    @MockitoBean
    private ProdutoService service;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(this.service);
    }

    @SneakyThrows
    @Test
    @Story("Listar Produtos")
    @Description("Verifica se a listagem de todos os produtos ocorre com sucesso.")
    @DisplayName("Deve listar todos os produtos com sucesso")
    void listarProdutos_DeveRetornarTodosComSucesso() {
        when(this.service.listar()).thenReturn(List.of(ProdutoStub.validProdutoResponse()));

        this.mockMvc.perform(get(URI_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1429f29a-a611-4212-8418-39df2e8abe5c"))
                .andExpect(jsonPath("$[0].nome").value("nome"))
                .andExpect(jsonPath("$[0].preco").value(10.00))
                .andExpect(jsonPath("$[0].categoria").value("categoria"));

        verify(this.service).listar();
    }

    @SneakyThrows
    @Test
    @Story("Buscar Produto por ID")
    @Description("Valida a busca de um produto específico por ID válido.")
    @DisplayName("Deve listar produto com sucesso ao fornecer ID válido no path")
    void listarProdutoPorId_DeveRetornarProdutoComSucesso() {
        when(this.service.listarPeloId(any(UUID.class))).thenReturn(ProdutoStub.validProdutoResponse());

        this.mockMvc.perform(get(URI_BASE.concat("/144f6924-fd91-4d4c-b58f-e27c5d19e15f"))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1429f29a-a611-4212-8418-39df2e8abe5c"))
                .andExpect(jsonPath("$.nome").value("nome"))
                .andExpect(jsonPath("$.preco").value(10.00))
                .andExpect(jsonPath("$.categoria").value("categoria"));

        verify(this.service).listarPeloId(any(UUID.class));
    }

    @SneakyThrows
    @Story("Buscar Produto por ID")
    @Description("Valida que a tentativa de buscar um produto com um ID inválido retorna erro conforme esperado.")
    @ParameterizedTest(name = "Busca realizado com ID: {0}")
    @ValueSource(strings = {"1208", "char", "null"})
    @DisplayName("Erro esperado ao listar produto com parâmetro ID inválido.")
    void listarProdutoPorId_DeveFalharQuandoParametroInvalido(String id) {
        this.mockMvc.perform(get(URI_BASE.concat("/%s".formatted(id)))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("A solicitação contém parâmetro inválido."))
                .andExpect(jsonPath("$.invalid-param.name").value("id"));

        verifyNoInteractions(this.service);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{0}")
    @MethodSource("buildProdutoRequestSucessoTest")
    @Story("Cadastrar Produto")
    @Description("Valida o cadastro de um novo produto com informações válidas.")
    @DisplayName("Deve cadastrar produto com sucesso")
    void cadastrarProduto_DeveCriarProdutoComSucesso(String name, ProdutoRequest payload) {
        var produtoEntity = this.mapper.mapToProdutoEntity(payload);
        var id = UUID.randomUUID();
        produtoEntity.setId(id);

        when(this.service.cadastrar(any(ProdutoRequest.class))).thenReturn(ProdutoStub.toProdutoResponse(payload, id));

        this.mockMvc.perform(post(URI_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(payload))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(produtoEntity.getId().toString()))
                .andExpect(jsonPath("$.nome").value(produtoEntity.getNome()))
                .andExpect(jsonPath("$.preco").value(produtoEntity.getPreco()))
                .andExpect(jsonPath("$.categoria").value(produtoEntity.getCategoria()));

        verify(this.service).cadastrar(any(ProdutoRequest.class));
    }

    @SneakyThrows
    @ParameterizedTest(name = "{0}")
    @MethodSource("buildProdutoRequestFalhaTest")
    @Story("Cadastrar Produto")
    @Description("Valida que a tentativa de cadastrar um produto com dados inválidos retorna erros adequados nos parâmetros informados.")
    @DisplayName("Deve falhar ao cadastrar produto com dados inválidos")
    void cadastrarProduto_DeveFalharQuandoDadosInvalidos(String name, ProdutoRequest payload, List<String> parametrosInvalidos) {
        this.mockMvc.perform(post(URI_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(payload))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("A solicitação contém parâmetros inválidos."))
                .andExpect(jsonPath("$.invalid-params[*].name", containsInAnyOrder(parametrosInvalidos.toArray())));

        verifyNoInteractions(this.service);
    }

    private static Stream<Arguments> buildProdutoRequestFalhaTest() {
        return Stream.of(
                Arguments.of(
                        "Não deve permitir \"nome\" vazio.",
                        new ProdutoRequest("", BigDecimal.TEN, "PJ"),
                        List.of("nome")
                ),
                Arguments.of(
                        "Não deve permitir \"nome\" nulo.",
                        new ProdutoRequest(null, BigDecimal.TEN, "PJ"),
                        List.of("nome")
                ),
                Arguments.of(
                        "Não deve permitir \"preco\" nulo.",
                        new ProdutoRequest("Cartão PJ", null, "PJ"),
                        List.of("preco")
                ),
                Arguments.of(
                        "Não deve permitir \"preco\" negativo.",
                        new ProdutoRequest("Cartão PJ", BigDecimal.valueOf(-10), "PJ"),
                        List.of("preco")
                ),
                Arguments.of(
                        "Não deve permitir \"categoria\" vazio.",
                        new ProdutoRequest("Cartão PJ", BigDecimal.TEN, ""),
                        List.of("categoria")
                ),
                Arguments.of(
                        "Não deve permitir \"categoria\" nulo.",
                        new ProdutoRequest("Cartão PJ", BigDecimal.TEN, null),
                        List.of("categoria")
                ),
                Arguments.of(
                        "Não deve permitir nenhum parâmetro nulo.",
                        new ProdutoRequest(null, null, null),
                        List.of("nome", "preco", "categoria")
                )
        );
    }

    @SneakyThrows
    @ParameterizedTest(name = "{0}")
    @MethodSource("buildProdutoRequestSucessoTest")
    @Story("Atualizar Produto")
    @Description("Valida que o sistema é capaz de atualizar as informações de um produto existente com sucesso, utilizando dados válidos.")
    @DisplayName("Deve atualizar produto com sucesso")
    void atualizarProduto_DeveAtualizarComSucesso(String name, ProdutoRequest payload) {
        var produtoAtual = ProdutoStub.valid();
        this.mapper.mapToProdutoModel(payload, produtoAtual);

        when(this.service.atualizar(anyLong(), any(ProdutoRequest.class))).thenReturn(produtoAtual);

        this.mockMvc.perform(put(URI_BASE.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(payload))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produtoAtual.getId()))
                .andExpect(jsonPath("$.nome").value(produtoAtual.getNome()))
                .andExpect(jsonPath("$.preco").value(produtoAtual.getPreco()))
                .andExpect(jsonPath("$.categoria").value(produtoAtual.getCategoria()));

        verify(this.service).atualizar(anyLong(), any(ProdutoRequest.class));
    }

    private static Stream<Arguments> buildProdutoRequestSucessoTest() {
        return Stream.of(
                Arguments.of(
                        "Deve permitir cadastro com preço inteiro positivo.",
                        new ProdutoRequest("Cartão PJ", BigDecimal.TEN, "PJ")
                ),
                Arguments.of(
                        "Deve permitir cadastro com preço igual a zero.",
                        new ProdutoRequest("Cartão PJ", BigDecimal.ZERO, "PJ")
                ),
                Arguments.of(
                        "Deve permitir cadastro com preço contendo duas casas decimais.",
                        new ProdutoRequest("Cartão PJ", BigDecimal.valueOf(10.50), "PJ")
                )
        );
    }

    @SneakyThrows
    @ParameterizedTest(name = "{0}")
    @MethodSource("buildProdutoRequestFalhaTest")
    @Story("Atualizar Produto")
    @Description("Valida que o sistema retorna um erro ao tentar atualizar um produto com dados inválidos.")
    @DisplayName("Deve falhar ao atualizar produto com dados inválidos")
    void atualizarProduto_DeveFalharQuandoDadosInvalidos(String name, ProdutoRequest payload, List<String> parametrosInvalidos) {
        this.mockMvc.perform(put(URI_BASE.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(payload))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("A solicitação contém parâmetros inválidos."))
                .andExpect(jsonPath("$.invalid-params[*].name", containsInAnyOrder(parametrosInvalidos.toArray())));

        verifyNoInteractions(this.service);
    }

    @SneakyThrows
    @Test
    @Story("Atualizar Produto")
    @Description("Valida que o sistema retorna erro ao tentar atualizar um produto com uma variável de caminho inválida.")
    @DisplayName("Deve falhar ao atualizar produto com path variable inválido")
    void atualizarProduto_DeveLancarErroComPathVariableInvalido() {
        this.mockMvc.perform(put(URI_BASE.concat("/null"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(ProdutoStub.valid()))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("A solicitação contém parâmetro inválido."))
                .andExpect(jsonPath("$.invalid-param.name").value("id"));

        verifyNoInteractions(this.service);
    }

    @SneakyThrows
    @Test
    @Story("Deletar Produto")
    @Description("Valida que o sistema exclui um produto com sucesso ao fornecer um ID válido.")
    @DisplayName("Deve excluir produto com sucesso")
    void deletarProduto_DeveExcluirComSucesso() {
        doNothing().when(this.service).deletar(anyLong());

        this.mockMvc.perform(delete(URI_BASE.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        verify(this.service).deletar(anyLong());
    }

    @SneakyThrows
    @Test
    @Story("Deletar Produto")
    @Description("Valida que o sistema retorna erro ao tentar excluir um produto com uma variável de caminho inválida.")
    @DisplayName("Deve falhar ao excluir produto com path variable inválido")
    void deletarProduto_DeveFalharComPathVariableInvalido() {
        this.mockMvc.perform(delete(URI_BASE.concat("/null"))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("A solicitação contém parâmetro inválido."))
                .andExpect(jsonPath("$.invalid-param.name").value("id"));

        verifyNoInteractions(this.service);
    }
}