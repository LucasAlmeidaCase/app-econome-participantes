package com.econome.participantes.service;

import com.econome.participantes.dto.ParticipanteRequest;
import com.econome.participantes.dto.ParticipanteResponse;

import java.util.List;

/**
 * Contrato de serviço para o contexto de Participantes.
 */
public interface ParticipanteService {
    ParticipanteResponse criar(ParticipanteRequest request);

    ParticipanteResponse atualizar(Long id, ParticipanteRequest request);

    ParticipanteResponse buscarPorId(Long id);

    List<ParticipanteResponse> listarTodos();

    void excluir(Long id);
}
