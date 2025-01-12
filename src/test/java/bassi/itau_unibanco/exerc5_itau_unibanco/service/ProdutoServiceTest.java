package bassi.itau_unibanco.exerc5_itau_unibanco.service;

import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoRequest;
import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoStub;
import bassi.itau_unibanco.exerc5_itau_unibanco.entity.ProdutoEntity;
import bassi.itau_unibanco.exerc5_itau_unibanco.exception.ProdutoNaoEncontradoException;
import bassi.itau_unibanco.exerc5_itau_unibanco.mapper.ProdutoMapper;
import bassi.itau_unibanco.exerc5_itau_unibanco.producer.CadastroProdutoProducer;
import bassi.itau_unibanco.exerc5_itau_unibanco.repository.ProdutoRepository;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Epic("Gestão de Produtos")
@Feature("Testes de Serviço de Produto")
@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService service;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private CadastroProdutoProducer producer;

    @Spy
    private final ProdutoMapper mapper = Mappers.getMapper(ProdutoMapper.class);

    @Test
    @Story("Testar a Listagem de Produtos")
    @Description("Este teste verifica se o serviço de produtos retorna corretamente uma lista de produtos cadastrados.")
    @DisplayName("Deve retornar todos os produtos cadastrados.")
    void listarProdutos_DeveRetornarListaDeProdutos() {
        var produtoEntities = List.of(
                ProdutoStub.buildProdutoEntity(UUID.fromString("32c6fc74-42f1-4edd-a6fa-3e137512cdcc"), "Cartão PJ", BigDecimal.TEN, "PJ"),
                ProdutoStub.buildProdutoEntity(UUID.fromString("1f0ab96e-a2de-4005-9013-95ff12aa89cc"), "Empréstimo PJ", BigDecimal.TEN, "PJ")
        );
        var resultadoEsperado = List.of(
                ProdutoStub.buildProdutoResponse(UUID.fromString("32c6fc74-42f1-4edd-a6fa-3e137512cdcc"), "Cartão PJ", BigDecimal.TEN, "PJ"),
                ProdutoStub.buildProdutoResponse(UUID.fromString("1f0ab96e-a2de-4005-9013-95ff12aa89cc"), "Empréstimo PJ", BigDecimal.TEN, "PJ")
        );

        when(this.repository.findAll()).thenReturn(produtoEntities);

        var result = Assertions.assertDoesNotThrow(() -> this.service.listar());

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(resultadoEsperado, result);

        verify(this.repository).findAll();
        verify(this.mapper, times(2)).mapToProdutoResponse(any(ProdutoEntity.class));
        verifyNoMoreInteractions(this.repository);
        verifyNoMoreInteractions(this.mapper);
    }

    @Test
    @Story("Testar a busca de produto por ID")
    @Description("Este teste verifica se o serviço de produtos retorna o produto correto quando um ID válido é fornecido.")
    @DisplayName("Deve retornar produto com sucesso quando ID válido for fornecido.")
    void listarProdutoPorId_DeveRetornarProdutoExistente() {
        var id = UUID.randomUUID();

        when(this.repository.findById(any(UUID.class))).thenReturn(
                Optional.of(ProdutoStub.buildProdutoEntity(id, "Cartão PJ", BigDecimal.valueOf(50.00), "PJ"))
        );

        var result = Assertions.assertDoesNotThrow(() -> this.service.listarPeloId(id));

        assertNotNull(result);
        assertEquals(ProdutoStub.buildProdutoResponse(id, "Cartão PJ", BigDecimal.valueOf(50.00), "PJ"), result);

        verify(this.repository).findById(any(UUID.class));
        verify(this.mapper).mapToProdutoResponse(any(ProdutoEntity.class));
        verifyNoMoreInteractions(this.repository);
        verifyNoMoreInteractions(this.mapper);
    }

    @Test
    @Story("Testar a busca de produto por ID inválido")
    @Description("Este teste verifica se o serviço de produtos lança uma exceção quando um ID inválido (não encontrado) é fornecido.")
    @DisplayName("Deve lançar exceção quando produto não for encontrado pelo ID.")
    void listarProdutoPorId_DeveLancarErroQuandoProdutoNaoEncontrado() {
        when(this.repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var id = UUID.randomUUID();
        var result = Assertions.assertThrows(ProdutoNaoEncontradoException.class, () -> this.service.listarPeloId(id));

        assertEquals("Produto não encontrado pelo id %s.".formatted(id), result.getMessage());
    }

    @Test
    @Story("Testa a busca de produtos por nome, categoria e preço")
    @Description("Valida a busca de uma lista de produtos por nome, categoria e preço.")
    @DisplayName("Deve listar produtos com sucesso ao fornecer nome, categoria e preço")
    void listarProdutosPorNomeCategoriaEPreco_DeveRetornarListaDeProdutos() {
        var produtoEntities = List.of(
                ProdutoStub.buildProdutoEntity(UUID.fromString("32c6fc74-42f1-4edd-a6fa-3e137512cdcc"), "Cartão PJ", BigDecimal.TEN, "PJ"),
                ProdutoStub.buildProdutoEntity(UUID.fromString("1f0ab96e-a2de-4005-9013-95ff12aa89cc"), "Empréstimo PJ", BigDecimal.TEN, "PJ")
        );
        var resultadoEsperado = List.of(
                ProdutoStub.buildProdutoResponse(UUID.fromString("32c6fc74-42f1-4edd-a6fa-3e137512cdcc"), "Cartão PJ", BigDecimal.TEN, "PJ"),
                ProdutoStub.buildProdutoResponse(UUID.fromString("1f0ab96e-a2de-4005-9013-95ff12aa89cc"), "Empréstimo PJ", BigDecimal.TEN, "PJ")
        );

        when(this.repository.listagemPersonalizada(anyString(), any(BigDecimal.class), anyString()))
                .thenReturn(produtoEntities);

        var result = Assertions.assertDoesNotThrow(() -> this.service.listagemPersonalizada("nome", BigDecimal.TEN, "categoria"));

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(resultadoEsperado, result);

        verify(this.repository).listagemPersonalizada(anyString(), any(BigDecimal.class), anyString());
        verify(this.mapper, times(2)).mapToProdutoResponse(any(ProdutoEntity.class));
        verifyNoMoreInteractions(this.repository);
        verifyNoMoreInteractions(this.mapper);
    }

    @Test
    @Story("Testar o cadastro de produto")
    @Description("Este teste verifica se o serviço de produtos consegue cadastrar um novo produto e retorna o produto com ID gerado corretamente.")
    @DisplayName("Deve cadastrar produto e retornar produto com ID.")
    void cadastrarProduto_DeveRetornarProdutoComIdCadastrado() {
        doNothing().when(this.producer).sendMessage(any(ProdutoEntity.class));
        var produtoRequest = ProdutoStub.buildProdutoRequest("Cartão PF", BigDecimal.valueOf(25.00), "PF");
        var id = UUID.randomUUID();
        when(this.repository.save(any(ProdutoEntity.class))).thenReturn(ProdutoStub.toProdutoEntity(produtoRequest, id));

        var result = Assertions.assertDoesNotThrow(() -> this.service.cadastrar(produtoRequest));

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Cartão PF", result.nome());
        assertEquals(BigDecimal.valueOf(25.00), result.preco());
        assertEquals("PF", result.categoria());

        verify(this.mapper).mapToProdutoEntity(any(ProdutoRequest.class));
        verify(this.mapper).mapToProdutoResponse(any(ProdutoEntity.class));
        verify(this.repository).save(any(ProdutoEntity.class));
        verify(this.producer).sendMessage(any(ProdutoEntity.class));
        verifyNoMoreInteractions(this.mapper);
        verifyNoMoreInteractions(this.repository);
        verifyNoMoreInteractions(this.producer);
    }

    @Test
    @Story("Testar a atualização de produto")
    @Description("Este teste verifica se o serviço de produtos consegue atualizar corretamente um produto existente e retorna os novos dados.")
    @DisplayName("Deve atualizar produto e retornar produto com novos dados.")
    void atualizarProduto_DeveRetornarProdutoAtualizado() {
        when(this.repository.findById(any(UUID.class))).thenReturn(Optional.of(ProdutoStub.validProdutoEntity()));
        when(repository.save(any(ProdutoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = Assertions.assertDoesNotThrow(() -> this.service.atualizar(
                UUID.fromString("1429f29a-a611-4212-8418-39df2e8abe5c"),
                ProdutoStub.buildProdutoRequest("Cartão PF", BigDecimal.valueOf(25.00), "PF")
        ));

        assertNotNull(result);
        assertEquals(UUID.fromString("1429f29a-a611-4212-8418-39df2e8abe5c"), result.id());
        assertEquals("Cartão PF", result.nome());
        assertEquals(BigDecimal.valueOf(25.00), result.preco());
        assertEquals("PF", result.categoria());

        verify(this.mapper).mapToProdutoEntity(any(ProdutoRequest.class), any(ProdutoEntity.class));
        verify(this.mapper).mapToProdutoResponse(any(ProdutoEntity.class));
        verify(this.repository).findById(any(UUID.class));
        verify(this.repository).save(any(ProdutoEntity.class));
        verifyNoMoreInteractions(this.mapper);
        verifyNoMoreInteractions(this.repository);
    }

    @Test
    @Story("Testar a falha ao tentar atualizar produto com ID inválido")
    @Description("Este teste verifica se o serviço de produtos lança uma exceção quando tenta atualizar um produto com um ID inexistente.")
    @DisplayName("Deve falhar ao atualizar produto quando ID não for encontrado.")
    void atualizarProduto_DeveFalharQuandoIdNaoForEncontrado() {
        when(this.repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var produtoRequest = ProdutoStub.buildProdutoRequest("Cartão PF", BigDecimal.valueOf(25.00), "PF");
        var id = UUID.randomUUID();
        var result = Assertions.assertThrows(ProdutoNaoEncontradoException.class, () -> this.service.atualizar(
                id,
                produtoRequest
        ));

        assertNotNull(result);
        assertEquals("Produto não encontrado pelo id %s.".formatted(id), result.getMessage());

        verify(this.repository).findById(any(UUID.class));
        verifyNoMoreInteractions(this.repository);
        verifyNoInteractions(this.mapper);
    }

    @Test
    @Story("Testar a exclusão de produto")
    @Description("Este teste verifica se o serviço de produtos consegue excluir um produto com sucesso, sem retornar conteúdo após a exclusão.")
    @DisplayName("Deve deletar produto e não retornar conteúdo.")
    void deletarProduto_DeveExcluirComSucessoSemConteudo() {
        when(this.repository.findById(any(UUID.class))).thenReturn(Optional.of(ProdutoStub.validProdutoEntity()));
        doNothing().when(this.repository).delete(any(ProdutoEntity.class));

        var id = UUID.randomUUID();
        Assertions.assertDoesNotThrow(() -> this.service.deletar(id));

        verify(this.repository).findById(any(UUID.class));
        verify(this.repository).delete(any(ProdutoEntity.class));
        verifyNoMoreInteractions(this.repository);
    }

    @Test
    @Story("Testar a falha ao tentar excluir produto com ID inválido")
    @Description("Este teste verifica se o serviço de produtos lança uma exceção quando tenta excluir um produto com um ID inexistente.")
    @DisplayName("Deve falhar ao deletar produto quando ID não for encontrado.")
    void deletarProduto_DeveLancarErroQuandoIdNaoForEncontrado() {
        when(this.repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var id = UUID.randomUUID();
        var result = Assertions.assertThrows(ProdutoNaoEncontradoException.class, () -> this.service.deletar(id));

        assertNotNull(result);
        assertEquals("Produto não encontrado pelo id %s.".formatted(id), result.getMessage());

        verify(this.repository).findById(any(UUID.class));
        verifyNoMoreInteractions(this.repository);
    }
}