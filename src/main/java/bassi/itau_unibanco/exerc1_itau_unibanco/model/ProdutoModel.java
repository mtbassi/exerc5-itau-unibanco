package bassi.itau_unibanco.exerc1_itau_unibanco.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoModel {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private String categoria;
}