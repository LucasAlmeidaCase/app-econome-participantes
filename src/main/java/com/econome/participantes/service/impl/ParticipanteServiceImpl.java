package com.econome.participantes.service.impl;

import com.econome.domain.Participante;
import com.econome.participantes.dto.ParticipanteMapper;
import com.econome.participantes.dto.ParticipanteRequest;
import com.econome.participantes.dto.ParticipanteResponse;
import com.econome.participantes.exception.ParticipanteDuplicadoException;
import com.econome.participantes.exception.ParticipanteNaoEncontradoException;
import com.econome.participantes.service.ParticipanteService;
import com.econome.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Implementação do serviço de Participantes.
 * Aplica regras transacionais, valida unicidade de campos chaves (codigo, cpfCnpj)
 * e delega o mapeamento entidade/DTO ao MapStruct. Mantém a lógica de negócio
 * fora da camada de controle e da camada de persistência.
 */
@Service
@RequiredArgsConstructor
public class ParticipanteServiceImpl implements ParticipanteService {

    private final ParticipanteRepository participanteRepository;
    private final ParticipanteMapper participanteMapper;

    /**
     * Cria um novo participante a partir dos dados informados.
     * Valida unicidade de código e CPF/CNPJ antes de persistir.
     *
     * @param request dados do participante (validados na API)
     * @return participante criado
     * @throws com.econome.participantes.exception.ParticipanteDuplicadoException se já existir código/cpfCnpj
     */
    @Override
    @Transactional
    public ParticipanteResponse criar(ParticipanteRequest request) {
        validarUnicidade(request.codigo(), request.cpfCnpj(), null);
        Participante entity = participanteMapper.toEntity(request);
        if (entity.getDataHoraCadastro() == null) {
            entity.setDataHoraCadastro(ZonedDateTime.now());
        }
        Participante salvo = participanteRepository.save(entity);
        return participanteMapper.toResponse(salvo);
    }

    /**
     * Atualiza um participante existente.
     * Valida unicidade de código e CPF/CNPJ (exceto para o próprio registro).
     *
     * @param id      identificador do participante
     * @param request dados atualizados
     * @return participante atualizado
     * @throws com.econome.participantes.exception.ParticipanteNaoEncontradoException se não existir
     * @throws com.econome.participantes.exception.ParticipanteDuplicadoException     se já existir código/cpfCnpj em outro registro
     */
    @Override
    @Transactional
    public ParticipanteResponse atualizar(Long id, ParticipanteRequest request) {
        Participante existente = participanteRepository.findById(id)
                .orElseThrow(() -> new ParticipanteNaoEncontradoException(id));
        validarUnicidade(request.codigo(), request.cpfCnpj(), id);
        ZonedDateTime cadastroOriginal = existente.getDataHoraCadastro();
        participanteMapper.updateEntityFromRequest(request, existente);
        // Reforça preservação
        existente.setDataHoraCadastro(cadastroOriginal);
        Participante atualizado = participanteRepository.save(existente);
        return participanteMapper.toResponse(atualizado);
    }

    /**
     * Busca participante por ID.
     *
     * @param id identificador
     * @return participante encontrado
     * @throws com.econome.participantes.exception.ParticipanteNaoEncontradoException se não existir
     */
    @Override
    @Transactional(readOnly = true)
    public ParticipanteResponse buscarPorId(Long id) {
        return participanteRepository.findById(id)
                .map(participanteMapper::toResponse)
                .orElseThrow(() -> new ParticipanteNaoEncontradoException(id));
    }

    /**
     * Lista todos os participantes cadastrados.
     *
     * @return lista de participantes
     */
    @Override
    @Transactional(readOnly = true)
    public List<ParticipanteResponse> listarTodos() {
        return participanteMapper.toResponseList(participanteRepository.findAll());
    }

    /**
     * Exclui participante pelo ID.
     *
     * @param id identificador
     * @throws com.econome.participantes.exception.ParticipanteNaoEncontradoException se não existir
     */
    @Override
    @Transactional
    public void excluir(Long id) {
        if (!participanteRepository.existsById(id)) {
            throw new ParticipanteNaoEncontradoException(id);
        }
        participanteRepository.deleteById(id);
    }

    /**
     * Valida unicidade de codigo e cpfCnpj antes de criar/atualizar.
     *
     * @param codigo  codigo informado
     * @param cpfCnpj documento informado
     * @param idAtual id do registro sendo atualizado (null para criação)
     * @throws com.econome.participantes.exception.ParticipanteDuplicadoException em caso de conflito
     */
    private void validarUnicidade(String codigo, String cpfCnpj, Long idAtual) {
        if (codigo != null) {
            participanteRepository.findByCodigo(codigo).ifPresent(existing -> {
                if (idAtual == null || !existing.getId().equals(idAtual)) {
                    throw new ParticipanteDuplicadoException("codigo", codigo);
                }
            });
        }
        if (cpfCnpj != null) {
            participanteRepository.findByCpfCnpj(cpfCnpj).ifPresent(existing -> {
                if (idAtual == null || !existing.getId().equals(idAtual)) {
                    throw new ParticipanteDuplicadoException("cpfCnpj", cpfCnpj);
                }
            });
        }
    }
}
