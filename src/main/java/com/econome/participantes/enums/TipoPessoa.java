package com.econome.participantes.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum que representa o tipo de pessoa (física ou jurídica).
 */
@Schema(description = "Tipo de pessoa")
public enum TipoPessoa {
    FISICA, JURIDICA
}
