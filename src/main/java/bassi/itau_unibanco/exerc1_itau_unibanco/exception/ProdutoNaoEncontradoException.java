package bassi.itau_unibanco.exerc1_itau_unibanco.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ProdutoNaoEncontradoException extends ProdutoException {

    private final Long id;

    public ProdutoNaoEncontradoException(Long id) {
        super("Produto não encontrado pelo id %d.".formatted(id));
        this.id = id;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Produto não encontrado pelo id %d.".formatted(id));
        return problemDetail;
    }
}
