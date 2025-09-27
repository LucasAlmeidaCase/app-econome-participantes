package com.econome.participantes.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum que representa o tipo do participante (cliente, fornecedor, etc.).
 */
@Schema(description = "Tipo de participante")
public enum TipoParticipante {
    CLIENTE, FORNECEDOR, TRANSPORTADORA, COLABORADOR, ASSISTENCIA_TECNICA, ADMINISTRADORA, CONSULTORIA,
    CONTABILIDADE, OUTROS
}
