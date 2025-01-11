package bassi.itau_unibanco.exerc2_itau_unibanco.service;

import bassi.itau_unibanco.exerc2_itau_unibanco.dto.ProdutoRequest;
import bassi.itau_unibanco.exerc2_itau_unibanco.exception.ProdutoNaoEncontradoException;
import bassi.itau_unibanco.exerc2_itau_unibanco.mapper.ProdutoMapper;
import bassi.itau_unibanco.exerc2_itau_unibanco.model.ProdutoModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final List<ProdutoModel> produtos;

    private final ProdutoMapper mapper;

    private final AtomicLong nextId = new AtomicLong(1);

    public List<ProdutoModel> listar() {
        return produtos;
    }

    public ProdutoModel listarPeloId(Long id) {
        return this.produtos.stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst()
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    public ProdutoModel cadastrar(ProdutoRequest produtoRequest) {
        var produto = this.mapper.mapToProdutoModel(produtoRequest, nextId.getAndIncrement());
        this.produtos.add(produto);
        return produto;
    }

    public ProdutoModel atualizar(Long id, ProdutoRequest produtoRequest) {
        return this.produtos.stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst()
                .map(p -> {
                    this.atualizar(produtoRequest, p);
                    return p;
                })
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    private void atualizar(ProdutoRequest produtoRequest, ProdutoModel produtoAtual) {
        this.mapper.mapToProdutoModel(produtoRequest, produtoAtual);
    }

    public void deletar(Long id) {
        if (!this.produtos.removeIf(p -> id.equals(p.getId()))) throw new ProdutoNaoEncontradoException(id);
    }
}
