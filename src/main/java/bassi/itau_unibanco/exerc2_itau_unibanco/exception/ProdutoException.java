package bassi.itau_unibanco.exerc2_itau_unibanco.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@NoArgsConstructor
public class ProdutoException extends RuntimeException {

    public ProdutoException(String message) {
        super(message);
    }

    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Microsserviço Produto internal server error.");
        return problemDetail;
    }
}
