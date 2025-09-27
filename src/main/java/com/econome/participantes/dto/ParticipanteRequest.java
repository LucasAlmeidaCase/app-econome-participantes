package com.econome.participantes.dto;

import com.econome.participantes.enums.TipoParticipante;
import com.econome.participantes.enums.TipoPessoa;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

/**
 * DTO de entrada (record) para criação/atualização de Participante.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ParticipanteRequest", description = "Dados de entrada para criação/atualização de participantes")
public record ParticipanteRequest(
        @Schema(description = "Código interno único do participante", example = "PART-001")
        @NotBlank(message = "codigo é obrigatório") @Size(max = 50, message = "codigo deve ter no máximo 50 caracteres") String codigo,

        @Schema(description = "Documento (CPF ou CNPJ) sem formatação", example = "12345678901")
        @NotBlank(message = "cpfCnpj é obrigatório") @Size(max = 20, message = "cpfCnpj deve ter no máximo 20 caracteres") String cpfCnpj,

        @Schema(description = "Nome completo / Razão Social", example = "Empresa Exemplo LTDA")
        @NotBlank(message = "nome é obrigatório") @Size(max = 255, message = "nome deve ter no máximo 255 caracteres") String nome,

        @Schema(description = "Tipo da pessoa (FISICA ou JURIDICA)")
        @NotNull(message = "tipoPessoa é obrigatório") TipoPessoa tipoPessoa,

        @Schema(description = "Classificação do participante")
        @NotNull(message = "tipoParticipante é obrigatório") TipoParticipante tipoParticipante,

        @Schema(description = "Data/hora do cadastro (timezone)", example = "2025-09-24T10:15:30.000-03:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") ZonedDateTime dataHoraCadastro
) {
}
