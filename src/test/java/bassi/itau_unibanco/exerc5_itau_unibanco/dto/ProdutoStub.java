package bassi.itau_unibanco.exerc5_itau_unibanco.dto;

import bassi.itau_unibanco.exerc5_itau_unibanco.entity.ProdutoEntity;
import bassi.itau_unibanco.exerc5_itau_unibanco.model.ProdutoModel;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.UUID;

@UtilityClass
public class ProdutoStub {

    public static ProdutoModel buildProdutoModel(Long id, String nome, BigDecimal preco, String categoria) {
        return new ProdutoModel(id, nome, preco, categoria);
    }

    public static ProdutoEntity buildProdutoEntity(UUID id, String nome, BigDecimal preco, String categoria) {
        return new ProdutoEntity(id, nome, preco, categoria);
    }

    public static ProdutoRequest buildProdutoRequest(String nome, BigDecimal preco, String categoria) {
        return new ProdutoRequest(nome, preco, categoria);
    }

    public static ProdutoModel valid() {
        return new ProdutoModel(1L, "nome", BigDecimal.valueOf(10.00), "categoria");
    }

    public static ProdutoResponse validProdutoResponse() {
        return new ProdutoResponse(UUID.fromString("1429f29a-a611-4212-8418-39df2e8abe5c"), "nome", BigDecimal.valueOf(10.00), "categoria");
    }

    public static ProdutoModel toProdutoModel(ProdutoRequest data) {
        return new ProdutoModel(1L, data.nome(), data.preco(), data.categoria());
    }

    public static ProdutoResponse toProdutoResponse(ProdutoRequest data, UUID id) {
        return new ProdutoResponse(id, data.nome(), data.preco(), data.categoria());
    }

    public static ProdutoEntity toProdutoEntity(ProdutoRequest data, UUID id) {
        return new ProdutoEntity(id, data.nome(), data.preco(), data.categoria());
    }
}
