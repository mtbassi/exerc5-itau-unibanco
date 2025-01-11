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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("Mapeamento de Objetos")
@Feature("Mapeamento para ProdutoEntity")
@SpringBootTest
@ActiveProfiles({"test"})
class ProdutoMapperTest {

    @Autowired
    private ProdutoMapper mapper;

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