package com.econome.participantes.dto;

import com.econome.domain.Participante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Mapper MapStruct para o contexto de Participantes.
 */
@Mapper(componentModel = "spring")
public interface ParticipanteMapper {

    @Mapping(target = "id", ignore = true)
    Participante toEntity(ParticipanteRequest request);

    /**
     * Atualiza entidade existente com dados do request.
     * dataHoraCadastro é intencionalmente ignorado para preservar a data original.
     */
    @Mapping(target = "dataHoraCadastro", ignore = true)
    void updateEntityFromRequest(ParticipanteRequest request, @MappingTarget Participante entity);

    ParticipanteResponse toResponse(Participante entity);

    List<ParticipanteResponse> toResponseList(List<Participante> entities);

    /**
     * Ajuste utilitário para preencher dataHoraCadastro se nulo.
     */
    default ZonedDateTime mapNowIfNull(ZonedDateTime value) {
        return value == null ? ZonedDateTime.now() : value;
    }
}
