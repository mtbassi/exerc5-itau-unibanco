package bassi.itau_unibanco.exerc5_itau_unibanco.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ProdutoModel {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private String categoria;
}