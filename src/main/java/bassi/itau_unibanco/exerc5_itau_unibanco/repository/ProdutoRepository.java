package bassi.itau_unibanco.exerc5_itau_unibanco.repository;

import bassi.itau_unibanco.exerc5_itau_unibanco.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, UUID> {
}
