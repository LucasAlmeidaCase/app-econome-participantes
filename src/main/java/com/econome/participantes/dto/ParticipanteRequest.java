package com.econome.participantes.dto;

import com.econome.participantes.enums.TipoParticipante;
import com.econome.participantes.enums.TipoPessoa;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

/**
 * DTO de entrada (record) para criação/atualização de Participante.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ParticipanteRequest(
        @NotBlank(message = "codigo é obrigatório") @Size(max = 50, message = "codigo deve ter no máximo 50 caracteres") String codigo,

        @NotBlank(message = "cpfCnpj é obrigatório") @Size(max = 20, message = "cpfCnpj deve ter no máximo 20 caracteres") String cpfCnpj,

        @NotBlank(message = "nome é obrigatório") @Size(max = 255, message = "nome deve ter no máximo 255 caracteres") String nome,

        @NotNull(message = "tipoPessoa é obrigatório") TipoPessoa tipoPessoa,

        @NotNull(message = "tipoParticipante é obrigatório") TipoParticipante tipoParticipante,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") ZonedDateTime dataHoraCadastro
) {
}
