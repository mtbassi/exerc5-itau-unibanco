package bassi.itau_unibanco.exerc3_itau_unibanco.controller.exception_handler;

import bassi.itau_unibanco.exerc3_itau_unibanco.exception.ProdutoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var fieldErrors = e.getFieldErrors()
                .stream()
                .map(fieldError -> new InvalidParam(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("A solicitação contém parâmetros inválidos.");
        problemDetail.setProperty("invalid-params", fieldErrors);
        return problemDetail;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ProblemDetail handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("A solicitação contém parâmetro inválido.");
        problemDetail.setProperty("invalid-param", new InvalidParam(e.getParameter().getParameterName(), e.getMessage()));
        return problemDetail;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("A API não entendeu a solicitação do cliente.");
        return problemDetail;
    }

    @ExceptionHandler({ProdutoException.class})
    public ProblemDetail handleProdutoException(ProdutoException e) {
        return e.toProblemDetail();
    }

    private record InvalidParam(String name, String reason) {
    }
}
