package com.econome.participantes.controller;

import com.econome.participantes.dto.ParticipanteRequest;
import com.econome.participantes.dto.ParticipanteResponse;
import com.econome.participantes.service.ParticipanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST responsável por expor endpoints do contexto de "Participantes".
 * Mantém-se fino delegando regras ao serviço e usando DTOs para clareza de contrato.
 */
@RestController
@RequestMapping("/api/participantes")
@Validated
@RequiredArgsConstructor
@Tag(name = "Participantes", description = "Operações de CRUD para o recurso Participante")
public class ParticipanteController {

    private final ParticipanteService participanteService;

    /**
     * Lista todos os participantes.
     *
     * @return lista de participantes
     */
    @GetMapping
    @Operation(summary = "Listar participantes", description = "Retorna a lista de todos os participantes")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<ParticipanteResponse> listar() {
        return participanteService.listarTodos();
    }

    @GetMapping("/search")
    @Operation(summary = "Pesquisar participantes", description = "Pesquisa por termo (código, nome ou cpfCnpj). Limite 20.")
    @ApiResponse(responseCode = "200", description = "Lista retornada (possivelmente vazia)")
    public List<ParticipanteResponse> pesquisar(@RequestParam(name = "term", required = false) String term) {
        return participanteService.pesquisar(term);
    }

    /**
     * Busca um participante pelo identificador.
     *
     * @param id identificador do participante
     * @return participante encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar participante por ID", description = "Retorna o participante correspondente ao ID informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participante encontrado"),
            @ApiResponse(responseCode = "404", description = "Participante não encontrado", content = @Content)
    })
    public ParticipanteResponse buscarPorId(@PathVariable Long id) {
        return participanteService.buscarPorId(id);
    }

    /**
     * Cria um novo participante.
     *
     * @param request dados do participante a ser criado
     * @return participante criado com Location apontando para o recurso
     */
    @PostMapping
    @Operation(summary = "Criar participante", description = "Cria um novo participante")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Participante criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito de unicidade", content = @Content)
    })
    public ResponseEntity<ParticipanteResponse> criar(@Valid @RequestBody ParticipanteRequest request) {
        ParticipanteResponse criado = participanteService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    /**
     * Atualiza um participante existente.
     *
     * @param id      identificador
     * @param request dados atualizados
     * @return participante atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar participante", description = "Atualiza os dados de um participante existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participante atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Participante não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito de unicidade", content = @Content)
    })
    public ParticipanteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ParticipanteRequest request) {
        return participanteService.atualizar(id, request);
    }

    /**
     * Exclui um participante.
     *
     * @param id identificador do participante
     * @return 204 se removido
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir participante", description = "Remove um participante pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Participante excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Participante não encontrado", content = @Content)
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        participanteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
