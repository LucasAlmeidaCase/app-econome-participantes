package com.econome.participantes.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de participante")
public enum TipoParticipante {
    CLIENTE, FORNECEDOR, TRANSPORTADORA, COLABORADOR, ASSISTENCIA_TECNICA, ADMINISTRADORA, CONSULTORIA,
    CONTABILIDADE, OUTROS
}
