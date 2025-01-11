package bassi.itau_unibanco.exerc5_itau_unibanco.mapper;

import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoRequest;
import bassi.itau_unibanco.exerc5_itau_unibanco.model.ProdutoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoModel mapToProdutoModel(ProdutoRequest data, long id);

    @Mapping(target = "id", ignore = true)
    void mapToProdutoModel(ProdutoRequest produtoRequest, @MappingTarget ProdutoModel produtoAtual);
}
