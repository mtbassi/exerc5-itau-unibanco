package bassi.itau_unibanco.exerc5_itau_unibanco.repository;

import bassi.itau_unibanco.exerc5_itau_unibanco.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, UUID> {

    @Query( """
                SELECT p FROM ProdutoEntity p WHERE
                (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND
                (:preco IS NULL OR p.preco = :preco) AND
                (:categoria IS NULL OR p.categoria = :categoria)
            """)
    List<ProdutoEntity> listagemPersonalizada(
            @Param("nome") String nome,
            @Param("preco") BigDecimal preco,
            @Param("categoria") String categoria
    );
}
