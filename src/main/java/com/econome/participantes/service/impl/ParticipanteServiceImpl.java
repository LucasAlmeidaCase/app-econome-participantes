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

@Service
@RequiredArgsConstructor
public class ParticipanteServiceImpl implements ParticipanteService {

    private final ParticipanteRepository participanteRepository;
    private final ParticipanteMapper participanteMapper;

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

    @Override
    @Transactional(readOnly = true)
    public ParticipanteResponse buscarPorId(Long id) {
        return participanteRepository.findById(id)
                .map(participanteMapper::toResponse)
                .orElseThrow(() -> new ParticipanteNaoEncontradoException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipanteResponse> listarTodos() {
        return participanteMapper.toResponseList(participanteRepository.findAll());
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!participanteRepository.existsById(id)) {
            throw new ParticipanteNaoEncontradoException(id);
        }
        participanteRepository.deleteById(id);
    }

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
