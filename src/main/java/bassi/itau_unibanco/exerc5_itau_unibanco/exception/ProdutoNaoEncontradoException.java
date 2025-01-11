package bassi.itau_unibanco.exerc5_itau_unibanco.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.UUID;

public class ProdutoNaoEncontradoException extends ProdutoException {

    private final UUID id;

    public ProdutoNaoEncontradoException(UUID id) {
        super("Produto não encontrado pelo id %s.".formatted(id));
        this.id = id;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Produto não encontrado pelo id %s.".formatted(id));
        return problemDetail;
    }
}
