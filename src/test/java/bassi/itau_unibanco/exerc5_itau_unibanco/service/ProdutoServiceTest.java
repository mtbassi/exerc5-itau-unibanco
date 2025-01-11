package bassi.itau_unibanco.exerc5_itau_unibanco.service;

import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoRequest;
import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoStub;
import bassi.itau_unibanco.exerc5_itau_unibanco.exception.ProdutoNaoEncontradoException;
import bassi.itau_unibanco.exerc5_itau_unibanco.mapper.ProdutoMapper;
import bassi.itau_unibanco.exerc5_itau_unibanco.model.ProdutoModel;
import bassi.itau_unibanco.exerc5_itau_unibanco.producer.CadastroProdutoProducer;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.hamcrest.beans.HasPropertyWithValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Epic("Gestão de Produtos")
@Feature("Testes de Serviço de Produto")
@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    private ProdutoService service;

    private final List<ProdutoModel> produtos = new ArrayList<>(Arrays.asList(
            ProdutoStub.buildProdutoModel(10L, "Cartão PJ", BigDecimal.valueOf(50.00), "PJ"),
            ProdutoStub.buildProdutoModel(20L, "Empréstimo PJ", BigDecimal.valueOf(300.00), "PJ")
    ));

    @Spy
    private final ProdutoMapper mapper = Mappers.getMapper(ProdutoMapper.class);

    @Mock
    private CadastroProdutoProducer producer;

    @BeforeEach
    void setUp() {
        this.service = Mockito.mock(
                ProdutoService.class,
                withSettings().useConstructor(
                                produtos,
                                mapper,
                                producer
                        )
                        .defaultAnswer(Mockito.CALLS_REAL_METHODS)
        );
    }

    @Test
    @Story("Testar a Listagem de Produtos")
    @Description("Este teste verifica se o serviço de produtos retorna corretamente uma lista de produtos cadastrados.")
    @DisplayName("Deve retornar todos os produtos cadastrados.")
    void listarProdutos_DeveRetornarListaDeProdutos() {
        var result = Assertions.assertDoesNotThrow(() -> this.service.listar());

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertThat(result, containsInAnyOrder(
                HasPropertyWithValue.hasProperty("nome", is("Cartão PJ")),
                HasPropertyWithValue.hasProperty("nome", is("Empréstimo PJ"))
        ));
    }

    @Test
    @Story("Testar a busca de produto por ID")
    @Description("Este teste verifica se o serviço de produtos retorna o produto correto quando um ID válido é fornecido.")
    @DisplayName("Deve retornar produto com sucesso quando ID válido for fornecido.")
    void listarProdutoPorId_DeveRetornarProdutoExistente() {
        var result = Assertions.assertDoesNotThrow(() -> this.service.listarPeloId(10L));

        assertNotNull(result);
        assertEquals(ProdutoStub.buildProdutoModel(10L, "Cartão PJ", BigDecimal.valueOf(50.00), "PJ"), result);
    }

    @Test
    @Story("Testar a busca de produto por ID inválido")
    @Description("Este teste verifica se o serviço de produtos lança uma exceção quando um ID inválido (não encontrado) é fornecido.")
    @DisplayName("Deve lançar exceção quando produto não for encontrado pelo ID.")
    void listarProdutoPorId_DeveLancarErroQuandoProdutoNaoEncontrado() {
        var result = Assertions.assertThrows(ProdutoNaoEncontradoException.class, () -> this.service.listarPeloId(100L));

        assertEquals("Produto não encontrado pelo id 100.", result.getMessage());
    }

    @Test
    @Story("Testar o cadastro de produto")
    @Description("Este teste verifica se o serviço de produtos consegue cadastrar um novo produto e retorna o produto com ID gerado corretamente.")
    @DisplayName("Deve cadastrar produto e retornar produto com ID.")
    void cadastrarProduto_DeveRetornarProdutoComIdCadastrado() {
        doNothing().when(this.producer).sendMessage(any(ProdutoModel.class));

        var result = Assertions.assertDoesNotThrow(() -> this.service.cadastrar(
                ProdutoStub.buildProdutoRequest("Cartão PF", BigDecimal.valueOf(25.00), "PF")
        ));

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Cartão PF", result.getNome());
        assertEquals(BigDecimal.valueOf(25.00), result.getPreco());
        assertEquals("PF", result.getCategoria());

        verify(this.mapper).mapToProdutoModel(any(ProdutoRequest.class), anyLong());
        verify(this.producer).sendMessage(any(ProdutoModel.class));
        verifyNoMoreInteractions(this.mapper);
        verifyNoMoreInteractions(this.producer);
    }

    @Test
    @Story("Testar a atualização de produto")
    @Description("Este teste verifica se o serviço de produtos consegue atualizar corretamente um produto existente e retorna os novos dados.")
    @DisplayName("Deve atualizar produto e retornar produto com novos dados.")
    void atualizarProduto_DeveRetornarProdutoAtualizado() {
        var result = Assertions.assertDoesNotThrow(() -> this.service.atualizar(10L,
                ProdutoStub.buildProdutoRequest("Cartão PF", BigDecimal.valueOf(25.00), "PF")
        ));

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Cartão PF", result.getNome());
        assertEquals(BigDecimal.valueOf(25.00), result.getPreco());
        assertEquals("PF", result.getCategoria());

        verify(this.mapper).mapToProdutoModel(any(ProdutoRequest.class), any(ProdutoModel.class));
        verifyNoMoreInteractions(this.mapper);
    }

    @Test
    @Story("Testar a falha ao tentar atualizar produto com ID inválido")
    @Description("Este teste verifica se o serviço de produtos lança uma exceção quando tenta atualizar um produto com um ID inexistente.")
    @DisplayName("Deve falhar ao atualizar produto quando ID não for encontrado.")
    void atualizarProduto_DeveFalharQuandoIdNaoForEncontrado() {
        var produtoRequest = ProdutoStub.buildProdutoRequest("Cartão PF", BigDecimal.valueOf(25.00), "PF");
        var result = Assertions.assertThrows(ProdutoNaoEncontradoException.class, () -> this.service.atualizar(
                100L,
                produtoRequest
        ));

        assertNotNull(result);
        assertEquals("Produto não encontrado pelo id 100.", result.getMessage());

        verifyNoInteractions(this.mapper);
    }

    @Test
    @Story("Testar a exclusão de produto")
    @Description("Este teste verifica se o serviço de produtos consegue excluir um produto com sucesso, sem retornar conteúdo após a exclusão.")
    @DisplayName("Deve deletar produto e não retornar conteúdo.")
    void deletarProduto_DeveExcluirComSucessoSemConteudo() {
        Assertions.assertDoesNotThrow(() -> this.service.deletar(10L));
    }

    @Test
    @Story("Testar a falha ao tentar excluir produto com ID inválido")
    @Description("Este teste verifica se o serviço de produtos lança uma exceção quando tenta excluir um produto com um ID inexistente.")
    @DisplayName("Deve falhar ao deletar produto quando ID não for encontrado.")
    void deletarProduto_DeveLancarErroQuandoIdNaoForEncontrado() {
        var result = Assertions.assertThrows(ProdutoNaoEncontradoException.class, () -> this.service.deletar(100L));

        assertNotNull(result);
        assertEquals("Produto não encontrado pelo id 100.", result.getMessage());
    }
}