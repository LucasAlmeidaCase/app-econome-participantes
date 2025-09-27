package com.econome.participantes.dto;

import com.econome.participantes.enums.TipoParticipante;
import com.econome.participantes.enums.TipoPessoa;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

/**
 * DTO de saída (record) representando um Participante retornado pela API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ParticipanteResponse(
        Long id,
        String codigo,
        String cpfCnpj,
        String nome,
        TipoPessoa tipoPessoa,
        TipoParticipante tipoParticipante,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") ZonedDateTime dataHoraCadastro
) {
}
