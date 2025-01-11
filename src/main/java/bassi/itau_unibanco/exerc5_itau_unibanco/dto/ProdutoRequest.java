package bassi.itau_unibanco.exerc5_itau_unibanco.dto;

import bassi.itau_unibanco.exerc5_itau_unibanco.util.BigDecimalTwoDecimalDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProdutoRequest(
        @NotBlank
        String nome,
        @NotNull
        @JsonDeserialize(using = BigDecimalTwoDecimalDeserializer.class)
        @DecimalMin("0.00")
        @Digits(integer = 10, fraction = 2)
        BigDecimal preco,
        @NotBlank
        String categoria
) {
}
