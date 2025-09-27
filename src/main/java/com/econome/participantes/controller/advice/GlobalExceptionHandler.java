package com.econome.participantes.controller.advice;

import com.econome.participantes.exception.ParticipanteDuplicadoException;
import com.econome.participantes.exception.ParticipanteNaoEncontradoException;
import com.econome.participantes.exception.ProblemDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ParticipanteNaoEncontradoException.class)
    public ResponseEntity<ProblemDetails> handleNaoEncontrado(ParticipanteNaoEncontradoException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), req.getRequestURI(), List.of());
    }

    @ExceptionHandler(ParticipanteDuplicadoException.class)
    public ResponseEntity<ProblemDetails> handleDuplicado(ParticipanteDuplicadoException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Conflito", ex.getMessage(), req.getRequestURI(), List.of());
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ProblemDetails> handleValidacao(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Requisição inválida", ex.getMessage(), req.getRequestURI(), List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGenerico(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", ex.getMessage(), req.getRequestURI(), List.of());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        List<ProblemDetails.FieldErrorDetails> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::mapFieldError)
                .toList();
        ProblemDetails body = new ProblemDetails(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Requisição inválida",
                "Erros de validação encontrados",
                path,
                fieldErrors
        );
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    private ProblemDetails.FieldErrorDetails mapFieldError(FieldError fe) {
        String message = fe.getDefaultMessage();
        if (message == null || message.isBlank()) {
            message = "Campo '" + fe.getField() + "' inválido";
        }
        return new ProblemDetails.FieldErrorDetails(fe.getField(), message);
    }

    private ResponseEntity<ProblemDetails> build(HttpStatus status, String error, String message, String path,
                                                 List<ProblemDetails.FieldErrorDetails> fieldErrors) {
        ProblemDetails body = new ProblemDetails(
                OffsetDateTime.now(),
                status.value(),
                error,
                message,
                path,
                fieldErrors
        );
        return ResponseEntity.status(status).body(body);
    }
}
