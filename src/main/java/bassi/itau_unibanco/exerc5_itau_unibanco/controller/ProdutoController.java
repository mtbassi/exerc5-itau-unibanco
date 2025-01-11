package bassi.itau_unibanco.exerc5_itau_unibanco.controller;

import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoRequest;
import bassi.itau_unibanco.exerc5_itau_unibanco.dto.ProdutoResponse;
import bassi.itau_unibanco.exerc5_itau_unibanco.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/produto")
@RequiredArgsConstructor
@Tag(name = "1.1. [v1] Produto", description = "API para gerenciamento de produtos, permitindo operações de consulta, cadastro, atualização e exclusão de produtos.")
public class ProdutoController {

    private final ProdutoService service;

    @Operation(summary = "Listar todos os produtos",
            description = "Retorna uma lista completa de todos os produtos cadastrados na plataforma.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProdutoResponse.class)))),
                    @ApiResponse(responseCode = "400", description = "Requisição com parâmetros inválidos.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProdutoResponse> listar() {
        return this.service.listar();
    }

    @Operation(summary = "Consultar produto por ID",
            description = "Retorna os detalhes de um produto específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto encontrado e retornado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição com parâmetros inválidos.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "422", description = "Produto não encontrado para o ID fornecido.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProdutoResponse listarPeloId(@PathVariable UUID id) {
        return this.service.listarPeloId(id);
    }


    @Operation(summary = "Cadastrar novo produto",
            description = "Realiza o cadastro de um novo produto utilizando os dados fornecidos no corpo da requisição.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição com parâmetros inválidos.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "422", description = "Dados inválidos ou ausentes no cadastro.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse cadastrar(@RequestBody @Valid ProdutoRequest data) {
        return this.service.cadastrar(data);
    }


    @Operation(summary = "Atualizar produto existente",
            description = "Atualiza as informações de um produto com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição com parâmetros inválidos.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "422", description = "Produto não encontrado para o ID fornecido.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProdutoResponse atualizar(@PathVariable UUID id, @RequestBody @Valid ProdutoRequest data) {
        return this.service.atualizar(id, data);
    }

    @Operation(summary = "Deletar produto por ID",
            description = "Remove um produto do sistema utilizando o ID fornecido na requisição.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso. Sem conteúdo no corpo da resposta."),
                    @ApiResponse(responseCode = "400", description = "Requisição com parâmetros inválidos.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "422", description = "Produto não encontrado para o ID fornecido.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        this.service.deletar(id);
    }
}
