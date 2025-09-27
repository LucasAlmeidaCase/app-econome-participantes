package com.econome.participantes.service;

import com.econome.participantes.dto.ParticipanteRequest;
import com.econome.participantes.dto.ParticipanteResponse;

import java.util.List;

/**
 * Contrato de serviço para o contexto de Participantes.
 * Responsável por encapsular regras de negócio ligadas ao ciclo de vida de {@code Participante}
 * e orquestrar o acesso ao repositório. Mantém a API desacoplada da persistência
 * e favorece testabilidade (DIP / SRP).
 */
public interface ParticipanteService {
    /**
     * Cria um novo participante.
     *
     * @param request dados de entrada (validados na camada de API)
     * @return participante criado
     */
    ParticipanteResponse criar(ParticipanteRequest request);

    /**
     * Atualiza um participante existente.
     *
     * @param id      identificador do participante
     * @param request novos dados
     * @return participante atualizado
     * @throws com.econome.participantes.exception.ParticipanteNaoEncontradoException se não existir
     */
    ParticipanteResponse atualizar(Long id, ParticipanteRequest request);

    /**
     * Busca participante por ID.
     *
     * @param id identificador
     * @return participante encontrado
     * @throws com.econome.participantes.exception.ParticipanteNaoEncontradoException se não existir
     */
    ParticipanteResponse buscarPorId(Long id);

    /**
     * Lista todos os participantes.
     *
     * @return lista
     */
    List<ParticipanteResponse> listarTodos();

    /**
     * Exclui um participante pelo ID.
     *
     * @param id identificador
     * @throws com.econome.participantes.exception.ParticipanteNaoEncontradoException se não existir
     */
    void excluir(Long id);
}
