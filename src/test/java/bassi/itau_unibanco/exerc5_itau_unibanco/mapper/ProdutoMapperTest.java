package bassi.itau_unibanco.exerc5_itau_unibanco.mapper;

import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoStub;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Mapeamento de Objetos")
@Feature("Mapeamento para ProdutoEntity")
@SpringBootTest
@ActiveProfiles({"test"})
class ProdutoMapperTest {

    @Autowired
    private ProdutoMapper mapper;

    @Test
    @Story("Mapeia ProdutoRequest para ProdutoEntity")
    @Description("Valida se o método mapToProdutoEntity atualiza corretamente um ProdutoEntity existente com informações de um ProdutoRequest.")
    @DisplayName("Deve mapear ProdutoRequest para ProdutoEntity.")
    void mapeiaProdutoRequestParaProdutoEntity_DeveMapearCorretamente() {
        var produtoRequest = ProdutoStub.buildProdutoRequest("Cartão PJ", BigDecimal.valueOf(50.00), "PJ");
        var produtoEntity = Assertions.assertDoesNotThrow(() -> this.mapper.mapToProdutoEntity(produtoRequest));

        assertNotNull(produtoEntity);
        assertNull(produtoEntity.getId());
        assertEquals("Cartão PJ", produtoEntity.getNome());
        assertEquals(BigDecimal.valueOf(50.00), produtoEntity.getPreco());
        assertEquals("PJ", produtoEntity.getCategoria());
    }

    @Test
    @Story("Mapeia ProdutoEntity para ProdutoResponse")
    @Description("Valida se o método mapToProdutoEntity atualiza corretamente um ProdutoEntity existente com informações de um ProdutoRequest.")
    @DisplayName("Deve mapear ProdutoEntity para ProdutoResponse.")
    void mapeiaProdutoEntityParaProdutoResponse_DeveMapearCorretamente() {
        var produtoEntity = ProdutoStub.buildProdutoEntity(UUID.fromString("850db7cf-d747-4dff-8041-426885842420"), "Cartão PJ", BigDecimal.valueOf(25.00), "PJ");
        var produtoResponse = Assertions.assertDoesNotThrow(() -> this.mapper.mapToProdutoResponse(produtoEntity));

        assertNotNull(produtoResponse);
        assertEquals("850db7cf-d747-4dff-8041-426885842420", produtoResponse.id().toString());
        assertEquals("Cartão PJ", produtoResponse.nome());
        assertEquals(BigDecimal.valueOf(25.00), produtoResponse.preco());
        assertEquals("PJ", produtoResponse.categoria());
    }

    @Test
    @Story("Atualizar ProdutoEntity com ProdutoRequest")
    @Description("Valida se o método mapToProdutoEntity atualiza corretamente um ProdutoEntity existente com informações de um ProdutoRequest.")
    @DisplayName("Deve atualizar ProdutoEntity com informações do ProdutoRequest.")
    void atualizarProdutoEntityComProdutoRequest_DeveAtualizarCorretamente() {
        var produtoRequest = ProdutoStub.buildProdutoRequest("Cartão PJ", BigDecimal.valueOf(50.00), "PJ");
        var produtoEntity = ProdutoStub.buildProdutoEntity(UUID.fromString("850db7cf-d747-4dff-8041-426885842420"), "Cartão PF", BigDecimal.valueOf(25.00), "PF");
        Assertions.assertDoesNotThrow(() -> this.mapper.mapToProdutoEntity(produtoRequest, produtoEntity));

        assertNotNull(produtoEntity);
        assertEquals("850db7cf-d747-4dff-8041-426885842420", produtoEntity.getId().toString());
        assertEquals("Cartão PJ", produtoEntity.getNome());
        assertEquals(BigDecimal.valueOf(50.00), produtoEntity.getPreco());
        assertEquals("PJ", produtoEntity.getCategoria());
    }
}