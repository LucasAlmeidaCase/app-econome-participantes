package com.econome.participantes.dto;

import com.econome.domain.Participante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Mapper do contexto de Participantes baseado em MapStruct.
 * Converte entre entidade JPA e DTOs de entrada/saída sem adicionar lógica de negócio.
 */
@Mapper(componentModel = "spring")
public interface ParticipanteMapper {

    /**
     * Cria nova entidade a partir dos dados do request.
     */
    @Mapping(target = "id", ignore = true)
    Participante toEntity(ParticipanteRequest request);

    /**
     * Atualiza entidade existente com dados do request.
     * dataHoraCadastro é intencionalmente ignorado para preservar a data original.
     */
    @Mapping(target = "dataHoraCadastro", ignore = true)
    void updateEntityFromRequest(ParticipanteRequest request, @MappingTarget Participante entity);

    /**
     * Converte entidade em DTO de resposta.
     */
    ParticipanteResponse toResponse(Participante entity);

    /**
     * Converte lista de entidades em lista de DTOs.
     */
    List<ParticipanteResponse> toResponseList(List<Participante> entities);

    /**
     * Ajuste utilitário para preencher dataHoraCadastro se nulo.
     */
    default ZonedDateTime mapNowIfNull(ZonedDateTime value) {
        return value == null ? ZonedDateTime.now() : value;
    }
}
