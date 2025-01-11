package bassi.itau_unibanco.exerc5_itau_unibanco.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProdutoResponse(
        UUID id,
        String nome,
        BigDecimal preco,
        String categoria
) {
}
