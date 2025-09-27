package com.econome.participantes.exception;

/**
 * Exceção de domínio indicando violação de unicidade (codigo ou cpfCnpj).
 */
public class ParticipanteDuplicadoException extends RuntimeException {
    public ParticipanteDuplicadoException(String campo, String valor) {
        super("Já existe participante com " + campo + "='" + valor + "'.");
    }
}
