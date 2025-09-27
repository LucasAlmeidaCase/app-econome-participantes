package com.econome.participantes.exception;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Payload padronizado de erros para a API de Participantes.
 */
public record ProblemDetails(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDetails> fieldErrors
) {
    public record FieldErrorDetails(String field, String message) {
    }
}
