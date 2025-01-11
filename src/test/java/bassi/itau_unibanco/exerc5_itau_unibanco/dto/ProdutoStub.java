package bassi.itau_unibanco.exerc5_itau_unibanco.dto;

import bassi.itau_unibanco.exerc5_itau_unibanco.model.ProdutoModel;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class ProdutoStub {

    public static ProdutoModel buildProdutoModel(Long id, String nome, BigDecimal preco, String categoria) {
        return new ProdutoModel(id, nome, preco, categoria);
    }

    public static ProdutoRequest buildProdutoRequest(String nome, BigDecimal preco, String categoria) {
        return new ProdutoRequest(nome, preco, categoria);
    }

    public static ProdutoModel valid() {
        return new ProdutoModel(1L, "nome", BigDecimal.valueOf(10.00), "categoria");
    }

    public static ProdutoModel toProdutoModel(ProdutoRequest data) {
        return new ProdutoModel(1L, data.nome(), data.preco(), data.categoria());
    }
}
