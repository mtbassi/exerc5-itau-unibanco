package bassi.itau_unibanco.exerc2_itau_unibanco.mapper;

import bassi.itau_unibanco.exerc2_itau_unibanco.dto.ProdutoStub;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("Mapeamento de Objetos")
@Feature("Mapeamento para ProdutoModel")
@SpringBootTest
@ActiveProfiles({"test"})
class ProdutoMapperTest {

    @Autowired
    private ProdutoMapper mapper;

    @Test
    @Story("Mapear ProdutoRequest para ProdutoModel")
    @Description("Verifica se o método mapToProdutoModel converte corretamente um ProdutoRequest em ProdutoModel, preservando os valores e adicionando o ID.")
    @DisplayName("Deve mapear ProdutoRequest para ProdutoModel corretamente.")
    void mapearProdutoRequestParaProdutoModel_DeveRetornarObjetoCorreto() {
        var produtoRequest = ProdutoStub.buildProdutoRequest("Cartão PJ", BigDecimal.valueOf(50.00), "PJ");

        var produtoModel = Assertions.assertDoesNotThrow(() -> this.mapper.mapToProdutoModel(produtoRequest, 1L));

        assertNotNull(produtoModel);
        assertEquals(1L, produtoModel.getId());
        assertEquals("Cartão PJ", produtoModel.getNome());
        assertEquals(BigDecimal.valueOf(50.00), produtoModel.getPreco());
        assertEquals("PJ", produtoModel.getCategoria());
    }

    @Test
    @Story("Atualizar ProdutoModel com ProdutoRequest")
    @Description("Valida se o método mapToProdutoModel atualiza corretamente um ProdutoModel existente com informações de um ProdutoRequest.")
    @DisplayName("Deve atualizar ProdutoModel com informações do ProdutoRequest.")
    void atualizarProdutoModelComProdutoRequest_DeveAtualizarCorretamente() {
        var produtoRequest = ProdutoStub.buildProdutoRequest("Cartão PJ", BigDecimal.valueOf(50.00), "PJ");
        var produtoModel = ProdutoStub.buildProdutoModel(1L, "Cartão PF", BigDecimal.valueOf(25.00), "PF");
        Assertions.assertDoesNotThrow(() -> this.mapper.mapToProdutoModel(produtoRequest, produtoModel));

        assertNotNull(produtoModel);
        assertEquals(1L, produtoModel.getId());
        assertEquals("Cartão PJ", produtoModel.getNome());
        assertEquals(BigDecimal.valueOf(50.00), produtoModel.getPreco());
        assertEquals("PJ", produtoModel.getCategoria());
    }
}