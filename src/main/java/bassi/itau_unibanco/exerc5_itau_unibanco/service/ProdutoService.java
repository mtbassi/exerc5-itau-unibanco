package bassi.itau_unibanco.exerc5_itau_unibanco.service;

import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoRequest;
import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoResponse;
import bassi.itau_unibanco.exerc5_itau_unibanco.exception.ProdutoNaoEncontradoException;
import bassi.itau_unibanco.exerc5_itau_unibanco.mapper.ProdutoMapper;
import bassi.itau_unibanco.exerc5_itau_unibanco.model.ProdutoModel;
import bassi.itau_unibanco.exerc5_itau_unibanco.producer.CadastroProdutoProducer;
import bassi.itau_unibanco.exerc5_itau_unibanco.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final List<ProdutoModel> produtos;

    private final ProdutoRepository repository;

    private final ProdutoMapper mapper;

    private final CadastroProdutoProducer producer;

    private final AtomicLong nextId = new AtomicLong(1);

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::mapToProdutoResponse)
                .toList();
    }

    public ProdutoModel listarPeloId(Long id) {
        return this.produtos.stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst()
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    @Transactional
    public ProdutoResponse cadastrar(ProdutoRequest produtoRequest) {
        var entity = this.repository.save(this.mapper.mapToProdutoEntity(produtoRequest));
        this.producer.sendMessage(entity);
        return this.mapper.mapToProdutoResponse(entity);
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
