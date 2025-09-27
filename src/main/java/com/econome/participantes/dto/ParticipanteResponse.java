package com.econome.participantes.dto;

import com.econome.participantes.enums.TipoParticipante;
import com.econome.participantes.enums.TipoPessoa;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

/**
 * DTO de saída (record) representando um Participante retornado pela API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ParticipanteResponse(
        @Schema(description = "Identificador único", example = "1") Long id,
        @Schema(description = "Código", example = "PART-001") String codigo,
        @Schema(description = "CPF / CNPJ", example = "12345678901") String cpfCnpj,
        @Schema(description = "Nome / Razão Social", example = "Empresa Exemplo LTDA") String nome,
        @Schema(description = "Tipo da pessoa") TipoPessoa tipoPessoa,
        @Schema(description = "Classificação do participante") TipoParticipante tipoParticipante,
        @Schema(description = "Data/hora de cadastro", example = "2025-09-24T10:15:30.000-03:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") ZonedDateTime dataHoraCadastro) {
}
